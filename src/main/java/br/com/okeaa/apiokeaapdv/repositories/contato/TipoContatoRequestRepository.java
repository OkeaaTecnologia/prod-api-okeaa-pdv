package br.com.okeaa.apiokeaapdv.repositories.contato;

import br.com.okeaa.apiokeaapdv.controllers.request.contato.TipoContatoRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoContatoRequestRepository extends JpaRepository<TipoContatoRequest, Long> {

    Optional<TipoContatoRequest> findById(Long id);

    Optional<TipoContatoRequest> findByDescricao(String descricao);

}
