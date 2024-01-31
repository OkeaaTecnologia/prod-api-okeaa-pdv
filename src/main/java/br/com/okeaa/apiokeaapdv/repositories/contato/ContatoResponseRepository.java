package br.com.okeaa.apiokeaapdv.repositories.contato;

import br.com.okeaa.apiokeaapdv.controllers.request.contato.ContatoRequest;
import br.com.okeaa.apiokeaapdv.controllers.response.contato.ContatoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContatoResponseRepository extends JpaRepository<ContatoResponse, Long> {

    Optional<ContatoResponse> findById(Long id);

    @Query("SELECT c.cnpj FROM ContatoResponse c")
    List<String> findAllDescricao();

    @Query("SELECT c FROM ContatoResponse c WHERE c.cnpj = :cnpj")
    List<ContatoResponse> findByCpfCnpj(@Param("cnpj") String cnpj);

    @Query("SELECT c FROM ContatoResponse c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<ContatoResponse> findContactByName(@Param("nome") String nome);

    @Query("SELECT c FROM ContatoResponse c WHERE LOWER(c.codigo) LIKE LOWER(CONCAT('%', :codigo, '%'))")
    List<ContatoResponse> findContactByCode(@Param("codigo") String codigo);

    @Query("SELECT c FROM ContatoResponse c WHERE REPLACE(REPLACE(REPLACE(c.cnpj, '.', ''), '-', ''), '/', '') = :cnpj")
    List<ContatoResponse> findContactByCnpj(@Param("cnpj") String cnpj);
}

