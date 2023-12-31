package br.com.okeaa.apiokeaapdv.exceptions.contato;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApplicationContatoExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiContatoException.class)
    public ResponseEntity<String> handleApiContatoException(ApiContatoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ContatoListaException.class)
    public ResponseEntity<String> handleContatoListaExceptionException(ContatoListaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ContatoIdException.class)
    public ResponseEntity<String> handleContatoIdException(ContatoIdException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ContatoCadastroException.class)
    public ResponseEntity<String> handleContatoCadastroException(ContatoCadastroException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ContatoAtualizarException.class)
    public ResponseEntity<String> handleContatoAtualizarException(ContatoAtualizarException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
        return new ResponseEntity<>("Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}