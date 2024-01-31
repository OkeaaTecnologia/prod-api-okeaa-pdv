package br.com.okeaa.apiokeaapdv.repositories.selecionarLoja;

import br.com.okeaa.apiokeaapdv.controllers.response.selecionarLoja.SelecionaLoja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SelecionaLojaRepository extends JpaRepository<SelecionaLoja, Long> {

    List<SelecionaLoja> findAll();

    Optional<SelecionaLoja> findByIdLoja(String idLoja);

    void deleteByIdLoja(String idLoja);

    @Query("SELECT s FROM SelecionaLoja s WHERE s.id = :id")
    Optional<SelecionaLoja> findId(@Param("id") String id);

}

