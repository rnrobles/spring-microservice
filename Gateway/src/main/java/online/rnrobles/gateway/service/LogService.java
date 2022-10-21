package online.rnrobles.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

import online.rnrobles.gateway.config.GatewayConfig;

@Service
public class LogService {

	private static final Logger log = LoggerFactory.getLogger(LogService.class);

	@Autowired
	Tracer tracer;

	public void log(String name, String message) {
		Span newSpan = tracer.nextSpan().name(name);

		try (Tracer.SpanInScope ws = this.tracer.withSpan(newSpan.start())) {
			log.info(message);
		} finally {
			newSpan.end();
		}
	}
}
