package br.com.okeaa.apiokeaapdv.repositories.contato;

import br.com.okeaa.apiokeaapdv.controllers.response.contato.ContatoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContatoResponseRepository extends JpaRepository<ContatoResponse, Long> {

    Optional<ContatoResponse> findById(Long id);

    @Query("SELECT c.cnpj FROM ContatoResponse c")
    List<String> findAllDescricao();

}

