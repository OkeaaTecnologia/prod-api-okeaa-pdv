package br.com.okeaa.apiokeaaproduto.repositories.produtofornecedor;

import br.com.okeaa.apiokeaaproduto.controllers.response.produtofornecedor.ProdutoFornecedoresResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoFornecedoresResponseRepository extends JpaRepository<ProdutoFornecedoresResponse, Long> {
    Optional<ProdutoFornecedoresResponse> findById(Long id);
}

