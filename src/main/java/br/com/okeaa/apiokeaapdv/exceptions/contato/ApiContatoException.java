package br.com.okeaa.apiokeaapdv.exceptions.contato;

public class ApiContatoException extends RuntimeException {
    public ApiContatoException(String message, Throwable cause) {
        super(message, cause);
    }
}

