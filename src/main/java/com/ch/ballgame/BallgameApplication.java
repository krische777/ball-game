package com.ch.ballgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;

@SpringBootApplication(exclude = {ValidationAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class})
public class BallgameApplication {

	public static void main(String[] args) {
		SpringApplication.run(BallgameApplication.class, args);
	}

}
