package br.com.okeaa.apiokeaapdv.service;

import br.com.okeaa.apiokeaapdv.service.formaPagamento.FormaPagamentoService;
import br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento.JsonRequestFormaPagamento;
import br.com.okeaa.apiokeaapdv.controllers.response.formaPagamento.JsonResponseFormaPagamento;
import br.com.okeaa.apiokeaapdv.exceptions.formaPagamento.ApiFormaPagamentoException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
public class FormaPagamentoServiceImpl implements FormaPagamentoService {

    @Value("${external.api.url}")
    public String apiBaseUrl;

    @Value("${external.api.apikey}")
    public String apiKey;

    @Value("${external.api.apikeyparam}")
    public String apikeyparam;

    @Autowired
    public RestTemplate restTemplate;


    /**
     * GET "BUSCAR A LISTA DE CATEGORIA CADASTRADOS NO BLING".
     * Método responsável por buscar a lista de produtos, tanto na API externa quanto no banco de dados local.
     *
     * @throws ApiFormaPagamentoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public JsonResponseFormaPagamento getAllFormaPagamento() throws ApiFormaPagamentoException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(headers);

            String url = apiBaseUrl + "/formaspagamento/json/" + apikeyparam + apiKey;
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonResponseFormaPagamento response = objectMapper.readValue(responseEntity.getBody(), JsonResponseFormaPagamento.class);

            return response;

        } catch (JsonProcessingException e) {
            throw new ApiFormaPagamentoException("Erro ao processar JSON", e);
        } catch (RestClientException e) {
            throw new ApiFormaPagamentoException("Erro ao chamar API", e);
        }
    }

    /**
     * GET "BUSCA CATEGORIA PELO IDCATEGORIA".
     * Método responsável por localizar uma categoria a partir do seu idCategoria, tanto na API externa quanto no banco de dados local.
     *
     * @param id idCategoria a ser localizado.
     * @throws ApiFormaPagamentoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public JsonResponseFormaPagamento getFormaPagamentoById(String id) throws ApiFormaPagamentoException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(id, headers);

            String url = apiBaseUrl + "/formapagamento/" + id + "/json/" + apikeyparam + apiKey;
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonResponseFormaPagamento response = objectMapper.readValue(responseEntity.getBody(), JsonResponseFormaPagamento.class);

            return response;

        } catch (JsonProcessingException e) {
            throw new ApiFormaPagamentoException("Erro ao processar JSON", e);
        } catch (RestClientException e) {
            throw new ApiFormaPagamentoException("Erro ao chamar API", e);
        }
    }

    /**
     * DELETE "DELETA UM PRODUTO PELO CODIGO (SKU)".
     *
     * @return
     */
    @Override
    public ResponseEntity<String> deleteFormaPagemento(String id) throws ApiFormaPagamentoException {
        ResponseEntity<String> response;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(headers);

            String url = apiBaseUrl + "/formapagamento/" + id + "/json/" + apikeyparam + apiKey;
            response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

        } catch (RestClientException e) {
            throw new ApiFormaPagamentoException("Erro ao chamar API", e);
        }
        return response;
    }

    /**
     * POST "CADASTRA UMA NOVA CATEGORIA UTILIZANDO XML".
     * Método responsável por cadastrar uma categoria, tanto na API externa quanto no banco de dados local.
     *
     * @param xmlFormaPagamento xml com os dados do cadastro da nova categoria.
     * @throws ApiFormaPagamentoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public JsonRequestFormaPagamento createFormaPagamento(String xmlFormaPagamento) throws ApiFormaPagamentoException {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("apikey", apiKey);
            map.add("xml", xmlFormaPagamento);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            String url  = apiBaseUrl + "/formapagamento/json/";
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonRequestFormaPagamento response = objectMapper.readValue(responseEntity.getBody(), JsonRequestFormaPagamento.class);

            return response;

        } catch (JsonProcessingException e) {
            throw new ApiFormaPagamentoException("Erro ao processar JSON", e);
        } catch (RestClientException e) {
            throw new ApiFormaPagamentoException("Erro ao chamar API", e);
        }
    }

    /**
     * PUT "ATUALIZA UMA CATEGORIA EXISTENTE UTILIZANDO XML".
     * Método responsável por atualizar uma categoria, tanto na API externa quanto no banco de dados local.
     *
     * @param xmlFormaPagamento xml com os dados do cadastro da nova categoria.
     * @param id  Id para acesso direto a categoria cadastrada.
     * @throws ApiFormaPagamentoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public JsonRequestFormaPagamento updateFormaPagamento(String xmlFormaPagamento, String id) throws ApiFormaPagamentoException {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("apikey", apiKey);
            map.add("xml", xmlFormaPagamento);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            String url = apiBaseUrl + "/formapagamento/" + id + "/json/";

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonRequestFormaPagamento response = objectMapper.readValue(responseEntity.getBody(), JsonRequestFormaPagamento.class);

            return response;

        } catch (JsonProcessingException e) {
            throw new ApiFormaPagamentoException("Erro ao processar JSON", e);
        } catch (RestClientException e) {
            throw new ApiFormaPagamentoException("Erro ao chamar API", e);
        }
    }
}

