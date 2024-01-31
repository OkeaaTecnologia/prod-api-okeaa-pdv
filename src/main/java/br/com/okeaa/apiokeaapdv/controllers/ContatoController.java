package br.com.okeaa.apiokeaapdv.controllers;

import br.com.okeaa.apiokeaapdv.controllers.response.contato.JsonResponseContato;
import br.com.okeaa.apiokeaapdv.controllers.response.contato.TipoContatoResponse;
import br.com.okeaa.apiokeaapdv.exceptions.contato.*;
import br.com.okeaa.apiokeaapdv.service.contato.ContatoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping(value = "/api/v1")   //Padrão para os métodos /api/...
@Api(value = "API REST CONTATO")    //Swagger
@CrossOrigin(origins = "*")          // Liberar os dominios da API
public class ContatoController {

    public static final Logger logger = LoggerFactory.getLogger(ContatoController.class);

    @Autowired
    public ContatoService contatosService;

    /**
     * GET "BUSCAR LISTA DE CONTATOS".
     */
//    @GetMapping("/contatos/page={pagina}")
//    @ApiOperation(value = "Retorna uma lista de contatos")
//    public ResponseEntity<JsonResponseContato> getAllContacts(@PathVariable int pagina) {
//        try {
//            JsonResponseContato request = contatosService.getAllContacts(pagina);
//
//            logger.info("GET: " + request);
//
//            return ResponseEntity.status(HttpStatus.OK).body(request);
//        } catch (Exception e) {
//            throw new ApiContatoException("Houve algum erro sistêmico, tente novamente", e);
//        }
//    }

    @GetMapping("/contatos/page={pagina}")
    @ApiOperation(value = "Retorna uma lista de contatos")
    public CompletableFuture<JsonResponseContato> getAllContacts(@PathVariable int pagina) {
        try {
            CompletableFuture<JsonResponseContato> request = contatosService.getAllContacts(pagina);

            return ResponseEntity.status(HttpStatus.OK).body(request).getBody();

        } catch (Exception e) {
            throw new ApiContatoException("Houve algum erro sistêmico, tente novamente", e);
        }
    }

    @GetMapping("/contatos")
    @ApiOperation(value = "Retorna uma lista de contatos")
    public ResponseEntity<JsonResponseContato> getListContacts(@RequestParam(required = false) String nome,
                                                               @RequestParam(required = false) String cnpj,
                                                               @RequestParam(required = false) String codigo
    ) {
        try {
            JsonResponseContato request = contatosService.getListContacts(nome, cnpj, codigo);

            return ResponseEntity.status(HttpStatus.OK).body(request);

        } catch (Exception e) {
            throw new ApiContatoException("Houve algum erro sistêmico, tente novamente", e);
        }
    }

    /**
     * GET "BUSCAR UM CONTATO PELO CPF ou CNPJ".
     */
    @GetMapping("/contato/{id}")
    @ApiOperation(value = "Retorna um contato pelo CPF ou CNPJ")
    public ResponseEntity<JsonResponseContato> getContactsById(@PathVariable String id) {
        try {
            JsonResponseContato request = contatosService.getContactsById(id);

            return ResponseEntity.status(HttpStatus.OK).body(request);

        } catch (Exception e) {
            throw new ApiContatoException("Houve algum erro sistemico, tente novamente", e);
        }
    }

    /**
     * POST "CADASTRAR UM NOVO CONTATO UTILIZANDO XML".
     */
    @PostMapping(path = "/cadastrarcontato", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Cadastrar um novo contato")
    public ResponseEntity<String> createContact(@RequestBody String xml) {
        try {
            String request = contatosService.createContact(xml).getBody();

            logger.info("POST: " + request);

            return ResponseEntity.ok(request);

        } catch (Exception e) {
            throw new ApiContatoException("Houve algum erro sistemico, tente novamente", e);
        }
    }

    /**
     * PUT "ATUALIZAR CONTATO PELO CPF e CNPJ UTILIZANDO XML".
     */
    @PutMapping(path = "/atualizarcontato/{id}", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Atualizar um contato existente")
    public ResponseEntity<String> updateContact(@RequestBody @Valid String xmlContato, @PathVariable("id") String id) {
        try {
            String request = contatosService.updateContact(xmlContato, id).getBody();

            logger.info("UPDATE: " + request);

            return ResponseEntity.ok(request);

        } catch (Exception e) {
            throw new ApiContatoException("Houve algum erro sistemico, tente novamente", e);
        }
    }

    @GetMapping("/selecionartipocontato")
    @ApiOperation(value = "Retorna os Tipos de Contato")
    public List<TipoContatoResponse> getAllLojas() {
        return contatosService.getAllTiposContato();
    }
}