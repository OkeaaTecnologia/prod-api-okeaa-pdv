package br.com.okeaa.apiokeaapdv.service;

import br.com.okeaa.apiokeaapdv.controllers.FormaPagamentoController;
import br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento.DadosCartaoRequest;
import br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento.DadosTaxasRequest;
import br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento.FormaPagamentoRequest;
import br.com.okeaa.apiokeaapdv.controllers.response.formaPagamento.FormaPagamentoResponse;
import br.com.okeaa.apiokeaapdv.controllers.response.formaPagamento.JsonResponseFormaPagamento;
import br.com.okeaa.apiokeaapdv.controllers.response.formaPagamento.RetornoResponseFormaPagamento;
import br.com.okeaa.apiokeaapdv.exceptions.formaPagamento.ApiFormaPagamentoException;
import br.com.okeaa.apiokeaapdv.repositories.formaPagamento.DadosCartaoRequestRepository;
import br.com.okeaa.apiokeaapdv.repositories.formaPagamento.DadosTaxasRequestRepository;
import br.com.okeaa.apiokeaapdv.repositories.formaPagamento.FormaPagamentoRequestRepository;
import br.com.okeaa.apiokeaapdv.repositories.formaPagamento.FormaPagamentoResponseRepository;
import br.com.okeaa.apiokeaapdv.service.formaPagamento.FormaPagamentoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class FormaPagamentoServiceImpl implements FormaPagamentoService {

    public static final Logger logger = LoggerFactory.getLogger(FormaPagamentoController.class);

    @Value("${external.api.url}")
    public String apiBaseUrl;

    @Value("${external.api.apikey}")
    public String apiKey;

    @Value("${external.api.apikeyparam}")
    public String apikeyparam;

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public FormaPagamentoResponseRepository formaPagamentoResponseRepository;

    @Autowired
    public FormaPagamentoRequestRepository formaPagamentoRequestRepository;

    @Autowired
    public DadosCartaoRequestRepository dadosCartaoRequestRepository;

    @Autowired
    public DadosTaxasRequestRepository dadosTaxasRequestRepository;

    /**
     * GET "BUSCAR A LISTA DE FORMAS DE PAGAMENTO CADASTRADOS NO BLING".
     * Método responsável por buscar a lista de formas de pagamento, tanto na API externa quanto no banco de dados local.
     *
     * @throws ApiFormaPagamentoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public JsonResponseFormaPagamento getAllFormaPagamento() throws ApiFormaPagamentoException {
        try {
            /* TESTE BANCO DE DADOS, DESCOMENTAR LINHA ABAIXO */
//            String url = "http://www.teste.com/";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(headers);

            String url = apiBaseUrl + "/formaspagamento/json/" + apikeyparam + apiKey;
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonResponseFormaPagamento response = objectMapper.readValue(responseEntity.getBody(), JsonResponseFormaPagamento.class);

            // Cria uma lista de Produtos com os valores da API Bling
//            List<FormaPagamentoResponse> formaspagamento = new ArrayList<>();
//            for (RetornoResponseFormaPagamento.Formaspagamento formaPagamento : response.getRetorno().getFormaspagamento()) {
//                formaspagamento.add(formaPagamento.getFormaPagamentoResponse());
//            }

            List<FormaPagamentoResponse> formaspagamento = new ArrayList<>();
            if (response.getRetorno() != null && response.getRetorno().getFormaspagamento() != null) {
                for (RetornoResponseFormaPagamento.Formaspagamento formaPagamento : response.getRetorno().getFormaspagamento()) {
                    formaspagamento.add(formaPagamento.getFormaPagamentoResponse());
                }
            }

            // Cria uma lista de Produtos de resposta para enviar de volta
            ArrayList<RetornoResponseFormaPagamento.Formaspagamento> formaspagamentoResponse = new ArrayList<>();
            // Percorre todos os produtos da lista
            for (FormaPagamentoResponse formaPagamento : formaspagamento) {
                // Verifica se o produto existe no banco de dados
                Optional<FormaPagamentoResponse> formaPagamentoExistente = formaPagamentoResponseRepository.findById(Long.valueOf(formaPagamento.getId()));
                if (formaPagamentoExistente.isPresent()) {
                    // Se o produto já existir, atualiza seus campos.
                    FormaPagamentoResponse formaPagamentoAtualizado = formaPagamentoExistente.get();
                    formaPagamentoAtualizado.setId(formaPagamento.getId());
                    formaPagamentoAtualizado.setCodigoFiscal(formaPagamento.getCodigoFiscal());
                    formaPagamentoAtualizado.setDescricao(formaPagamento.getDescricao());
                    formaPagamentoAtualizado.setFixa(formaPagamento.getFixa());
                    formaPagamentoAtualizado.setPadrao(formaPagamento.getPadrao());
                    formaPagamentoAtualizado.setSituacao(formaPagamento.getSituacao());

                    formaPagamentoResponseRepository.save(formaPagamentoAtualizado);

                    // Adiciona o produto de resposta à lista de produtos de resposta
                    RetornoResponseFormaPagamento.Formaspagamento formaPagamentoResponse = new RetornoResponseFormaPagamento.Formaspagamento();
                    formaPagamentoResponse.setFormaPagamentoResponse(formaPagamentoAtualizado);
                    formaspagamentoResponse.add(formaPagamentoResponse);
                }
                formaPagamentoResponseRepository.save(formaPagamento);
            }
            // Cria o objeto de resposta final
            RetornoResponseFormaPagamento retornoResponseFormaPagamento = new RetornoResponseFormaPagamento();
            retornoResponseFormaPagamento.setFormaspagamento(formaspagamentoResponse);

            JsonResponseFormaPagamento jsonRetornoFormaPagamento = new JsonResponseFormaPagamento();
            jsonRetornoFormaPagamento.setRetorno(retornoResponseFormaPagamento);

            // Retorna a resposta final em formato JSON
            return response;

        } catch (JsonProcessingException e) {
            throw new ApiFormaPagamentoException("Erro ao processar JSON: ", e);
        } catch (RestClientException e) {
            try {
                // Busca todos os produtos salvos no banco de dados
                List<FormaPagamentoResponse> formasPagamento = formaPagamentoResponseRepository.findAll();

                // Verifica se a lista de produtos está vazia, ou seja, se não há nenhum produto cadastrado no banco de dados
                if (formasPagamento.isEmpty()) {
                    throw new ApiFormaPagamentoException("Banco de dados está vazio: ", e);
                } else {
                    // Cria uma lista de produtos de resposta para enviar de volta
                    ArrayList<RetornoResponseFormaPagamento.Formaspagamento> formasPagamentoResponse = new ArrayList<>();

                    // Para cada produto salvo no banco de dados, carrega a categoria associada e cria um novo objeto RetornoResponse.Produtos com o produto e a categoria correspondentes
                    for (FormaPagamentoResponse formaPagamento : formasPagamento) {
                        RetornoResponseFormaPagamento.Formaspagamento formaPagamentoResponse = new RetornoResponseFormaPagamento.Formaspagamento();
                        formaPagamentoResponse.setFormaPagamentoResponse(formaPagamento);
                        formasPagamentoResponse.add(formaPagamentoResponse);
                    }

                    // Define a lista de produtos do retorno e cria um objeto JsonResponse com esse retorno
                    RetornoResponseFormaPagamento retornoResponseFormaPagamento = new RetornoResponseFormaPagamento();
                    retornoResponseFormaPagamento.setFormaspagamento(formasPagamentoResponse);
                    JsonResponseFormaPagamento jsonResponseFormaPagamento = new JsonResponseFormaPagamento();
                    jsonResponseFormaPagamento.setRetorno(retornoResponseFormaPagamento);

                    // Retorna o objeto JsonResponse
                    return jsonResponseFormaPagamento;
                }
            } catch (Exception ex) {
                // Caso ocorra algum erro ao buscar os produtos do banco de dados
                throw new ApiFormaPagamentoException("Erro ao buscar produtos do banco de dados: ", ex);
            }
        }
    }

    /**
     * GET "BUSCA O FORMA DE PAGAMENTO PELO ID".
     * Método responsável por localizar uma forma de pagamento a partir do seu id, tanto na API externa quanto no banco de dados local.
     *
     * @param id id da forma de pagamento a ser localizado.
     * @throws ApiFormaPagamentoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public JsonResponseFormaPagamento getFormaPagamentoById(String id) throws ApiFormaPagamentoException {
        try {
            /* TESTE BANCO DE DADOS, DESCOMENTAR LINHA ABAIXO */
//            String url = "http://www.teste.com/";

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
            // Busca o produto com o código informado no banco de dados
            Optional<FormaPagamentoResponse> formaPagamentoExistente = formaPagamentoResponseRepository.findById(Long.valueOf(id));
            // Se o produto existir no banco de dados, cria um objeto RetornoResponse.Produtos com o produto encontrado e retorna como resposta
            if (formaPagamentoExistente.isPresent()) {
                RetornoResponseFormaPagamento.Formaspagamento formaPagamento = new RetornoResponseFormaPagamento.Formaspagamento();
                formaPagamento.setFormaPagamentoResponse(formaPagamentoExistente.get());

                JsonResponseFormaPagamento jsonResponseFormaPagamento = new JsonResponseFormaPagamento();
                jsonResponseFormaPagamento.setRetorno(new RetornoResponseFormaPagamento());
                jsonResponseFormaPagamento.getRetorno().setFormaspagamento(new ArrayList<>());
                jsonResponseFormaPagamento.getRetorno().getFormaspagamento().add(formaPagamento);

                return jsonResponseFormaPagamento;

            } else {
                throw new ApiFormaPagamentoException("A API está indisponível e os produtos não foram encontrados no banco de dados.", e);
            }
        }
    }

    /**
     * DELETE "APAGA UMA FORMA DE PAGAMENTO PELO ID".
     * Método responsável por deletar uma forma de pagamento a partir do seu ID, tanto na API externa quanto no banco de dados local.
     *
     * @param id  Id para acesso direto a forma de pagamento cadastrada.
     * @throws ApiFormaPagamentoException Caso ocorra algum erro na comunicação com a API externa ou na exclusão do produto no banco de dados local.
     */
    @Override
    public ResponseEntity<String> deleteFormaPagemento(String id) throws ApiFormaPagamentoException {
        try {
            /* TESTE BANCO DE DADOS, DESCOMENTAR LINHA ABAIXO */
//            String url = "http://www.teste.com/";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(headers);

            String url = apiBaseUrl + "/formapagamento/" + id + "/json/" + apikeyparam + apiKey;
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

            // Verifica se a requisição na API externa foi bem sucedida
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // Se sim, busca o produto no banco de dados local a partir do seu código
                Optional<FormaPagamentoResponse> formaPagamentoExistente = formaPagamentoResponseRepository.findById(Long.valueOf(id));
                if (formaPagamentoExistente.isPresent()) {
                    // Se o produto existe no banco de dados local, exclui-o
                    formaPagamentoResponseRepository.delete(formaPagamentoExistente.get());
                }
            }

            return responseEntity;

        } catch (RestClientException e) {
            Optional<FormaPagamentoRequest> produtoExistenteRequest = formaPagamentoRequestRepository.findById(Long.valueOf(id));
            boolean produtoExiste = produtoExistenteRequest.isEmpty();

            if (produtoExiste) {
                logger.info("Produto não encontrado no Banco de dados, cadastrando... [DELETE]");

                // Mapeamento para ProdutoRequest
                FormaPagamentoRequest formaPagamentoRequest = new FormaPagamentoRequest();
                formaPagamentoRequest.setId(Long.valueOf(id));
                formaPagamentoRequest.setFlag("DELETE");

                formaPagamentoRequestRepository.save(formaPagamentoRequest);
            }

            // Caso ocorra algum erro na comunicação com a API externa, lança uma exceção informando o erro
            Optional<FormaPagamentoResponse> formaPagamentoExistenteResponse = formaPagamentoResponseRepository.findById(Long.valueOf(id));
            if (formaPagamentoExistenteResponse.isPresent()) {
                // Se o produto existe no banco de dados local, exclui-o
                formaPagamentoResponseRepository.delete(formaPagamentoExistenteResponse.get());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(""); // Retorna um ResponseEntity vazio com status 200 (OK)
    }

    /**
     * POST "CADASTRA UMA NOVA FORMA DE PAGAMENTO UTILIZANDO XML".
     * Método responsável por cadastrar uma forma de pagamento, tanto na API externa quanto no banco de dados local.
     *
     * @param xmlFormaPagamento xml com os dados do cadastro da nova forma de pagamento.
     * @throws ApiFormaPagamentoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public ResponseEntity<String> createFormaPagamento(String xmlFormaPagamento) throws
            ApiFormaPagamentoException {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("apikey", apiKey);
            map.add("xml", xmlFormaPagamento);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            String url = apiBaseUrl + "/formapagamento/json/";
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonRequestFormaPagamento response = objectMapper.readValue(responseEntity.getBody(), JsonRequestFormaPagamento.class);

            return responseEntity;

//        } catch (JsonProcessingException e) {
//            throw new ApiFormaPagamentoException("Erro ao processar JSON", e);
        } catch (RestClientException e) {
            // Em caso de erro ao chamar a API, salva os dados no banco de dados
            logger.info("API Bling Produto [POST] indisponível");
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(xmlFormaPagamento));
                Document doc = builder.parse(is);

                // Verifica se o produto existe no banco de dados
                String descricaoProdutoExistente = doc.getElementsByTagName("descricao").item(0).getTextContent();
                List<FormaPagamentoRequest> formaPagamentoExistente = formaPagamentoRequestRepository.findByDescricao(descricaoProdutoExistente);
                boolean formaPagamentoExiste = !formaPagamentoExistente.isEmpty();

                if (!formaPagamentoExiste) {
                    logger.info("Produto não encontrado no Banco de dados, cadastrando... [POST]");

                    // Mapeamento para ProdutoRequest
                    FormaPagamentoRequest formaPagamentoRequest = new FormaPagamentoRequest();
                    formaPagamentoRequest.setId(Long.valueOf("0"));
                    formaPagamentoRequest.setDescricao(getTextContent(doc, "descricao"));
                    formaPagamentoRequest.setCodigoFiscal(getBigDecimalContent(doc, "codigofiscal"));
                    formaPagamentoRequest.setCondicao(getTextContent(doc, "condicao"));
                    formaPagamentoRequest.setDestino(getBigDecimalContent(doc, "destino"));
                    formaPagamentoRequest.setPadrao(getBigDecimalContent(doc, "padrao"));
                    formaPagamentoRequest.setSituacao(getBigDecimalContent(doc, "situacao"));

                    DadosCartaoRequest dadosCartaoRequest = new DadosCartaoRequest();
                    dadosCartaoRequest.setBandeira(getBigDecimalContent(doc, "codigofiscal"));
                    dadosCartaoRequest.setTipointegracao(getBigDecimalContent(doc, "tipointegracao"));
                    dadosCartaoRequest.setCnpjcredenciadora(getTextContent(doc, "cnpjcredenciadora"));
                    dadosCartaoRequest.setAutoliquidacao(getBigDecimalContent(doc, "autoliquidacao"));
                    dadosCartaoRequestRepository.save(dadosCartaoRequest);
                    formaPagamentoRequest.setDadoscartao(dadosCartaoRequest);

                    DadosTaxasRequest dadosTaxasRequest = new DadosTaxasRequest();
                    dadosTaxasRequest.setValoraliquota(getBigDecimalContent(doc, "valoraliquota"));
                    dadosTaxasRequest.setValorfixo(getBigDecimalContent(doc, "valorfixo"));
                    dadosTaxasRequest.setPrazo(getBigDecimalContent(doc, "prazo"));
                    dadosTaxasRequestRepository.save(dadosTaxasRequest);
                    formaPagamentoRequest.setDadostaxas(dadosTaxasRequest);
                    formaPagamentoRequest.setFlag("POST");

                    formaPagamentoRequestRepository.save(formaPagamentoRequest);
                }

                return ResponseEntity.status(HttpStatus.OK).body(""); // Retorna um ResponseEntity vazio com status 200 (OK)

            } catch (ParserConfigurationException | SAXException | IOException ex) {
                throw new ApiFormaPagamentoException("Erro ao processar XML: ", ex);
            }
        }
    }

    /**
     * PUT "ATUALIZA UMA FORMA DE PAGAMENTO EXISTENTE UTILIZANDO XML".
     * Método responsável por atualizar uma forma de pagamento, tanto na API externa quanto no banco de dados local.
     *
     * @param xmlFormaPagamento xml com os dados do cadastro da nova categoria.
     * @param id                Id para acesso direto a forma de pagamento cadastrada.
     * @throws ApiFormaPagamentoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public ResponseEntity<String> updateFormaPagamento(String xmlFormaPagamento, String id) throws
            ApiFormaPagamentoException {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("apikey", apiKey);
            map.add("xml", xmlFormaPagamento);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            String url = apiBaseUrl + "/formapagamento/" + id + "/json/";

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonRequestFormaPagamento response = objectMapper.readValue(responseEntity.getBody(), JsonRequestFormaPagamento.class);

            return responseEntity;

//        } catch (JsonProcessingException e) {
//            throw new ApiFormaPagamentoException("Erro ao processar JSON", e);
        } catch (RestClientException e) {
            // Em caso de erro ao chamar a API, salva os dados no banco de dados
            logger.info("API Bling Produto [PUT] indisponível, atualizando o produto no banco de dados");

            Optional<FormaPagamentoResponse> optionalFormaPagamento = formaPagamentoResponseRepository.findById(Long.valueOf(id));
            if (optionalFormaPagamento.isPresent()) {
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(xmlFormaPagamento));
                    Document doc = builder.parse(is);

                    // Mapeamento para ProdutoRequest
                    FormaPagamentoRequest formaPagamentoRequest = new FormaPagamentoRequest();
                    formaPagamentoRequest.setId(Long.valueOf(id));
                    formaPagamentoRequest.setDescricao(getTextContent(doc, "descricao"));
                    formaPagamentoRequest.setCodigoFiscal(getBigDecimalContent(doc, "codigofiscal"));
                    formaPagamentoRequest.setCondicao(getTextContent(doc, "condicao"));
                    formaPagamentoRequest.setDestino(getBigDecimalContent(doc, "destino"));
                    formaPagamentoRequest.setPadrao(getBigDecimalContent(doc, "padrao"));
                    formaPagamentoRequest.setSituacao(getBigDecimalContent(doc, "situacao"));

                    DadosCartaoRequest dadosCartaoRequest = new DadosCartaoRequest();
                    dadosCartaoRequest.setBandeira(getBigDecimalContent(doc, "codigofiscal"));
                    dadosCartaoRequest.setTipointegracao(getBigDecimalContent(doc, "tipointegracao"));
                    dadosCartaoRequest.setCnpjcredenciadora(getTextContent(doc, "cnpjcredenciadora"));
                    dadosCartaoRequest.setAutoliquidacao(getBigDecimalContent(doc, "autoliquidacao"));
                    formaPagamentoRequest.setDadoscartao(dadosCartaoRequest);

                    DadosTaxasRequest dadosTaxasRequest = new DadosTaxasRequest();
                    dadosTaxasRequest.setValoraliquota(getBigDecimalContent(doc, "valoraliquota"));
                    dadosTaxasRequest.setValorfixo(getBigDecimalContent(doc, "valorfixo"));
                    dadosTaxasRequest.setPrazo(getBigDecimalContent(doc, "prazo"));
                    formaPagamentoRequest.setDadostaxas(dadosTaxasRequest);
                    formaPagamentoRequest.setFlag("PUT");

                    // Verifique se o produto existe na tabela de produtoRequest
                    String descricaoFormaPagamentoRequest = doc.getElementsByTagName("descricao").item(0).getTextContent();
                    List<FormaPagamentoRequest> produtoRequestExistente = formaPagamentoRequestRepository.findByDescricao(descricaoFormaPagamentoRequest);
                    boolean formaPagamentoRequestExiste = !produtoRequestExistente.isEmpty();
                    //Se o produto não existe na tabela, o mesmo é cadastrado.
                    if (!formaPagamentoRequestExiste) {
                        logger.info("Produto não encontrado no Banco de dados, adicionando... [PUT]");
                        dadosCartaoRequestRepository.save(dadosCartaoRequest);
                        dadosTaxasRequestRepository.save(dadosTaxasRequest);
                        formaPagamentoRequestRepository.save(formaPagamentoRequest);
                    }

                    // Mapeamento para ProdutoResponse
                    FormaPagamentoResponse formaPagamentoResponse = new FormaPagamentoResponse();
                    formaPagamentoResponse.setId(Long.valueOf(id));
                    formaPagamentoResponse.setDescricao(formaPagamentoRequest.getDescricao());
                    formaPagamentoResponse.setCodigoFiscal(String.valueOf(formaPagamentoRequest.getCodigoFiscal()));
                    formaPagamentoResponse.setFixa(String.valueOf(formaPagamentoRequest.getDadostaxas().getValorfixo()));
                    formaPagamentoResponse.setPadrao(String.valueOf(formaPagamentoRequest.getPadrao()));
                    formaPagamentoResponse.setSituacao(String.valueOf(formaPagamentoRequest.getSituacao()));

                    // Verifique se o produto existe na tabela de ProdutoResponse
                    String descricaoFormaPagamentoResponse = doc.getElementsByTagName("descricao").item(0).getTextContent();
                    List<FormaPagamentoResponse> produtoResponseExistente = formaPagamentoResponseRepository.findByDescricao(descricaoFormaPagamentoResponse);
                    boolean formaPagamentoResponseExiste = produtoResponseExistente.isEmpty();
                    //Se o produto existe na tabela, o mesmo é atualizado
                    if (formaPagamentoResponseExiste) {
                        logger.info("Dados atualizados no banco de dados.");
                        formaPagamentoResponseRepository.save(formaPagamentoResponse);
                    }

                    return ResponseEntity.status(HttpStatus.OK).body(""); // Retorna um ResponseEntity vazio com status 200 (OK)

                } catch (NumberFormatException | ParserConfigurationException | IOException | SAXException ex) {
                    throw new ApiFormaPagamentoException("Erro ao processar XML", ex);
                }
            } else {
                throw new ApiFormaPagamentoException("Produto não encontrado no banco de dados", e);
            }
        }
    }

    // Função auxiliar para obter o conteúdo de um nó de texto
    public String getTextContent(Document doc, String tagName) {
        NodeList nodes = doc.getElementsByTagName(tagName);
        if (nodes != null && nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    // Função auxiliar para obter o conteúdo de um nó de texto como Integer
    public Integer getIntegerContent(Document doc, String tagName) {
        String content = getTextContent(doc, tagName);
        return content != null ? Integer.valueOf(content) : null;
    }

    // Função auxiliar para obter o conteúdo de um nó de texto como BigDecimal
    public BigDecimal getBigDecimalContent(Document doc, String tagName) {
        String content = getTextContent(doc, tagName);
        return content != null ? new BigDecimal(content) : null;
    }

    /**
     * Verifica o status da API externa e atualiza o banco de dados local com os formas de pagamento cadastrados na API Bling.
     * Este método é executado periodicamente, com o intervalo de tempo definido na propriedade "api.check.delay".
     * Se uma forma de pagamento existir apenas no banco de dados local, será cadastrada na API do BLING.
     * Se uma forma de pagamento existir tanto no banco de dados local quanto no Bling, a forma de pagamento será deletada do banco de dados local.
     *
     * @throws ApiFormaPagamentoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponível para a consulta.
     */
    @Scheduled(fixedDelayString = "${api.check.delay}")
    public void scheduledPostFormaPagamento() {
        try {
            logger.info("---------- Scheduled POST Forma Pagamento ----------");
//            String url = "http://www.teste.com/";
            String url = apiBaseUrl + "/produtos/json/" + apikeyparam + apiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<FormaPagamentoRequest> formasPagamento = formaPagamentoRequestRepository.findAll();
                List<String> idFormaPagamento = formaPagamentoResponseRepository.findAllCodigo();

                if (formasPagamento.isEmpty() || formasPagamento.stream().noneMatch(request -> "POST".equals(request.getFlag()))) {
                    logger.info("Não há forma de pagamento no banco de dados para cadastrar no Bling");
                    logger.info("--------------------------------------------");
                } else {
                    for (FormaPagamentoRequest formaPagamento : formasPagamento) {
                        if (formaPagamento.getFlag() != null && formaPagamento.getFlag().equals("POST")) {
                            if (!idFormaPagamento.contains(formaPagamento.getDescricao())) {

                                StringBuilder xmlFormaPagamento = new StringBuilder("<formapagamento>");
                                appendIfNotNull(xmlFormaPagamento, "descricao", formaPagamento.getDescricao());
                                appendIfNotNull(xmlFormaPagamento, "codigoFiscal", String.valueOf(formaPagamento.getCodigoFiscal()));
                                appendIfNotNull(xmlFormaPagamento, "condicao", formaPagamento.getCondicao());
                                appendIfNotNull(xmlFormaPagamento, "destino", String.valueOf(formaPagamento.getDestino()));
                                appendIfNotNull(xmlFormaPagamento, "padrao", String.valueOf(formaPagamento.getPadrao()));
                                appendIfNotNull(xmlFormaPagamento, "situacao", String.valueOf(formaPagamento.getSituacao()));
                                xmlFormaPagamento.append("<dadoscartao>");
                                appendIfNotNull(xmlFormaPagamento, "bandeira", String.valueOf(formaPagamento.getDadoscartao().getBandeira()));
                                appendIfNotNull(xmlFormaPagamento, "tipointegracao", String.valueOf(formaPagamento.getDadoscartao().getTipointegracao()));
                                appendIfNotNull(xmlFormaPagamento, "cnpjcredenciadora", String.valueOf(formaPagamento.getDadoscartao().getCnpjcredenciadora()));
                                appendIfNotNull(xmlFormaPagamento, "autoliquidacao", String.valueOf(formaPagamento.getDadoscartao().getAutoliquidacao()));
                                xmlFormaPagamento.append("</dadoscartao>");
                                xmlFormaPagamento.append("<dadostaxas>");
                                appendIfNotNull(xmlFormaPagamento, "valoraliquota", String.valueOf(formaPagamento.getDadostaxas().getValoraliquota()));
                                appendIfNotNull(xmlFormaPagamento, "valorfixo", String.valueOf(formaPagamento.getDadostaxas().getValorfixo()));
                                appendIfNotNull(xmlFormaPagamento, "prazo", String.valueOf(formaPagamento.getDadostaxas().getPrazo()));
                                xmlFormaPagamento.append("</dadostaxas>");
                                xmlFormaPagamento.append("</formapagamento>");

                                // Chama a função createFormaPagamento e armazena o valor de HttpStatus.
                                ResponseEntity<String> createResponse = createFormaPagamento(xmlFormaPagamento.toString());

                                if (createResponse.getStatusCode() == HttpStatus.CREATED) {
                                    logger.info("A Forma pagamento "  + formaPagamento.getDescricao() + " foi cadastrada na plataforma Bling, deletando da fila do banco de dados.");
                                    formaPagamentoRequestRepository.delete(formaPagamento);
                                    dadosCartaoRequestRepository.delete(formaPagamento.getDadoscartao());
                                    dadosTaxasRequestRepository.delete(formaPagamento.getDadostaxas());
                                }
                                logger.info("--------------------------------------------");
                            } else {
                                logger.info("A forma pagamento existe na API, deletando...");
                                formaPagamentoRequestRepository.delete(formaPagamento);
                                dadosCartaoRequestRepository.delete(formaPagamento.getDadoscartao());
                                dadosTaxasRequestRepository.delete(formaPagamento.getDadostaxas());
                            }
                        }
                        logger.info("--------------------------------------------");
                    }
                }
            } else {
                logger.info(" ERRO na função scheduledPostFormaPagamento [POST]");
            }
        } catch (RestClientException e) {
            logger.info("API Bling Forma Pagamento está offline, nada a fazer");
        }
    }

    public void appendIfNotNull(StringBuilder xml, String tagName, String value) {
        if (value != null && !value.equals("null")) {
            xml.append("<").append(tagName).append(">").append(value).append("</").append(tagName).append(">");
        }
    }

    /**
     * Verifica o status da API externa e atualiza o banco de dados local com as formas de pagamento na API Bling.
     * Este método é executado periodicamente, com o intervalo de tempo definido na propriedade "api.check.delay".
     * Se uma forma de paagamento existir apenas no banco de dados local, ela será adicionada na API.
     * Se uma forma de paagamento existir tanto no banco de dados local quanto na API, será deletada do banco de dados local.
     *
     * @throws ApiFormaPagamentoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponível para a consulta.
     */
    @Scheduled(fixedDelayString = "${api.check.delay}")
    public void scheduledUpdateFormaPagamento() {
        try {
            logger.info("---------- Scheduled PUT  Forma Pagamento ----------");
//            String url = "http://www.teste.com/";
            String url = apiBaseUrl + "/produtos/json/" + apikeyparam + apiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<FormaPagamentoRequest> formaPagamentoRequests = formaPagamentoRequestRepository.findAll();

                if (formaPagamentoRequests.isEmpty() || formaPagamentoRequests.stream().noneMatch(request -> "PUT".equals(request.getFlag()))) {
                    logger.info("Não há forma de pagamento no banco de dados para atualizar no Bling");
                    logger.info("--------------------------------------------");
                } else {
                    for (FormaPagamentoRequest formaPagamento : formaPagamentoRequests) {
                        if ("PUT".equals(formaPagamento.getFlag())) { // verifica se a flag é "PUT"
                            String id = String.valueOf(formaPagamento.getId());

                            StringBuilder xmlFormaPagamento = new StringBuilder("<formapagamento>");
                            appendIfNotNull(xmlFormaPagamento, "descricao", formaPagamento.getDescricao());
                            appendIfNotNull(xmlFormaPagamento, "codigoFiscal", String.valueOf(formaPagamento.getCodigoFiscal()));
                            appendIfNotNull(xmlFormaPagamento, "condicao", formaPagamento.getCondicao());
                            appendIfNotNull(xmlFormaPagamento, "destino", String.valueOf(formaPagamento.getDestino()));
                            appendIfNotNull(xmlFormaPagamento, "padrao", String.valueOf(formaPagamento.getPadrao()));
                            appendIfNotNull(xmlFormaPagamento, "situacao", String.valueOf(formaPagamento.getSituacao()));
                            xmlFormaPagamento.append("<dadoscartao>");
                            appendIfNotNull(xmlFormaPagamento, "bandeira", String.valueOf(formaPagamento.getDadoscartao().getBandeira()));
                            appendIfNotNull(xmlFormaPagamento, "tipointegracao", String.valueOf(formaPagamento.getDadoscartao().getTipointegracao()));
                            appendIfNotNull(xmlFormaPagamento, "cnpjcredenciadora", String.valueOf(formaPagamento.getDadoscartao().getCnpjcredenciadora()));
                            appendIfNotNull(xmlFormaPagamento, "autoliquidacao", String.valueOf(formaPagamento.getDadoscartao().getAutoliquidacao()));
                            xmlFormaPagamento.append("</dadoscartao>");
                            xmlFormaPagamento.append("<dadostaxas>");
                            appendIfNotNull(xmlFormaPagamento, "valoraliquota", String.valueOf(formaPagamento.getDadostaxas().getValoraliquota()));
                            appendIfNotNull(xmlFormaPagamento, "valorfixo", String.valueOf(formaPagamento.getDadostaxas().getValorfixo()));
                            appendIfNotNull(xmlFormaPagamento, "prazo", String.valueOf(formaPagamento.getDadostaxas().getPrazo()));
                            xmlFormaPagamento.append("</dadostaxas>");
                            xmlFormaPagamento.append("</formapagamento>");

                            // Chama a função updateProduct e armazena o valor de HttpStatus.
                            ResponseEntity<String> updateResponse = updateFormaPagamento(xmlFormaPagamento.toString(), id);

                            if (updateResponse.getStatusCode() == HttpStatus.CREATED) {
                                logger.info("A Forma pagamento "  + formaPagamento.getDescricao() + " foi atualizado na plataforma Bling, deletando da fila do banco de dados.");
                                formaPagamentoRequestRepository.delete(formaPagamento);
                                dadosCartaoRequestRepository.delete(formaPagamento.getDadoscartao());
                                dadosTaxasRequestRepository.delete(formaPagamento.getDadostaxas());
                            }
                            logger.info("--------------------------------------------");
                        }
                    }
                }
            } else {
                logger.info("ERRO na função scheduledUpdateFormaPagamento [PUT]");
            }
        } catch (RestClientException e) {
            logger.info("API Bling Produto está offline, nada a fazer");
        }
    }

    /**
     * Verifica o status da API externa e deleta no banco de dados local e na API Bling a forma pagamento .
     * Este método é executado periodicamente, com o intervalo de tempo definido na propriedade "api.check.delay".
     * Se uma forma de pagamento existe apenas no banco de dados local, será adicionada na API do BLING.
     * Se um produto existe tanto no banco de dados local quanto no BLING, o produto será deletada do banco de dados local.
     *
     * @throws ApiFormaPagamentoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponível para a consulta.
     */
    @Scheduled(fixedDelayString = "${api.check.delay}")
    public void scheduledDeleteFormaPagamento() {
        try {
            logger.info("--------- Scheduled DELETE Forma Pagamento ----------");
//            String url = "http://www.teste.com/";
            String url = apiBaseUrl + "/produtos/json/" + apikeyparam + apiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<FormaPagamentoRequest> formaPagamentoRequests = formaPagamentoRequestRepository.findAll();

                if (formaPagamentoRequests.isEmpty()) {
                    logger.info("Não há forma de pagamento no banco de dados para atualizar no Bling");
                    logger.info("--------------------------------------------");
                } else {
                    for (FormaPagamentoRequest formaPagamento : formaPagamentoRequests) {
                        if ("DELETE".equals(formaPagamento.getFlag())) { // verifica se a flag é "DELETE".
                            Long id = formaPagamento.getId();

                            // Chama a função deleteProductByCode e armazena o valor de HttpStatus.
                            ResponseEntity<String> deleteResponse = deleteFormaPagemento(String.valueOf(id));

                            if (deleteResponse.getStatusCode() == HttpStatus.OK) {
                                logger.info("A Forma pagamento "  + formaPagamento.getDescricao() + " foi deletada da plataforma Bling, deletando da fila do banco de dados.");
                                formaPagamentoRequestRepository.delete(formaPagamento);
                            }
                            logger.info("--------------------------------------------");
                        }
                    }
                }
            } else {
                logger.info("ERRO na função scheduledDeleteFormaPagamento [DELETE]");
            }
        } catch (RestClientException e) {
            logger.info("API Bling Produto está offline, nada a fazer");
        }
    }
}

