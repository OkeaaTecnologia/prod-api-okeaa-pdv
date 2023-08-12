package br.com.okeaa.apiokeaapdv.repositories.contato;


import br.com.okeaa.apiokeaapdv.controllers.request.contato.TiposContatoRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TiposContatoRequestRepository extends JpaRepository<TiposContatoRequest, Long> {

    Optional<TiposContatoRequest> findById(Long id);
}
