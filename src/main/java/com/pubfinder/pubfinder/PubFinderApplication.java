package com.pubfinder.pubfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(
		exclude = {
				SecurityAutoConfiguration.class,
				UserDetailsServiceAutoConfiguration.class,
		})
@EnableCaching
public class PubFinderApplication {
	public static void main(String[] args) {
		SpringApplication.run(PubFinderApplication.class, args);
	}

}
