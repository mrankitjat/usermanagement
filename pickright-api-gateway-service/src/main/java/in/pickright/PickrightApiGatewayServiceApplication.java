package in.pickright;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PickrightApiGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PickrightApiGatewayServiceApplication.class, args);
	}

}
