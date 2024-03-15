package com.example.springsecurityturtle.auth;

import com.example.springsecurityturtle.config.JwtService;
import com.example.springsecurityturtle.repositories.UserRepository;
import com.example.springsecurityturtle.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    //cho phép tại 1 người dùng mới lưu vào trong database và trả lại token đã được tạo
    public AuthenticationResponse register(RegisterRequest request) {
        // var là cách khai báo 1 biến không cần chỉ định kiều dữ liệu. (được giới thiệu trong Java10)
        var user = User.builder()
                .fristName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))//mã hóa password
                .role(request.getRole())
                .build();

        // nên validate dữ liệu sau khi lấy dữ liệu từ client gửi về để tăng tính bảo mật

        repository.save(user);

        //tạo token
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    //xác thực người dùng
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        //xác thực thông tin người dùng

        //trước khi xác thực người dùng nên validate dữ liệu để tránh việc dữ liệu có thể
        // bị thay ổi trong quá trình gửi từ client đến máy chủ
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        //lấy tìm user trong database để tạo token
        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        //tạo token
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
