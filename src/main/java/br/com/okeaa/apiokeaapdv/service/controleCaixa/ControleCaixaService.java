package br.com.okeaa.apiokeaapdv.service.controleCaixa;

import br.com.okeaa.apiokeaapdv.controllers.request.pedido.ParcelaRequest;
import br.com.okeaa.apiokeaapdv.controllers.response.controleCaixa.ControleCaixa;
import br.com.okeaa.apiokeaapdv.controllers.response.controleCaixa.ControleCaixaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ControleCaixaService {

    List<ControleCaixa> getAllControleCaixa();

    Optional<ControleCaixa> getControleCaixaById(String id);

    void deleteControleCaixa(String idLoja);

    ResponseEntity<String> saveControleCaixa(@RequestBody String controleCaixa);

    ResponseEntity<String> saveControleCaixaOP(String loja, List<ParcelaRequest> parcelaList);

    public ResponseEntity<String> updateControleCaixa(String controleCaixa, String id);

 }
