package com.example.springsecurityturtle.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//tạo, đăng ký 1 tài khoản mới và xác thực 1 người dùng
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    //dùng để đăng ký
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        //trả về cho client, JWT token
        return ResponseEntity.ok(service.register(request));
    }

    //dùng để xác thực
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        //trả về cho client, JWT token
        return ResponseEntity.ok(service.authenticate(request));
    }
}
