package tqsua.DeliveriesServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.security.UserDetailsServiceImpl;
import tqsua.DeliveriesServer.service.RiderService;

@SpringBootApplication
@ComponentScan
public class DeliveriesServerApplication {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsServiceImpl userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Autowired
	public RiderService service;

	@Bean
	public Rider initializeAdmin(){
		if (!service.existsRiderByEmail("admin@gmail.com")) {
			var rider = new Rider("admin", "admin", "admin@gmail.com", "admin123", 5.0, "Online");
			rider.setAccountType("Admin");
			rider.setLat(48.0);
			rider.setLng(-8.0);
			service.saveRider(rider);
			return rider;
		} else {
			return null;
		}

	}

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(DeliveriesServerApplication.class);
		application.run(args);
	}
}
