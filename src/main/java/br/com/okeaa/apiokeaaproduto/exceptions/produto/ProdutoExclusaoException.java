package br.com.okeaa.apiokeaaproduto.exceptions.produto;

public class ProdutoExclusaoException extends RuntimeException {
    public ProdutoExclusaoException(String message) {
        super(message);
    }
}
