package br.com.okeaa.apiokeaapdv.controllers;

import br.com.okeaa.apiokeaapdv.controllers.response.controleCaixa.ControleCaixa;
import br.com.okeaa.apiokeaapdv.service.controleCaixa.ControleCaixaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")          //Padrão para os métodos /api/...
@Api(value = "API REST CONTROLE CAIXA")    //Swagger
@CrossOrigin(origins = "*")                 // Liberar os dominios da API
@Validated
public class ControleCaixaController {

    @Autowired
    public ControleCaixaService controleCaixaService;

    @GetMapping("/controleCaixas")
    public List<ControleCaixa> getAllControleCaixa() {
        return controleCaixaService.getAllControleCaixa();
    }

    @GetMapping("/controleCaixa/{id}")
    public ResponseEntity<ControleCaixa> getControleCaixaById(@PathVariable String id) {
        Optional<ControleCaixa> caixaOptional = controleCaixaService.getControleCaixaById(id);
        if (caixaOptional.isPresent()) {
            return ResponseEntity.ok(caixaOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletarControleCaixa/{id}")
    public ResponseEntity<Void> deleteControleCaixa(@PathVariable String id) {
        try {
            controleCaixaService.deleteControleCaixa(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/adicionarControleCaixa", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveControleCaixa(@RequestBody @Valid String controleCaixa) {
        try {
            String savedCaixa = controleCaixaService.saveControleCaixa(controleCaixa).getBody();
            return ResponseEntity.ok(savedCaixa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    // Adicione uma nova função na ControleCaixaService sem o parâmetro String
//    public ResponseEntity<String> saveControleCaixaWithoutXml(String loja, String vlr, String id) {
//        try {
//            return ResponseEntity.ok("Lista de preço salva com sucesso (sem XML).");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar a lista de preço: " + e.getMessage());
//        }
//    }

    @PutMapping(path = "/atualizarControleCaixa/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Atualizar um deposito existente")
    public ResponseEntity<String> updateControleCaixa(@RequestBody @Valid String controleCaixa, @PathVariable String id) {
        try {
            String request = controleCaixaService.updateControleCaixa(controleCaixa, id).getBody();

            return ResponseEntity.ok(request);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
