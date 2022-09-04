package online.rnrobles.redservicefeignclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;

import online.rnrobles.redservicefeignclient.clients.BlueServiceClient;
import online.rnrobles.redservicefeignclient.clients.RedServiceClient;

@SpringBootApplication
@EnableFeignClients
public class RedServiceFeignClientApplication implements ApplicationRunner {

	@Autowired
	private RedServiceClient redServiceClient;
	
	@Autowired
	private BlueServiceClient blueServiceClient;

	private static final Logger log = LoggerFactory.getLogger(RedServiceFeignClientApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RedServiceFeignClientApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		for (int i = 0; i < 10; i++) {
			ResponseEntity<String> responseEntity = redServiceClient.getApplicationName();	
			log.info("Status {}, Body {}", responseEntity.getStatusCode(), responseEntity.getBody());
		}
		
		for (int i = 0; i < 10; i++) {
			ResponseEntity<String> responseEntity = blueServiceClient.getApplicationName();
			log.info("Status {}, Body {}", responseEntity.getStatusCode(), responseEntity.getBody());
		}

	}

}
