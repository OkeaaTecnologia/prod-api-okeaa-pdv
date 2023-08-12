package br.com.okeaa.apiokeaaproduto.repositories.deposito;

import br.com.okeaa.apiokeaaproduto.controllers.request.deposito.DepositoRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositoRequestRepository extends JpaRepository<DepositoRequest, Long> {

    Optional<DepositoRequest> findById(Long idDeposito);
    @Query("SELECT c FROM DepositoRequest c WHERE c.descricao = :descricao")
    List<DepositoRequest> findByDescricao(@Param("descricao") String descricao);

}

