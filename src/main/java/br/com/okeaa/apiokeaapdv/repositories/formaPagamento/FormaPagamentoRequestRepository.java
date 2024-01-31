package br.com.okeaa.apiokeaapdv.repositories.formaPagamento;

import br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento.FormaPagamentoRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormaPagamentoRequestRepository extends JpaRepository<FormaPagamentoRequest, Long> {

    Optional<FormaPagamentoRequest> findById(Long idProduto);

    @Query("SELECT c FROM FormaPagamentoRequest c WHERE c.descricao = :descricao")
    List<FormaPagamentoRequest> findByDescricao(@Param("descricao") String descricao);
}
