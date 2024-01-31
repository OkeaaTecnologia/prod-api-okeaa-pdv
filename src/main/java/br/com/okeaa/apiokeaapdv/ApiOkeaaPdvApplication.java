package br.com.okeaa.apiokeaapdv;

import br.com.okeaa.apiokeaapdv.service.contato.ContatoService;
import br.com.okeaa.apiokeaapdv.service.formaPagamento.FormaPagamentoService;
import br.com.okeaa.apiokeaapdv.service.pedido.PedidoService;
import br.com.okeaa.apiokeaapdv.service.selecionarLoja.SelecionaLojaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class ApiOkeaaPdvApplication {

	@Autowired
	public ContatoService contatoService;

	@Autowired
	public FormaPagamentoService formaPagamentoService;

	@Autowired
	public PedidoService pedidoService;

	@Autowired
	public SelecionaLojaService selecionaLojaService;

	public static void main(String[] args) {
		SpringApplication.run(ApiOkeaaPdvApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Este método é chamado uma vez no início.
		doInit();
	}

//	@Scheduled(fixedRate = 300000) // 300.000 milissegundos = 05 Minutos.
//	public void scheduledInit() {
//		// Este método será chamado periodicamente a cada 05 minutos.
//		doInit();
//	}

	public void doInit() {
		// Defina os valores desejados para paginação
		int pagina = 1; // substitua pelo número da página desejada

		contatoService.getAllContacts(pagina);
		formaPagamentoService.getAllFormaPagamento();
		pedidoService.getAllPedido();
		selecionaLojaService.getAllLojas();
	}

}
