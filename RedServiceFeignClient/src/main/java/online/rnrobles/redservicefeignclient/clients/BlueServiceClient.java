package online.rnrobles.redservicefeignclient.clients;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "blue-service")
@LoadBalancerClient(name = "blue-service", configuration = LoadBalancerConfiguration.class)
public interface BlueServiceClient {
	@RequestMapping(method = RequestMethod.GET, value = "/config/name")
	public ResponseEntity<String> getApplicationName();
}
