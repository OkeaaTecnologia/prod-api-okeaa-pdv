package br.com.okeaa.apiokeaapdv.controllers;

import br.com.okeaa.apiokeaapdv.controllers.response.pedido.JsonResponsePedido;
import br.com.okeaa.apiokeaapdv.exceptions.pedido.ApiPedidoException;
import br.com.okeaa.apiokeaapdv.service.pedido.PedidoService;
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
@RequestMapping(value = "/api/v1")        //Padrão para os métodos /api/...
@Api(value = "API REST PEDIDO")    //Swagger
@CrossOrigin(origins = "*")        // Liberar os dominios da API
@Validated
public class PedidoController {

    public static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    public PedidoService pedidoService;

        /**
     * GET "BUSCA A LISTA DE CATEGORIAS".
     */
    @GetMapping("/pedidos")
    @ApiOperation(value = "Retorna uma lista de categorias")
    public ResponseEntity<JsonResponsePedido> getAllCategory() {
        try {
            JsonResponsePedido request = pedidoService.getAllPedido();

            logger.info("GET: " + request);

            return ResponseEntity.ok(request);

        } catch (Exception e) {
            throw new ApiPedidoException("Houve algum erro sistemico, tente novamente", e);
        }
    }

    /**
     * GET "BUSCA CATEGORIA PELO IDCATEGORIA".
     */
    @GetMapping("/pedido/{numero}")
    @ApiOperation(value = "Retorna uma categoria pelo numero")
    public ResponseEntity<JsonResponsePedido> getCategoryByIdCategory(@PathVariable("numero") String numero) {
        try {
            JsonResponsePedido request = pedidoService.getPedidoByIdPedido(numero);

            logger.info("GET ID: " + request);

            return ResponseEntity.ok(request);

        } catch (Exception e) {
            throw new ApiPedidoException("Houve algum erro sistemico, tente novamente", e);
        }
    }

    /**
     * POST "CADASTRA UMA NOVA CATEGORIA UTILIZANDO XML".
     */
    @PostMapping(path = "/cadastrarpedido", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Cadastrar um pedido")
    public ResponseEntity<String> createCategory(@RequestBody @Valid String xmlPedido) {
        try {
            String request = pedidoService.createPedido(xmlPedido).getBody();

            logger.info("POST: " + request);

            return ResponseEntity.ok(request);

        } catch (Exception e) {
            throw new ApiPedidoException("Houve algum erro sistemico, tente novamente", e);
        }
    }

    /**
     * PUT "ATUALIZA UMA CATEGORIA EXISTENTE UTILIZANDO XML".
     */
    @PutMapping(path = "/atualizarpedido/{numero}", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Atualiza um pedido")
    public ResponseEntity<String> updatePedido(@RequestBody String xmlPedido, @PathVariable("numero") String numero) {
        try {
            String request = pedidoService.updatePedido(xmlPedido, numero).getBody();

            logger.info("UPDATE: " + request);

            return ResponseEntity.ok(request);

        } catch (Exception e) {
            throw new ApiPedidoException("Houve algum erro sistemico, tente novamente", e);
        }
    }
}