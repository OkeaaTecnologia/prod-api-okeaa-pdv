package br.com.okeaa.apiokeaapdv.service.selecionarLoja;

import br.com.okeaa.apiokeaapdv.controllers.response.selecionarLoja.SelecionaLoja;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface SelecionaLojaService {

    List<SelecionaLoja> getAllLojas();

    Optional<SelecionaLoja> getLojaById(String idLoja);

    void deleteLojaById(String idLoja);

    ResponseEntity<String> saveLoja(@RequestBody String selecionaLoja);

    ResponseEntity<String> updateLoja(@RequestBody String selecionaLoja, @PathVariable("id") String id);


}
