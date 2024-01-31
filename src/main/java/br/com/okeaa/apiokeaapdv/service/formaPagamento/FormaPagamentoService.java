package br.com.okeaa.apiokeaapdv.service.formaPagamento;

import br.com.okeaa.apiokeaapdv.controllers.response.formaPagamento.JsonResponseFormaPagamento;
import br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento.JsonRequestFormaPagamento;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface FormaPagamentoService {

    JsonResponseFormaPagamento getAllFormaPagamento();

    JsonResponseFormaPagamento getFormaPagamentoById(@PathVariable("id") String id);

    ResponseEntity<String> deleteFormaPagemento(@PathVariable("id") String id);

    ResponseEntity<String> createFormaPagamento(@RequestBody String xmlFormaPagamento);

    ResponseEntity<String> updateFormaPagamento(@RequestBody String xmlFormaPagamento, @PathVariable("id") String id);
}