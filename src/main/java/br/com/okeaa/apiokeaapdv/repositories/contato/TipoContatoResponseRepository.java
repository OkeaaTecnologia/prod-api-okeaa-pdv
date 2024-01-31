package br.com.okeaa.apiokeaapdv.repositories.contato;

import br.com.okeaa.apiokeaapdv.controllers.response.contato.TipoContatoResponse;
import br.com.okeaa.apiokeaapdv.controllers.response.selecionarLoja.SelecionaLoja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoContatoResponseRepository extends JpaRepository<TipoContatoResponse, Long> {

    @Transactional
    Optional<TipoContatoResponse> findById(Long id);

    @Transactional
    Optional<TipoContatoResponse> findByDescricao(String descricao);

    List<TipoContatoResponse> findAllByDescricao(String descricao);


    List<TipoContatoResponse> findAll();

}
