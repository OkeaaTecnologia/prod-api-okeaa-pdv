package br.com.okeaa.apiokeaapdv.repositories.pedido;

import br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento.FormaPagamentoRequest;
import br.com.okeaa.apiokeaapdv.controllers.request.pedido.PedidoRequest;
import br.com.okeaa.apiokeaapdv.controllers.response.contato.ContatoResponse;
import br.com.okeaa.apiokeaapdv.controllers.response.pedido.PedidoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRequestRepository extends JpaRepository<PedidoRequest, String> {

    @Query("SELECT c FROM PedidoRequest c WHERE c.numero = :numero")
    Optional<PedidoRequest> findByNumero(@Param("numero") String numero);

    Optional<PedidoRequest> findByFlagAndNumero(String flag, String numero);

    @Query("SELECT c FROM PedidoRequest c WHERE c.numero_loja = :numero_loja")
    Optional<PedidoRequest> findByNumeroLoja(@Param("numero_loja") String numero_loja);

    @Query("SELECT c FROM PedidoRequest c WHERE c.flag = :flag")
    Optional<PedidoRequest> findFlag(@Param("flag") String flag);

}