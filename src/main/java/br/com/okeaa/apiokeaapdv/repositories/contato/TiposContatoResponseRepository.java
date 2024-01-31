package br.com.okeaa.apiokeaapdv.repositories.contato;


import br.com.okeaa.apiokeaapdv.controllers.response.contato.ContatoResponse;
import br.com.okeaa.apiokeaapdv.controllers.response.contato.TiposContatoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TiposContatoResponseRepository extends JpaRepository<TiposContatoResponse, Long> {

    Optional<TiposContatoResponse> findById(Long id);

    Optional<TiposContatoResponse> findByTipoContatoIdAndContatoResponseId(Long tipoContatoId, Long contatoResponseId);


}
