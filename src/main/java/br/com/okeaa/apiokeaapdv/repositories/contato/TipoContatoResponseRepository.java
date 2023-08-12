package br.com.okeaa.apiokeaapdv.repositories.contato;

import br.com.okeaa.apiokeaapdv.controllers.response.contato.TipoContatoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoContatoResponseRepository extends JpaRepository<TipoContatoResponse, Long> {

    Optional<TipoContatoResponse> findById(Long id);

    Optional<TipoContatoResponse> findByDescricao(String descricao);


}
