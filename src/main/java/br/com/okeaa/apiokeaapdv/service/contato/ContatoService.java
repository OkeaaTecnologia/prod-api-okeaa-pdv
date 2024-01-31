package br.com.okeaa.apiokeaapdv.service.contato;

import br.com.okeaa.apiokeaapdv.controllers.response.contato.JsonResponseContato;
import br.com.okeaa.apiokeaapdv.controllers.response.contato.TipoContatoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface ContatoService {

//    JsonResponseContato getAllContacts(int pagina);

    CompletableFuture<JsonResponseContato> getAllContacts(int pagina);

    JsonResponseContato getListContacts(String nome, String cnpj, String codigo);

    JsonResponseContato getContactsById(@PathVariable("cnpj") String cnpj);

    ResponseEntity<String> createContact(@RequestBody String xmlContato);

    ResponseEntity<String> updateContact(@RequestBody @Valid String xmlContato, @PathVariable("id") String id);

    List<TipoContatoResponse> getAllTiposContato();
}
