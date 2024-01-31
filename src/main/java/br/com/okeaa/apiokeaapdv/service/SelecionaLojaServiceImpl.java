package br.com.okeaa.apiokeaapdv.service;

import br.com.okeaa.apiokeaapdv.repositories.selecionarLoja.SelecionaLojaRepository;
import br.com.okeaa.apiokeaapdv.service.selecionarLoja.SelecionaLojaService;
import br.com.okeaa.apiokeaapdv.controllers.response.selecionarLoja.SelecionaLoja;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
public class SelecionaLojaServiceImpl implements SelecionaLojaService {

    public final SelecionaLojaRepository selecionaLojaRepository;

    @Autowired
    public SelecionaLojaServiceImpl(SelecionaLojaRepository selecionaLojaRepository) {
        this.selecionaLojaRepository = selecionaLojaRepository;
    }

    @Override
    public List<SelecionaLoja> getAllLojas() {

        return selecionaLojaRepository.findAll();
    }

    @Override
    public Optional<SelecionaLoja> getLojaById(String idLoja) {
        Optional<SelecionaLoja> lojaOptional = selecionaLojaRepository.findByIdLoja(idLoja);
        if (lojaOptional.isPresent()) {
            return lojaOptional;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loja não encontrada com o ID: " + idLoja);
        }
    }

    @Override
    @Transactional
    public void deleteLojaById(String idLoja) {
        Optional<SelecionaLoja> lojaOptional = selecionaLojaRepository.findByIdLoja(idLoja);
        if (lojaOptional.isPresent()) {
            selecionaLojaRepository.deleteByIdLoja(idLoja);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loja não encontrada com o ID: " + idLoja);
        }
    }
    @Override
    @Transactional
    public ResponseEntity<String> saveLoja(String selecionaLoja) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SelecionaLoja selecionaLojaResponseObject = objectMapper.readValue(selecionaLoja, SelecionaLoja.class);

            // Insere um numero randomico no ID.
            selecionaLojaResponseObject.setId(generateShortId());
            // Salva a SelecionaLoja no banco de dados
            selecionaLojaRepository.save(selecionaLojaResponseObject);

            return ResponseEntity.ok("Lista de preço salva com sucesso.");
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Erro ao desserializar o JSON: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar a lista de preço: " + e.getMessage());
        }
    }

    private String generateShortId() {
        // Gera um número aleatório entre 0 e 999999
        int randomNum = (int) (Math.random() * 1000000);

        // Formata o número como uma string com zeros à esquerda, se necessário
        return String.format("%06d", randomNum);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateLoja(String selecionaLoja, String id) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SelecionaLoja selecionaLojaResponseObject = objectMapper.readValue(selecionaLoja, SelecionaLoja.class);

            // Verifica se já existe uma entidade com o ID fornecido
            Optional<SelecionaLoja> existingEntity = selecionaLojaRepository.findId(id);

            if (existingEntity.isPresent()) {
                SelecionaLoja existingLoja = existingEntity.get();

                // Atualiza os atributos da entidade com os valores do JSON
                existingLoja.setIdLoja(selecionaLojaResponseObject.getIdLoja());
                existingLoja.setNomeLoja(selecionaLojaResponseObject.getNomeLoja());
                existingLoja.setUnidadeLoja(selecionaLojaResponseObject.getUnidadeLoja());

                // Salva a entidade atualizada no banco de dados
                selecionaLojaRepository.save(existingLoja);

                return ResponseEntity.ok("Loja atualizada com sucesso.");
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Erro ao desserializar o JSON: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar a loja: " + e.getMessage());
        }
    }
}
