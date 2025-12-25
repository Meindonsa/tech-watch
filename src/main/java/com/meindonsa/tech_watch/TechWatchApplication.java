package com.meindonsa.tech_watch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com")
@SpringBootApplication
@EnableAutoConfiguration
public class TechWatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechWatchApplication.class, args);
	}

}
