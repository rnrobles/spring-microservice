package online.rnrobles.redserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class ConfigServerClient {
	
	@Value("${application.name}")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		name = name;
	}

}
