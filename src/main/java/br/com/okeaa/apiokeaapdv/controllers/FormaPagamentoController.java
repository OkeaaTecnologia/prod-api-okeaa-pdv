package br.com.okeaa.apiokeaapdv.controllers;


import br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento.JsonRequestFormaPagamento;
import br.com.okeaa.apiokeaapdv.controllers.response.formaPagamento.JsonResponseFormaPagamento;
import br.com.okeaa.apiokeaapdv.exceptions.formaPagamento.ApiFormaPagamentoException;
import br.com.okeaa.apiokeaapdv.service.formaPagamento.FormaPagamentoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")          //Padrão para os métodos /api/...
@Api(value = "API REST FORMA PAGAMENTO")    //Swagger
@CrossOrigin(origins = "*")                 // Liberar os dominios da API
@Validated
public class FormaPagamentoController {

    public static final Logger logger = LoggerFactory.getLogger(FormaPagamentoController.class);

    @Autowired
    public FormaPagamentoService formaPagamentoService;

    /**
     * GET "BUSCA A LISTA DE CATEGORIAS".
     */
    @GetMapping("/formaspagamento")
    @ApiOperation(value = "Retorna uma lista de formas de pagamento")
    public ResponseEntity<JsonResponseFormaPagamento> getAllFormaPagamento() {
        try {
            JsonResponseFormaPagamento request = formaPagamentoService.getAllFormaPagamento();

            logger.info("GET: " + request);

            return ResponseEntity.ok(request);

        } catch (Exception e) {
            throw new ApiFormaPagamentoException("Houve algum erro sistemico, tente novamente", e);
        }
    }

    /**
     * GET "BUSCA CATEGORIA PELO IDCATEGORIA".
     */
    @GetMapping("/formapagamento/{id}")
    @ApiOperation(value = "Retorna uma forma de pagamento pelo ID")
    public ResponseEntity<JsonResponseFormaPagamento> getFormaPagamentoById(@PathVariable("id") String id) {
        try {
            JsonResponseFormaPagamento request = formaPagamentoService.getFormaPagamentoById(id);

            logger.info("GET ID: " + request);

            return ResponseEntity.ok(request);

        } catch (Exception e) {
            throw new ApiFormaPagamentoException("Houve algum erro sistemico, tente novamente", e);
        }
    }

    /**
     * DELETE PROUTO PELO CÓDIGO (SKU).
     */
    @DeleteMapping("/formapagamento/{id}")
    @ApiOperation(value = "Deleta uma forma de pagamento pelo ID")
    public ResponseEntity<String> deleteProductByCode(@PathVariable String id) {
        try {
            ResponseEntity<String> request = formaPagamentoService.deleteFormaPagemento(id);

            logger.info("Codigo deletado = " + id);

            return request;

        } catch (Exception e) {
            throw new ApiFormaPagamentoException("Houve algum erro sistemico, tente novamente", e);
        }
    }

    /**
     * POST "CADASTRA UMA NOVA CATEGORIA UTILIZANDO XML".
     */
    @PostMapping(path = "/cadastrarformapagamento", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Cadastra uma forma de pagamento")
    public ResponseEntity<String> createFormaPagamento(@RequestBody @Valid String xmlFormaPagamento) {
        try {
            String request = formaPagamentoService.createFormaPagamento(xmlFormaPagamento).getBody();

            logger.info("POST: " + request);

            return ResponseEntity.ok(request);

        } catch (Exception e) {
            throw new ApiFormaPagamentoException("Houve algum erro sistemico, tente novamente", e);
        }
    }

    /**
     * PUT "ATUALIZA UMA CATEGORIA EXISTENTE UTILIZANDO XML".
     */
    @PutMapping(path = "/atualizarformapagamento/{id}", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Atualiza um pedido")
    public ResponseEntity<String> updateFormaPagamento(@RequestBody String xmlFormaPagamento, @PathVariable("id") String id) {
        try {
            String request = formaPagamentoService.updateFormaPagamento(xmlFormaPagamento, id).getBody();

            logger.info("PUT: " + request);

            return ResponseEntity.ok(request);

        } catch (Exception e) {
            throw new ApiFormaPagamentoException("Houve algum erro sistemico, tente novamente", e);
        }
    }
}