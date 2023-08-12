package br.com.okeaa.apiokeaaproduto.repositories.produtofornecedor;

import br.com.okeaa.apiokeaaproduto.controllers.response.produtofornecedor.ProdutoFornecedorResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoFornecedorResponseRepository extends JpaRepository<ProdutoFornecedorResponse, Long> {

    Optional<ProdutoFornecedorResponse> findById(Long id);

}

