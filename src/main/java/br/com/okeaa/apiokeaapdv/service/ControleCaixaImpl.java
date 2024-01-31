package br.com.okeaa.apiokeaapdv.service;

import br.com.okeaa.apiokeaapdv.controllers.ControleCaixaController;
import br.com.okeaa.apiokeaapdv.controllers.request.pedido.ParcelaRequest;
import br.com.okeaa.apiokeaapdv.controllers.response.controleCaixa.ControleCaixa;
import br.com.okeaa.apiokeaapdv.controllers.response.controleCaixa.FormaPagamentoCaixa;
import br.com.okeaa.apiokeaapdv.controllers.response.controleCaixa.TotalInformadoCaixa;
import br.com.okeaa.apiokeaapdv.repositories.controleCaixa.ControleCaixaRepository;
import br.com.okeaa.apiokeaapdv.repositories.controleCaixa.FormaPagamentoCaixaRepository;
import br.com.okeaa.apiokeaapdv.repositories.controleCaixa.TotalInformadoCaixaRepository;
import br.com.okeaa.apiokeaapdv.service.controleCaixa.ControleCaixaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ControleCaixaImpl implements ControleCaixaService {

    public static final Logger logger = LoggerFactory.getLogger(ControleCaixaController.class);

    @Autowired
    public ControleCaixaRepository controleCaixaRepository;

    @Autowired
    public FormaPagamentoCaixaRepository formaPagamentoCaixaRepository;

    @Autowired
    public TotalInformadoCaixaRepository totalInformadoCaixaRepository;


    @Override
    public List<ControleCaixa> getAllControleCaixa() {
        return controleCaixaRepository.findAll();
    }

    @Override
    public Optional<ControleCaixa> getControleCaixaById(String id) {
        Optional<ControleCaixa> caixaOptional = controleCaixaRepository.findDistinctControleCaixaById(id);
        if (caixaOptional.isPresent()) {
            return caixaOptional;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loja não encontrada com o ID: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteControleCaixa(String id) {
        Optional<ControleCaixa> caixaOptional = controleCaixaRepository.findById(id);
        if (caixaOptional.isPresent()) {
            controleCaixaRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loja não encontrada com o ID: " + id);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> saveControleCaixa(String controleCaixa) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ControleCaixa selecionaLojaResponseObject = objectMapper.readValue(controleCaixa, ControleCaixa.class);

            // Obter o troco_caixa da última linha da tabela
            String trocoCaixaAnterior = controleCaixaRepository.findFirstByOrderByDataAberturaDesc()
                    .map(ControleCaixa::getTrocoCaixa)
                    .orElse(null);

            selecionaLojaResponseObject.setId(generateShortId());
            selecionaLojaResponseObject.setSituacao("Aberto");
            selecionaLojaResponseObject.setTrocoCaixaAnterior(trocoCaixaAnterior);
            selecionaLojaResponseObject.setOperadorAbertura("-");
            selecionaLojaResponseObject.setOperadorFechamento("-");
            selecionaLojaResponseObject.setDataFechamento("-");

            controleCaixaRepository.save(selecionaLojaResponseObject);

            return ResponseEntity.ok("Lista de preço salva com sucesso.");
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Erro ao desserializar o JSON: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar a lista de preço: " + e.getMessage());
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
    public ResponseEntity<String> saveControleCaixaOP(String loja, List<ParcelaRequest> parcelaList) {
        try {
            // Verifica se existe um ControleCaixa com idLoja igual a loja e situacao igual a "ABERTO"
            Optional<ControleCaixa> existingItem = controleCaixaRepository.findByIdLojaAndSituacao(loja, "Aberto");

            if (existingItem.isPresent()) {
                // O ControleCaixa já existe, você pode continuar com a lógica
                ControleCaixa controleCaixa = existingItem.get();

                for (ParcelaRequest parcela : parcelaList) {
                    FormaPagamentoCaixa formaPagamentoCaixa = new FormaPagamentoCaixa();
                    formaPagamentoCaixa.setId(Long.valueOf(parcela.getForma_pagamento().getId()));
                    formaPagamentoCaixa.setValorFaturado(parcela.getVlr().toString());
                    formaPagamentoCaixa.setDescricao(parcela.getForma_pagamento().getDescricao().toString());

                    // Associa a FormaPagamentoCaixa ao ControleCaixa
                    formaPagamentoCaixa.setFormaPagamentoCaixa(controleCaixa);

                    // Adiciona a FormaPagamentoCaixa à lista
                    controleCaixa.getFormaPagamentoCaixa().add(formaPagamentoCaixa);
                }

                // Salva o ControleCaixa com as associações
                controleCaixaRepository.save(controleCaixa);
            }

            return ResponseEntity.ok("Lista de preço salva com sucesso.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar a lista de preço: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateControleCaixa(String controleCaixa, String id) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ControleCaixa controleCaixaObject = objectMapper.readValue(controleCaixa, ControleCaixa.class);

            // Verifica se a entidade com o ID fornecido existe no banco de dados
            Optional<ControleCaixa> existingEntity = controleCaixaRepository.findById(id);
            if (existingEntity.isPresent()) {
                // Obtém a entidade existente
                ControleCaixa existingControleCaixa = existingEntity.get();

                // Atualiza os atributos da entidade existente com os valores do JSON
                existingControleCaixa.setSituacao("Fechado");
                existingControleCaixa.setOperadorFechamento(controleCaixaObject.getOperadorFechamento());
                existingControleCaixa.setDataFechamento(controleCaixaObject.getDataFechamento());

                // Atualiza a associação com os produtos (TotalInformadoCaixa)
                List<TotalInformadoCaixa> produtos = new ArrayList<>();
                for (TotalInformadoCaixa produto : controleCaixaObject.getTotalInformadoCaixa()) {
                    // Verifica se o produto já existe no banco de dados
//                    if (!totalInformadoCaixaRepository.existsByIdPagamentoAndDescricaoPagamento(produto.getIdPagamento(), produto.getDescricaoPagamento())) {
                        // Configura a associação bidirecional
                        produto.setTotalInformadoCaixa(existingControleCaixa);

                        // Define os valores dos campos descricao, totalInformado e totalDiferenca
                        produto.setDescricaoPagamento(produto.getDescricaoPagamento());
                        produto.setIdPagamento(produto.getIdPagamento());
                        produto.setTotalInformado(produto.getTotalInformado());
                        produto.setTotalDiferenca(produto.getTotalDiferenca());

                        produtos.add(produto);
//                    }
                }

                // Define a lista de produtos para a entidade existente
                existingControleCaixa.setTotalInformadoCaixa(produtos);

                // Salva a entidade existente com os produtos associados
                controleCaixaRepository.save(existingControleCaixa);


                // Salva os produtos individualmente
                totalInformadoCaixaRepository.saveAll(produtos);

                return ResponseEntity.ok("Controle de caixa atualizado com sucesso.");

            } else {
                return ResponseEntity.ok().body("Controle de caixa não encontrado para atualização.");
            }
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Erro ao desserializar o JSON: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar o controle de caixa: " + e.getMessage());
        }
    }


}
