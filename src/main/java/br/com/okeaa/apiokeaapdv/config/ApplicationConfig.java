package br.com.okeaa.apiokeaapdv.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
		"br.com.okeaa.apiokeaapdv.repositories.contato",
		"br.com.okeaa.apiokeaapdv.repositories.controleCaixa",
		"br.com.okeaa.apiokeaapdv.repositories.formaPagamento",
		"br.com.okeaa.apiokeaapdv.repositories.pedido",
		"br.com.okeaa.apiokeaapdv.repositories.selecionarLoja"
})
@EntityScan(basePackages = {
		"br.com.okeaa.apiokeaapdv.controllers.response.contato",
		"br.com.okeaa.apiokeaapdv.controllers.response.controleCaixa",
		"br.com.okeaa.apiokeaapdv.controllers.response.formaPagamento",
		"br.com.okeaa.apiokeaapdv.controllers.response.pedido",
		"br.com.okeaa.apiokeaapdv.controllers.response.selecionarLoja",

		"br.com.okeaa.apiokeaapdv.controllers.request.contato",
		"br.com.okeaa.apiokeaapdv.controllers.request.controleCaixa",
		"br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento",
		"br.com.okeaa.apiokeaapdv.controllers.request.pedido",
		"br.com.okeaa.apiokeaapdv.controllers.request.selecionarLoja"
})
@ComponentScan(basePackages = {
		"br.com.okeaa.apiokeaapdv.service.contato",
		"br.com.okeaa.apiokeaapdv.service.controleCaixa",
		"br.com.okeaa.apiokeaapdv.service.formaPagamento",
		"br.com.okeaa.apiokeaapdv.service.pedido",
		"br.com.okeaa.apiokeaapdv.service.selecionarLoja"
})
public class ApplicationConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
