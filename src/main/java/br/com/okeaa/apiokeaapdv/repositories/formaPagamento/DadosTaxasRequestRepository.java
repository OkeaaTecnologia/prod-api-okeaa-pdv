package br.com.okeaa.apiokeaapdv.repositories.formaPagamento;

import br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento.DadosTaxasRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DadosTaxasRequestRepository extends JpaRepository<DadosTaxasRequest, Long> {

}