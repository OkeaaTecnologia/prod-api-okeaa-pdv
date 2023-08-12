package br.com.okeaa.apiokeaapdv.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = {"br.com.okeaa.apiokeaapdv.repositories"})
@EntityScan(basePackages = {"br.com.okeaa.apiokeaapdv.controllers.response", "br.com.okeaa.apiokeaapdv.controllers.request"})
public class ApplicationConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
