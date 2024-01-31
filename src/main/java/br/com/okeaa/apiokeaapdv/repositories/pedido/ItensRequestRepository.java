package br.com.okeaa.apiokeaapdv.repositories.pedido;

import br.com.okeaa.apiokeaapdv.controllers.request.pedido.ItensRequest;
import br.com.okeaa.apiokeaapdv.controllers.response.pedido.ItemResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItensRequestRepository extends JpaRepository<ItensRequest, String> {

}
