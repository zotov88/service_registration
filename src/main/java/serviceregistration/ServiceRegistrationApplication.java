package serviceregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceRegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistrationApplication.class, args);
	}

}

