package tqsua.DeliveriesServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tqsua.DeliveriesServer.repository.RiderRepository;
import tqsua.DeliveriesServer.security.UserDetailsServiceImpl;

@SpringBootApplication
public class DeliveriesServerApplication {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsServiceImpl userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	public static void main(String[] args) {
		SpringApplication.run(DeliveriesServerApplication.class, args);
	}

}
