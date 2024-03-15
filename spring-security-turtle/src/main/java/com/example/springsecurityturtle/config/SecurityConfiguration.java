package com.example.springsecurityturtle.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.springsecurityturtle.user.Permission.ADMIN_READ;
import static com.example.springsecurityturtle.user.Permission.USER_READ;
import static com.example.springsecurityturtle.user.Role.ADMIN;
import static com.example.springsecurityturtle.user.Role.USER;
import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity

@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);// vô hiệu hóa bảo vệ Cross-Site Request Forgery (CSRF)
        http.authorizeHttpRequests((request) -> request
                .requestMatchers("/api/v1/auth/**")
                .permitAll()

                .requestMatchers("/api/v1/demo/hello").hasAnyAuthority(ADMIN.name(), USER.name())
                .requestMatchers(GET ,"/api/v1/demo/hello").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())

                .requestMatchers("/api/v1/demo/turtle").hasAuthority(ADMIN.name())
                .requestMatchers(GET,"/api/v1/demo/turtle").hasAuthority(ADMIN_READ.name())


                .anyRequest()
                .authenticated()
        );
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
