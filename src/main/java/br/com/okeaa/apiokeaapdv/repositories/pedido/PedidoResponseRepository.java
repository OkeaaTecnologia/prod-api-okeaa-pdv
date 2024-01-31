package br.com.okeaa.apiokeaapdv.repositories.pedido;

import br.com.okeaa.apiokeaapdv.controllers.response.pedido.PedidoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoResponseRepository extends JpaRepository<PedidoResponse, String> {

    @Query("SELECT c.numero FROM PedidoResponse c")
    List<String> findAllNumero();

    @Query("SELECT c FROM PedidoResponse c WHERE c.numero = :numero")
    Optional<PedidoResponse> findByNumero(@Param("numero") String numero);
}

