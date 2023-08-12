package br.com.okeaa.apiokeaaproduto.exceptions.produto;

public class ApiProdutoException extends RuntimeException {
    public ApiProdutoException(String message, Throwable cause) {
        super(message, cause);
    }
}