package br.com.okeaa.apiokeaapdv.service.contato;

import br.com.okeaa.apiokeaapdv.controllers.request.contato.JsonRequestContato;
import br.com.okeaa.apiokeaapdv.controllers.response.contato.JsonResponseContato;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface ContatoService {

    JsonResponseContato getAllContacts();

    JsonResponseContato getContactsById(@PathVariable("cnpj") String cnpj);

    JsonRequestContato createContact(@RequestBody String xmlContato);

    JsonRequestContato updateContact(@RequestBody @Valid String xmlContato, @PathVariable("id") String id);
}
