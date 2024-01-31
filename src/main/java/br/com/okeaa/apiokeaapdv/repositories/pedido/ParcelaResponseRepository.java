package br.com.okeaa.apiokeaapdv.repositories.pedido;

import br.com.okeaa.apiokeaapdv.controllers.response.pedido.ItemResponse;
import br.com.okeaa.apiokeaapdv.controllers.response.pedido.ParcelaResponse;
import br.com.okeaa.apiokeaapdv.controllers.response.pedido.ParcelasResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParcelaResponseRepository extends JpaRepository<ParcelaResponse, String> {

}