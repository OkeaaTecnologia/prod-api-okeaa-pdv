package br.com.okeaa.apiokeaapdv.repositories.pedido;

import br.com.okeaa.apiokeaapdv.controllers.request.pedido.ParcelaRequest;
import br.com.okeaa.apiokeaapdv.controllers.response.pedido.ParcelaResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelaRequestRepository extends JpaRepository<ParcelaRequest, String> {

}
