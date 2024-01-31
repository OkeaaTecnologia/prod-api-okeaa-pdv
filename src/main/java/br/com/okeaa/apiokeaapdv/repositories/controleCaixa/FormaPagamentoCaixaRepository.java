package br.com.okeaa.apiokeaapdv.repositories.controleCaixa;

import br.com.okeaa.apiokeaapdv.controllers.response.controleCaixa.ControleCaixa;
import br.com.okeaa.apiokeaapdv.controllers.response.controleCaixa.FormaPagamentoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FormaPagamentoCaixaRepository extends JpaRepository<FormaPagamentoCaixa, Long> {




}
