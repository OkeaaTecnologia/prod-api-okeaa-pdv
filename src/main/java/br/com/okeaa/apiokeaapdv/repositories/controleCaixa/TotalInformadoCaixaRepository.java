package br.com.okeaa.apiokeaapdv.repositories.controleCaixa;

import br.com.okeaa.apiokeaapdv.controllers.response.controleCaixa.TotalInformadoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface  TotalInformadoCaixaRepository extends JpaRepository<TotalInformadoCaixa, Long> {

//    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TotalInformadoCaixa t WHERE t.idPagamento = ?1 AND t.descricaoPagamento = ?2")
//    boolean existsByIdPagamentoAndDescricaoPagamento(Long idPagamento, String descricaoPagamento);
}

