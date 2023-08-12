package br.com.okeaa.apiokeaapdv.controllers;


import br.com.okeaa.apiokeaapdv.controllers.request.pedido.JsonRequestPedido;
import br.com.okeaa.apiokeaapdv.controllers.response.pedido.JsonResponsePedido;
import br.com.okeaa.apiokeaapdv.exceptions.pedido.ApiPedidoException;
import br.com.okeaa.apiokeaapdv.service.pedido.PedidoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")        //Padrão para os métodos /api/...
@Api(value = "API REST PEDIDOS")    //Swagger
@CrossOrigin(origins = "*")        // Liberar os dominios da API
@Validated
public class PedidoController {

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

            System.out.println("GET: " + request);

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

            System.out.println("GET ID: " + request);

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
    public ResponseEntity<JsonRequestPedido> createCategory(@RequestBody @Valid String xmlPedido) {
        try {
            JsonRequestPedido request = pedidoService.createPedido(xmlPedido);

            System.out.println("POST: " + request);

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
    public ResponseEntity<JsonRequestPedido> updatePedido(@RequestBody String xmlPedido, @PathVariable("numero") String numero) {
        try {
            JsonRequestPedido request = pedidoService.updatePedido(xmlPedido, numero);

            return ResponseEntity.ok(request);

        } catch (Exception e) {
            throw new ApiPedidoException("Houve algum erro sistemico, tente novamente", e);
        }
    }
}