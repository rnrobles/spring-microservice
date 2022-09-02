package online.rnrobles.redserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import online.rnrobles.redserver.config.ConfigServerClient;

@RestController
@RequestMapping("/config")
public class ConfigServerController {

	private static final Logger log = LoggerFactory.getLogger(ConfigServerController.class);

	@Autowired
	ConfigServerClient config;
	
	@GetMapping
	public String getConfiguracion()
	{
		log.info(config.getName());
		return config.getName();
	}
}
