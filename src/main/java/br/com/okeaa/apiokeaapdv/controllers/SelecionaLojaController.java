package br.com.okeaa.apiokeaapdv.controllers;

import br.com.okeaa.apiokeaapdv.controllers.response.selecionarLoja.SelecionaLoja;
import br.com.okeaa.apiokeaapdv.service.selecionarLoja.SelecionaLojaService;
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
@Api(value = "API REST SELECIONA LOJA")    //Swagger
@CrossOrigin(origins = "*")                 // Liberar os dominios da API
@Validated
public class SelecionaLojaController {

    @Autowired
    public SelecionaLojaService selecionaLojaService;

    @GetMapping("/selecionarLojas")
    public List<SelecionaLoja> getAllLojas() {
        return selecionaLojaService.getAllLojas();
    }

    @GetMapping("/selecionarLoja/{idLoja}")
    public ResponseEntity<SelecionaLoja> getLojaById(@PathVariable String idLoja) {
        Optional<SelecionaLoja> lojaOptional = selecionaLojaService.getLojaById(idLoja);
        if (lojaOptional.isPresent()) {
            return ResponseEntity.ok(lojaOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletarLoja/{idLoja}")
    public ResponseEntity<Void> deleteLojaById(@PathVariable String idLoja) {
        try {
            selecionaLojaService.deleteLojaById(idLoja);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/adicionarLoja", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveLoja(@RequestBody @Valid String selecionaLoja) {
        try {
            String savedLoja = selecionaLojaService.saveLoja(selecionaLoja).getBody();

            return ResponseEntity.ok(savedLoja);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(path = "/atualizarLoja/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Atualizar uma loja existente")
    public ResponseEntity<String> updateLoja(@RequestBody @Valid String selecionaLoja, @PathVariable String id) {
        try {
            String updatedLoja = selecionaLojaService.updateLoja(selecionaLoja, id).getBody();

            return ResponseEntity.ok(updatedLoja);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}