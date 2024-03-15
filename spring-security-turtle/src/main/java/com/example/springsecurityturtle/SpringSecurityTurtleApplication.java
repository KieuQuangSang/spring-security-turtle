package com.example.springsecurityturtle;

import com.example.springsecurityturtle.auth.AuthenticationService;
import com.example.springsecurityturtle.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.springsecurityturtle.user.Role.ADMIN;
import static com.example.springsecurityturtle.user.Role.USER;

@SpringBootApplication
public class SpringSecurityTurtleApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringSecurityTurtleApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	){
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("turtle")
					.lastname("mr.")
					.email("turtle@gmail.com")
					.password("123")
					.role(ADMIN)
					.build();
			System.out.println("Admin token:" + service.register(admin).getToken());

			var user = RegisterRequest.builder()
					.firstname("fish")
					.lastname("mr.")
					.email("fish@gmail.com")
					.password("123")
					.role(USER)
					.build();
			System.out.println("User token:" + service.register(user).getToken());
		};
	}

}
