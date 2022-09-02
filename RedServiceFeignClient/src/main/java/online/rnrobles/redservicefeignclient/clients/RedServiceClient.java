package online.rnrobles.redservicefeignclient.clients;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "red-service")
@LoadBalancerClient(name = "red-service", configuration = LoadBalancerConfiguration.class)
public interface RedServiceClient {

	@RequestMapping(method = RequestMethod.GET, value = "/config")
	public ResponseEntity<String> getApplicationName();
}
