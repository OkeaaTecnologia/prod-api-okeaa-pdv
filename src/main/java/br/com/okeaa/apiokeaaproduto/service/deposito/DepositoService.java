package br.com.okeaa.apiokeaaproduto.service.deposito;

import br.com.okeaa.apiokeaaproduto.controllers.request.deposito.JsonRequest;
import br.com.okeaa.apiokeaaproduto.controllers.response.deposito.JsonResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface DepositoService {

    JsonResponse getAllDeposit();

    JsonResponse getDepositById(@PathVariable("idDeposito") String idDeposito);

    JsonRequest createDeposit(@RequestBody String xmlDeposito);

    JsonRequest updateDeposit(@RequestBody String xmlDeposito, @PathVariable("idDeposito") String idDeposito);

}
