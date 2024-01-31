package br.com.okeaa.apiokeaapdv.repositories.formaPagamento;

import br.com.okeaa.apiokeaapdv.controllers.response.formaPagamento.FormaPagamentoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormaPagamentoResponseRepository extends JpaRepository<FormaPagamentoResponse, Long> {

    Optional<FormaPagamentoResponse> findById(Long id);

//    @Query("SELECT c FROM FormaPagamentoResponse c WHERE c.codigoFabricante = :codigoFabricante AND c.idFabricante = :idFabricante")
//    Optional<FormaPagamentoResponse> findByFabricante(@Param("codigoFabricante") String codigoFabricante, @Param("idFabricante") String idFabricante);

    @Query("SELECT c FROM FormaPagamentoResponse c WHERE c.descricao = :descricao")
    List<FormaPagamentoResponse> findByDescricao(@Param("descricao") String descricao);

    @Query("SELECT c.descricao FROM FormaPagamentoResponse c")
    List<String> findAllCodigo();
}