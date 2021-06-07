package tqsua.OrdersServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import tqsua.OrdersServer.security.UserDetailsServiceImpl;

@SpringBootApplication
public class OrdersServerApplication {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsServiceImpl userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	public static void main(String[] args) {
		SpringApplication.run(OrdersServerApplication.class, args);
	}

}
