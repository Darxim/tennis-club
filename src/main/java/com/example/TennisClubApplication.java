package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Třída pro spuštění aplikace
 */
@SpringBootApplication
@EnableConfigurationProperties(DataInitialization.class)
public class TennisClubApplication {
	public static final Repository repository = new Repository();

	public static void main(String[] args) {
		SpringApplication.run(TennisClubApplication.class, args);
	}
}
