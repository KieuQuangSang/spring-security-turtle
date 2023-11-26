package com.example.springsecurityturtle.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "4585fdc1f3069f0596109887dc8ed3338fe5fba6ee03562439149360d667d5c2";

    //lấy email từ jwt
    public String extractUsername(String token) {

        //Có nhiều loại claim ví dụ: claim sub(Subject) là Tên người dùng hoặc ID của người dùng,
        //claim exp(expiration time) là Thời gian hết hạn của token, .... tham khảo thêm về cấu tạo của JWT.
        return extractClaim(token, Claims::getSubject); // Claims::getSubject để lấy Tên người dùng hoặc ID của người dùng.
    }

    //extractClaim lấy 1 dữ liệu(claim) cụ thể từ JWT
    //Hàm claimsResolver() được sử dụng để lấy claim (dữ liệu) cụ thể từ đối tượng claims.
    //Hàm claimsResolver này có thể trả về một giá trị bất kỳ, tùy thuộc vào claim (dữ liệu) cần lấy.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token); //extractAllClaims() để lấy tất cả các claim (dữ liệu) của token JWT.
        return claimsResolver.apply(claims); //apply(claims) có nghĩa là áp dụng hàm claimsResolver() cho đối tượng claims
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    //tạo token JWT
    public String generateToken(
            Map<String, Objects> extraClaim,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaim)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // kiểm tra ngày hết hạn của JWT với ngày hiện tại
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // lấy ngày hết hạn của JWT
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //token đang được mã hóa nên hàm extractAllClaims này dùng để giải mã token để lấy các dữ liệu có trong
    //token. fun này trả về các claims(dữ liệu) của token. Có thể sử dụng các dữ liệu của token
    //bằng cách sử dụng đối tượng Claims
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())//cần signinkey để giả mã jwt
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Tạo SignInKey để giải mã JWT
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);// giải mã SECRET_KEY từ Base64 sang Byte
        return Keys.hmacShaKeyFor(keyBytes);//tạo khóa giải mã JWT bằng thuật toán HMAC
    }
}
