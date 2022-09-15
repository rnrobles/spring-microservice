package online.rnrobles.redserver.controller;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import online.rnrobles.redserver.config.ConfigServerClient;

@RestController
@RequestMapping("/config")
public class ConfigServerController implements ApplicationContextAware {

	private static final Logger log = LoggerFactory.getLogger(ConfigServerController.class);

	private ApplicationContext context;

	@Autowired
	ConfigServerClient config;

	@GetMapping
	public String getConfiguracion() {
		log.info(config.getType());
		return config.getType();
	}

	@GetMapping("/name")
	public String nameService() {
		log.info(config.getName());
		return config.getName();
	}

	@GetMapping("/shutdown")
	public String shutDown() {
		log.info("Bye");
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.submit(() -> {
			shutdown();
		});
		return "context is shutdown";
	}

	@GetMapping("/run/{name}/{port}")
	public String newService(@PathVariable String name, @PathVariable String port) throws IOException, HttpClientErrorException {
		String homeDirectory = System.getProperty("user.home");
		log.info(homeDirectory);
		String validName = "";

		if (name.equals("red-service") || name.equals("blue-service") || name.equals("green-service")) {
			validName = name;
		} else {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Nombre del servicio invalido");
		}

		try {
			Integer portInt = Integer.parseInt(port);
			Runtime rt = Runtime.getRuntime();

			Process pr = rt.exec("java -Dspring.application.name=" + validName + " -Dserver.port=" + portInt.toString()
					+ " -jar /home/user/repos/RedServer.jar");
		} catch (Exception e) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Puerto invalido");
		}

		return "Iniciando el servicio " + validName + "en el puerto " + port;
	}

	@Async
	public void shutdown() {
		try {
			ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) context;
			ctx.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}
}
