package br.com.okeaa.apiokeaapdv.service.selecionarLoja;

import br.com.okeaa.apiokeaapdv.controllers.response.selecionarLoja.SelecionaLoja;

import java.util.List;
import java.util.Optional;

public interface SelecionaLojaService {

    List<SelecionaLoja> getAllLojas();

    Optional<SelecionaLoja> getLojaById(String idLoja);

    void deleteLojaById(String idLoja);

    SelecionaLoja saveLoja(SelecionaLoja selecionaLoja);

}
