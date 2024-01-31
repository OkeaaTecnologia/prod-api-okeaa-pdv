package br.com.okeaa.apiokeaapdv.service;

import br.com.okeaa.apiokeaapdv.controllers.ContatoController;
import br.com.okeaa.apiokeaapdv.controllers.request.contato.ContatoRequest;
import br.com.okeaa.apiokeaapdv.controllers.response.contato.*;
import br.com.okeaa.apiokeaapdv.exceptions.contato.ApiContatoException;
import br.com.okeaa.apiokeaapdv.repositories.contato.*;
import br.com.okeaa.apiokeaapdv.service.contato.ContatoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class ContatoServiceImpl implements ContatoService {

    public static final Logger logger = LoggerFactory.getLogger(ContatoServiceImpl.class);

    @Value("${external.api.url}")
    public String apiBaseUrl;

    @Value("${external.api.apikey}")
    public String apiKey;

    @Value("${external.api.apikeyparam}")
    public String apikeyparam;

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public ContatoResponseRepository contatoResponseRepository;

    @Autowired
    public ContatoRequestRepository contatoRequestRepository;

    @Autowired
    public TipoContatoResponseRepository tipoContatoResponseRepository;

    @Autowired
    public TipoContatoRequestRepository tipoContatoRequestRepository;

    @Autowired
    public TiposContatoResponseRepository tiposContatoResponseRepository;


    /**
     * GET "BUSCAR A LISTA DE CONTATOS CADASTRADOS NO BLING".
     * Método responsável por buscar a lista de contatos, tanto na API externa quanto no banco de dados local.
     *
     * @throws ApiContatoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
//    @Override
//    @Transactional
//    public JsonResponseContato getAllContacts(int pagina) throws ApiContatoException {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> request = new HttpEntity<>(headers);
//
//            ArrayList<RetornoResponseContato.Contatos> contatosResponse = new ArrayList<>();
//            List<ContatoResponse> contatos = new ArrayList<>();
//
//            boolean hasMorePages = true;
//
//            // Verifica se o banco de dados já contém dados
//            if (contatoResponseRepository.count() > 0) {
//                System.out.println("O banco de dados já contém dados, pulando o loop.");
//                hasMorePages = false;
//
//                String urlPage = apiBaseUrl + "/contatos/page=" + pagina + "/json/" + apikeyparam + apiKey;
//
//                ResponseEntity<String> responsePage = restTemplate.exchange(urlPage, HttpMethod.GET, request, String.class);
//
//                ObjectMapper objectMapperPage = new ObjectMapper();
//                JsonResponseContato jsonResponseContatoPage = objectMapperPage.readValue(responsePage.getBody(), JsonResponseContato.class);
//
//                List<RetornoResponseContato.Contatos> contatosListPage = jsonResponseContatoPage.getRetorno().getContatos();
//
//                if (contatosListPage != null) {
//                    for (RetornoResponseContato.Contatos contato : contatosListPage) {
//                        if (contato != null && contato.getContato() != null) {
//                            contatos.add(contato.getContato());
//                        }
//                    }
//                }
//            }
//
//            while (hasMorePages) {
//                String url = apiBaseUrl + "/contatos/page=" + pagina + "/json/" + apikeyparam + apiKey;
//
//                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
//
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonResponseContato jsonResponseContato = objectMapper.readValue(response.getBody(), JsonResponseContato.class);
//
//                if (jsonResponseContato == null || jsonResponseContato.getRetorno() == null) {
//                    System.out.println("Não há mais contatos, saindo do loop.");
//                    hasMorePages = false;
//                    break;
//                }
//
//                List<RetornoResponseContato.Contatos> contatosList = jsonResponseContato.getRetorno().getContatos();
//
//                if (contatosList != null) {
//                    for (RetornoResponseContato.Contatos contato : contatosList) {
//                        if (contato != null && contato.getContato() != null) {
//                            contatos.add(contato.getContato());
//                        }
//                    }
//                }
//
//                pagina++;
//
//                // Verifica se a página está vazia ou nula
//                if (contatosList == null || contatosList.isEmpty()) {
//                    System.out.println("A página está vazia ou nula, saindo do loop.");
//                    hasMorePages = false;
//                    break;
//                }
//            }
//
//            for (ContatoResponse contato : contatos) {
//                Optional<ContatoResponse> contatoExistente = contatoResponseRepository.findById(contato.getId());
//                if (contatoExistente.isPresent()) {
//                    ContatoResponse contatoAtualizado = contatoExistente.get();
//                    contatoAtualizado.setId(contato.getId());
//                    contatoAtualizado.setCodigo(contato.getCodigo());
//                    contatoAtualizado.setNome(contato.getNome());
//                    contatoAtualizado.setFantasia(contato.getFantasia());
//                    contatoAtualizado.setTipo(contato.getTipo());
//                    contatoAtualizado.setCnpj(contato.getCnpj());
//                    contatoAtualizado.setIe_rg(contato.getIe_rg());
//                    contatoAtualizado.setEndereco(contato.getEndereco());
//                    contatoAtualizado.setNumero(contato.getNumero());
//                    contatoAtualizado.setBairro(contato.getBairro());
//                    contatoAtualizado.setCep(contato.getCep());
//                    contatoAtualizado.setCidade(contato.getCidade());
//                    contatoAtualizado.setComplemento(contato.getComplemento());
//                    contatoAtualizado.setUf(contato.getUf());
//                    contatoAtualizado.setFone(contato.getFone());
//                    contatoAtualizado.setEmail(contato.getEmail());
//                    contatoAtualizado.setSituacao(contato.getSituacao());
//                    contatoAtualizado.setContribuinte(contato.getContribuinte());
//                    contatoAtualizado.setSite(contato.getSite());
//                    contatoAtualizado.setCelular(contato.getCelular());
//                    contatoAtualizado.setDataAlteracao(contato.getDataAlteracao());
//                    contatoAtualizado.setDataInclusao(contato.getDataInclusao());
//                    contatoAtualizado.setSexo(contato.getSexo());
//                    contatoAtualizado.setClienteDesde(contato.getClienteDesde());
//                    contatoAtualizado.setLimiteCredito(contato.getLimiteCredito());
//                    contatoAtualizado.setDataNascimento(contato.getDataNascimento());
//
//                    // Itera sobre os tipos de contato da lista de entrada e salva aqueles que não estão no banco de dados
//                    for (TiposContatoResponse tiposContato : contato.getTiposContato()) {
//                        Optional<TipoContatoResponse> tipoContatoExistente = tipoContatoResponseRepository.findByDescricao(tiposContato.getTipoContato().getDescricao());
//                        if (tipoContatoExistente.isPresent()) {
//                            tiposContato.setTipoContato(tipoContatoExistente.get());
//                        } else {
//                            TipoContatoResponse novoTipoContato = new TipoContatoResponse();
//                            novoTipoContato.setDescricao(tiposContato.getTipoContato().getDescricao());
//                            tipoContatoResponseRepository.save(novoTipoContato);
//                            tiposContato.setTipoContato(novoTipoContato);
//                        }
//                    }
//
//                    // Obtém os tipos de contato existentes para o contato atualizado
//                    List<TiposContatoResponse> tiposContatoExistente = contatoAtualizado.getTiposContato();
//                    List<TiposContatoResponse> tiposContatoAdicionar = contato.getTiposContato();
//
//                    // Lista para armazenar os tipos de contato que precisam ser excluídos
//                    List<TiposContatoResponse> tiposContatoParaExcluir = new ArrayList<>();
//                    List<TiposContatoResponse> tiposContatoParaAdicionar = new ArrayList<>();
//
//                    // Itera sobre os tipos de contato existentes
//                    for (TiposContatoResponse tipoContatoExistente : tiposContatoExistente) {
//                        boolean encontrado = false;
//
//                        // Itera sobre os tipos de contato da lista de entrada
//                        for (TiposContatoResponse tipoContatoEntrada : contato.getTiposContato()) {
//                            if (tipoContatoExistente.getTipoContato().getDescricao().equals(tipoContatoEntrada.getTipoContato().getDescricao())) {
//                                encontrado = true;
//                                break;
//                            }
//                        }
//                        // Se o tipo de contato existente não foi encontrado na lista de entrada, marque-o para exclusão
//                        if (!encontrado) {
//                            tiposContatoParaExcluir.add(tipoContatoExistente);
//                        }
//                    }
//
//                    // Itera sobre os tipos de contato existentes
//                    for (TiposContatoResponse tipoContatoAdicionar : tiposContatoAdicionar) {
//                        boolean encontrado = false;
//
//                        Optional<TiposContatoResponse> tipoContatoExistente = tiposContatoResponseRepository.findByTipoContatoIdAndContatoResponseId(
//                                tipoContatoAdicionar.getTipoContato().getId(), contatoAtualizado.getId());
//
//                        if (tipoContatoExistente.isPresent()) {
//                            encontrado = true;
//                        }
//
//                        // Se o tipo de contato não foi encontrado no banco de dados, marque-o para adição
//                        if (!encontrado) {
//                            tiposContatoParaAdicionar.add(tipoContatoAdicionar);
//                        }
//                    }
//
//                    // Realiza a exclusão dos tipos de contato marcados para exclusão
//                    for (TiposContatoResponse tipoContatoParaExcluir : tiposContatoParaExcluir) {
//                        tiposContatoResponseRepository.delete(tipoContatoParaExcluir);
//                        tiposContatoExistente.remove(tipoContatoParaExcluir);
//                    }
//
//                    // Realiza a adição dos tipos de contato marcados para adição
//                    for (TiposContatoResponse tipoContatoParaAdicionar : tiposContatoParaAdicionar) {
//                        tiposContatoResponseRepository.save(tipoContatoParaAdicionar);
//                        tiposContatoExistente.add(tipoContatoParaAdicionar);
//                    }
//
//                    // Define a lista de tipos de contato em contatoAtualizado
//                    contatoAtualizado.setTiposContato(tiposContatoExistente);
//
//                    // Após salvar os tipos de contato, salve o contato atualizado
//                    contatoResponseRepository.save(contatoAtualizado);
//
//                } else {
//                    for (TiposContatoResponse tiposContato : contato.getTiposContato()) {
//                        Optional<TipoContatoResponse> tipoContatoExistente = tipoContatoResponseRepository.findByDescricao(tiposContato.getTipoContato().getDescricao());
//                        if (tipoContatoExistente.isPresent()) {
//                            tiposContato.setTipoContato(tipoContatoExistente.get());
//                        } else {
//                            TipoContatoResponse novoTipoContato = new TipoContatoResponse();
//                            novoTipoContato.setDescricao(tiposContato.getTipoContato().getDescricao());
//                            tipoContatoResponseRepository.save(novoTipoContato);
//                            tiposContato.setTipoContato(novoTipoContato);
//                        }
//                        if (contato.getId() != null) {
//                            tiposContato.setContatoResponse(contato);
//                        }
//                    }
//                    contatoResponseRepository.save(contato);
//                }
//
//                RetornoResponseContato.Contatos contatoResponse = new RetornoResponseContato.Contatos();
//                contatoResponse.setContato(contato);
//                contatosResponse.add(contatoResponse);
//            }
//
//            RetornoResponseContato retornoResponse = new RetornoResponseContato();
//            retornoResponse.setContatos(contatosResponse);
//
//            JsonResponseContato jsonRetornoResponse = new JsonResponseContato();
//            jsonRetornoResponse.setRetorno(retornoResponse);
//
//            return jsonRetornoResponse;
//
//        } catch (JsonProcessingException e) {
//            throw new ApiContatoException("Erro ao processar JSON", e);
//        } catch (RestClientException e) {
//
//            List<ContatoResponse> contatos = contatoResponseRepository.findAll();
//            if (!contatos.isEmpty()) {
//                ArrayList<RetornoResponseContato.Contatos> contatosResponse = new ArrayList<>();
//                for (ContatoResponse contato : contatos) {
//                    RetornoResponseContato.Contatos contatoResponse = new RetornoResponseContato.Contatos();
//                    contatoResponse.setContato(contato);
//                    contatosResponse.add(contatoResponse);
//                }
//
//                RetornoResponseContato retornoResponse = new RetornoResponseContato();
//                retornoResponse.setContatos(contatosResponse);
//                JsonResponseContato jsonResponse = new JsonResponseContato();
//                jsonResponse.setRetorno(retornoResponse);
//
//                return jsonResponse;
//            } else {
//                logger.info("API Bling Contato [GET] indisponível, não há dados para inserir no banco de dados local.");
//                return new JsonResponseContato(); // Isso cria uma resposta vazia
//            }
//        }
//    }

    @Async
    @Transactional
    public CompletableFuture<JsonResponseContato> getAllContacts(int pagina) throws ApiContatoException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(headers);

            String urlPage = apiBaseUrl + "/contatos/page=" + pagina + "/json/" + apikeyparam + apiKey;

            ResponseEntity<String> responsePage = restTemplate.exchange(urlPage, HttpMethod.GET, request, String.class);

            ObjectMapper objectMapperPage = new ObjectMapper();
            JsonResponseContato jsonResponseContatoPage = objectMapperPage.readValue(responsePage.getBody(), JsonResponseContato.class);

            return CompletableFuture.completedFuture(jsonResponseContatoPage);

        } catch (JsonProcessingException e) {
            throw new ApiContatoException("Erro ao processar JSON", e);
        } catch (RestClientException e) {

            List<ContatoResponse> contatos = contatoResponseRepository.findAll();
            if (!contatos.isEmpty()) {
                ArrayList<RetornoResponseContato.Contatos> contatosResponse = new ArrayList<>();
                for (ContatoResponse contato : contatos) {
                    RetornoResponseContato.Contatos contatoResponse = new RetornoResponseContato.Contatos();
                    contatoResponse.setContato(contato);
                    contatosResponse.add(contatoResponse);
                }

                RetornoResponseContato retornoResponse = new RetornoResponseContato();
                retornoResponse.setContatos(contatosResponse);
                JsonResponseContato jsonResponse = new JsonResponseContato();
                jsonResponse.setRetorno(retornoResponse);

                return CompletableFuture.completedFuture(jsonResponse);
            } else {
                logger.info("API Bling Contato [GET] indisponível, não há dados para inserir no banco de dados local.");
                return CompletableFuture.completedFuture(new JsonResponseContato()); // Isso cria uma resposta vazia
            }
        }
    }

    @Override
    @Transactional
    public JsonResponseContato getListContacts(String nome, String cnpj, String codigo) throws ApiContatoException {
        try {
            List<ContatoResponse> contatos;

            if (nome != null) {
                contatos = contatoResponseRepository.findContactByName(nome);
            } else if (cnpj != null) {
                contatos = contatoResponseRepository.findContactByCnpj(cnpj);
            } else {
                contatos = contatoResponseRepository.findContactByCode(codigo);
            }

            if (!contatos.isEmpty()) {
                ArrayList<RetornoResponseContato.Contatos> contatosResponse = new ArrayList<>();

                for (ContatoResponse contato : contatos) {
                    RetornoResponseContato.Contatos contatoResponse = new RetornoResponseContato.Contatos();
                    contatoResponse.setContato(contato);
                    contatosResponse.add(contatoResponse);
                }

                RetornoResponseContato retornoResponse = new RetornoResponseContato();
                retornoResponse.setContatos(contatosResponse);

                JsonResponseContato jsonResponse = new JsonResponseContato();
                jsonResponse.setRetorno(retornoResponse);

                return jsonResponse;
            } else {
                logger.info("API Bling Contato [GET] indisponível, não há dados para inserir no banco de dados local.");
                return new JsonResponseContato(); // Isso cria uma resposta vazia
            }
        } catch (RestClientException e) {
            List<ContatoResponse> contatos;

            if (nome != null) {
                contatos = contatoResponseRepository.findContactByName(nome);
            } else if (cnpj != null) {
                contatos = contatoResponseRepository.findContactByCnpj(cnpj);
            } else {
                contatos = contatoResponseRepository.findAll();
            }

            if (!contatos.isEmpty()) {
                ArrayList<RetornoResponseContato.Contatos> contatosResponse = new ArrayList<>();

                for (ContatoResponse contato : contatos) {
                    RetornoResponseContato.Contatos contatoResponse = new RetornoResponseContato.Contatos();
                    contatoResponse.setContato(contato);
                    contatosResponse.add(contatoResponse);
                }

                RetornoResponseContato retornoResponse = new RetornoResponseContato();
                retornoResponse.setContatos(contatosResponse);

                JsonResponseContato jsonResponse = new JsonResponseContato();
                jsonResponse.setRetorno(retornoResponse);

                return jsonResponse;
            } else {
                logger.info("API Bling Contato [GET] indisponível, não há dados para inserir no banco de dados local.");
                return new JsonResponseContato(); // Isso cria uma resposta vazia
            }
        }
    }


    /**
     * GET "BUSCA O CONTATO PELO ID".
     * Método responsável por localizar um contato a partir do seu ID, tanto na API externa quanto no banco de dados local.
     *
     * @param id ID a ser localizado.
     * @throws ApiContatoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public JsonResponseContato getContactsById(String id) throws ApiContatoException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(id, headers);

            /* TESTE BANCO DE DADOS, DESCOMENTAR LINHA ABAIXO */
//            String url = "http://www.teste.com/";
            String url = apiBaseUrl + "/contato/" + id + "/json/" + apikeyparam + apiKey + "&identificador=2";
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonResponseContato jsonResponseContato = objectMapper.readValue(response.getBody(), JsonResponseContato.class);

            return jsonResponseContato;

        } catch (JsonProcessingException e) {
            throw new ApiContatoException("Erro ao processar JSON: ", e);
        } catch (RestClientException e) {
            Optional<ContatoResponse> contatoExistente = contatoResponseRepository.findById(Long.valueOf(id));
            if (contatoExistente.isPresent()) {
                RetornoResponseContato.Contatos contato = new RetornoResponseContato.Contatos();
                contato.setContato(contatoExistente.get());

                JsonResponseContato jsonResponse = new JsonResponseContato();
                jsonResponse.setRetorno(new RetornoResponseContato());
                jsonResponse.getRetorno().setContatos(new ArrayList<>());
                jsonResponse.getRetorno().getContatos().add(contato);

                return jsonResponse;

            } else {
                throw new ApiContatoException("A API está indisponível e o contato não foi encontrado no banco de dados.", e);
            }
        }
    }

    /**
     * POST "CADASTRA UM NOVO CONTATO UTILIZANDO XML".
     * Método responsável por cadastrar o Contato, tanto na API externa quanto no banco de dados local.
     *
     * @param xmlContato xml com os dados do cadastro do novo contato.
     * @throws ApiContatoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public ResponseEntity<String> createContact(String xmlContato) throws ApiContatoException {
        try {
            /* TESTE BANCO DE DADOS, DESCOMENTAR LINHA ABAIXO */
//            String url = "http://www.teste.com/";

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("apikey", apiKey);
            map.add("xml", xmlContato);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            String url = apiBaseUrl + "/contato/json/";

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            return responseEntity;
        } catch (RestClientException e) {
            logger.info("API Bling Contato [POST] indisponível, inserindo o contato na fila do banco de dados");
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(xmlContato));
                Document doc = builder.parse(is);

                Random random = new Random();
                int randomNumber = random.nextInt(900000000) + 100000000; // gera número entre 100000000 e 999999999

                ContatoRequest contatoRequest = new ContatoRequest();
                contatoRequest.setId((long) randomNumber);
                contatoRequest.setNome(getTextContent(doc, "nome"));
                contatoRequest.setFantasia(getTextContent(doc, "fantasia"));
                contatoRequest.setTipoPessoa(getTextContent(doc, "tipoPessoa"));
                contatoRequest.setContribuinte(getIntegerContent(doc, "contribuinte"));
                contatoRequest.setCpf_cnpj(getTextContent(doc, "cpf_cnpj"));
                contatoRequest.setIe_rg(getTextContent(doc, "ie_rg"));
                contatoRequest.setEndereco(getTextContent(doc, "endereco"));
                contatoRequest.setNumero(getTextContent(doc, "numero"));
                contatoRequest.setComplemento(getTextContent(doc, "complemento"));
                contatoRequest.setBairro(getTextContent(doc, "bairro"));
                contatoRequest.setCep(getTextContent(doc, "cep"));
                contatoRequest.setCidade(getTextContent(doc, "cidade"));
                contatoRequest.setUf(getTextContent(doc, "uf"));
                contatoRequest.setFone(getTextContent(doc, "fone"));
                contatoRequest.setCelular(getTextContent(doc, "celular"));
                contatoRequest.setEmail(getTextContent(doc, "email"));
                contatoRequest.setEmailNfe(getTextContent(doc, "emailNfe"));
                contatoRequest.setLimiteCredito(getBigDecimalContent(doc, "limiteCredito"));
                contatoRequest.setPaisOrigem(getTextContent(doc, "paisOrigem"));
                contatoRequest.setCodigo(getTextContent(doc, "codigo"));
                contatoRequest.setSite(getTextContent(doc, "site"));
                contatoRequest.setSexo(getTextContent(doc, "sexo"));
                contatoRequest.setSituacao(getTextContent(doc, "situacao"));
                contatoRequest.setDataNascimento(getTextContent(doc, "dataNascimento"));
//                contatoRequest.setInformacaoContato(getTextContent(doc, "informacaoContato"));
//                contatoRequest.setObs(getTextContent(doc, "obs"));
                contatoRequest.setFlag("POST");

//                // Preenchimento dos tipos de contato
//                List<ContatoRequest> contatos = new ArrayList<>();
//                NodeList tiposContatoNodes = doc.getElementsByTagName("tipos_contatos").item(0).getChildNodes();
//                List<TipoContatoRequest> tipoContatoList = new ArrayList<>();
//                for (int i = 0; i < tiposContatoNodes.getLength(); i++) {
//                    Node tipoContatoNode = tiposContatoNodes.item(i);
//                    if (tipoContatoNode.getNodeType() == Node.ELEMENT_NODE) {
//                        Element tipoContatoElement = (Element) tipoContatoNode;
//                        TipoContatoRequest tipoContatoRequest = new TipoContatoRequest();
//                        tipoContatoRequest.setDescricao(tipoContatoElement.getElementsByTagName("descricao").item(0).getTextContent());
//                        tipoContatoList.add(tipoContatoRequest);
//                    }
//                }
//                List<TiposContatoRequest> tiposContatoList = new ArrayList<>();
//                for (TipoContatoRequest tipoContatoRequest : tipoContatoList) {
//                    TiposContatoRequest tiposContato = new TiposContatoRequest();
//                    tiposContato.setTipo_contato(tipoContatoRequest);
//                    tiposContatoList.add(tiposContato);
//                }
//                contatoRequest.setTipos_contatos(tiposContatoList);
//                contatos.add(contatoRequest);
//
//                ArrayList<RetornoRequestContato.Contatos> contatosRequest = new ArrayList<>();
//                for (ContatoRequest contato : contatos) {
//                    for (TiposContatoRequest tiposContato : contato.getTipos_contatos()) {
//                        Optional<TipoContatoRequest> tipoContatoExistente = tipoContatoRequestRepository.findByDescricao(tiposContato.getTipo_contato().getDescricao());
//                        if (tipoContatoExistente.isPresent()) {
//                            tiposContato.setTipo_contato(tipoContatoExistente.get());
//                        } else {
//                            TipoContatoRequest novoTipoContato = new TipoContatoRequest();
//                            novoTipoContato.setDescricao(tiposContato.getTipo_contato().getDescricao());
//                            tipoContatoRequestRepository.save(novoTipoContato);
//                            tiposContato.setTipo_contato(novoTipoContato);
//                        }
//                        tiposContato.setContatoRequest(contato);
//                    }
////                    contatoRequestRepository.save(contato);
//                    RetornoRequestContato.Contatos contatoRequestPut = new RetornoRequestContato.Contatos();
//                    contatoRequestPut.setContato(contato);
//                    contatosRequest.add(contatoRequestPut);
//                }

                String nomeContato = doc.getElementsByTagName("cpf_cnpj").item(0).getTextContent();
                List<ContatoRequest> contatoExistente = contatoRequestRepository.findByCpfCnpj(nomeContato);
                boolean contatoJaExiste = !contatoExistente.isEmpty();
                if (!contatoJaExiste) {
                    contatoRequestRepository.save(contatoRequest);
                }

                return ResponseEntity.status(HttpStatus.OK).body(""); // Retorna um ResponseEntity vazio com status 200 (OK)

            } catch (ParserConfigurationException | SAXException | IOException ex) {
                throw new ApiContatoException("Erro ao processar XML: ", ex);
            }
        }
    }

    @Override
    public List<TipoContatoResponse> getAllTiposContato() {
        return tipoContatoResponseRepository.findAll();
    }

    /**
     * PUT "ATUALIZA UM CONTATO EXISTENTE UTILIZANDO XML".
     * Método responsável por atualizar um contato, tanto na API externa quanto no banco de dados local.
     *
     * @param xmlContato xml com os dados do cadastro do novo contato.
     * @param id         ID para acesso direto ao contato cadastrado.
     * @throws ApiContatoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public ResponseEntity<String> updateContact(@RequestBody @Valid String xmlContato, @PathVariable("id") String id) throws ApiContatoException {
        try {
            /* TESTE BANCO DE DADOS, DESCOMENTAR LINHA ABAIXO */
//            String url = "http://www.teste.com/";

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("apikey", apiKey);
            map.add("xml", xmlContato);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            String url = apiBaseUrl + "/contato/" + id + "/json/";

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonRequestContato result = objectMapper.readValue(response.getBody(), JsonRequestContato.class);

            return responseEntity;

//        } catch (JsonProcessingException e) {
//            throw new ApiContatoException("Erro ao processar JSON: ", e);
        } catch (RestClientException e) {
            logger.info("API Bling Contato [PUT] indisponível, inserindo e atualizando o contato na fila do banco de dados");
            Optional<ContatoResponse> optionalContato = contatoResponseRepository.findById(Long.valueOf(id));
            if (optionalContato.isPresent()) {
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(xmlContato));
                    Document doc = builder.parse(is);

                    ContatoRequest contatoRequest = new ContatoRequest();
                    contatoRequest.setId(Long.valueOf(id));
                    contatoRequest.setNome(getTextContent(doc, "nome"));
                    contatoRequest.setFantasia(getTextContent(doc, "fantasia"));
                    contatoRequest.setTipoPessoa(getTextContent(doc, "tipoPessoa"));
                    contatoRequest.setContribuinte(getIntegerContent(doc, "contribuinte"));
                    contatoRequest.setCpf_cnpj(getTextContent(doc, "cpf_cnpj"));
                    contatoRequest.setIe_rg(getTextContent(doc, "ie_rg"));
                    contatoRequest.setEndereco(getTextContent(doc, "endereco"));
                    contatoRequest.setNumero(getTextContent(doc, "numero"));
                    contatoRequest.setComplemento(getTextContent(doc, "complemento"));
                    contatoRequest.setBairro(getTextContent(doc, "bairro"));
                    contatoRequest.setCep(getTextContent(doc, "cep"));
                    contatoRequest.setCidade(getTextContent(doc, "cidade"));
                    contatoRequest.setUf(getTextContent(doc, "uf"));
                    contatoRequest.setFone(getTextContent(doc, "fone"));
                    contatoRequest.setCelular(getTextContent(doc, "celular"));
                    contatoRequest.setEmail(getTextContent(doc, "email"));
                    contatoRequest.setEmailNfe(getTextContent(doc, "emailNfe"));
                    contatoRequest.setLimiteCredito(getBigDecimalContent(doc, "limiteCredito"));
                    contatoRequest.setPaisOrigem(getTextContent(doc, "paisOrigem"));
                    contatoRequest.setCodigo(getTextContent(doc, "codigo"));
                    contatoRequest.setSexo(getTextContent(doc, "sexo"));
                    contatoRequest.setSite(getTextContent(doc, "site"));
                    contatoRequest.setSituacao(getTextContent(doc, "situacao"));
                    contatoRequest.setDataNascimento(getTextContent(doc, "dataNascimento"));
//                    contatoRequest.setInformacaoContato(getTextContent(doc, "informacaoContato"));
//                    contatoRequest.setObs(getTextContent(doc, "obs"));
                    contatoRequest.setFlag("PUT");

//                    // Preenchimento dos tipos de contato
//                    List<ContatoRequest> contatos = new ArrayList<>();
//                    NodeList tiposContatoNodes = doc.getElementsByTagName("tipos_contatos").item(0).getChildNodes();
//                    List<TipoContatoRequest> tipoContatoList = new ArrayList<>();
//                    for (int i = 0; i < tiposContatoNodes.getLength(); i++) {
//                        Node tipoContatoNode = tiposContatoNodes.item(i);
//                        if (tipoContatoNode.getNodeType() == Node.ELEMENT_NODE) {
//                            Element tipoContatoElement = (Element) tipoContatoNode;
//                            TipoContatoRequest tipoContatoRequest = new TipoContatoRequest();
//                            tipoContatoRequest.setDescricao(tipoContatoElement.getElementsByTagName("descricao").item(0).getTextContent());
//                            tipoContatoList.add(tipoContatoRequest);
//                        }
//                    }
//                    List<TiposContatoRequest> tiposContatoList = new ArrayList<>();
//                    for (TipoContatoRequest tipoContatoRequest : tipoContatoList) {
//                        TiposContatoRequest tiposContato = new TiposContatoRequest();
//                        tiposContato.setTipoContato(tipoContatoRequest);
//                        tiposContatoList.add(tiposContato);
//                    }
//                    contatoRequest.setTiposContato(tiposContatoList);
//                    contatos.add(contatoRequest);
//
//                    ArrayList<RetornoRequestContato.Contatos> contatosRequest = new ArrayList<>();
//                    for (ContatoRequest contato : contatos) {
//                        for (TiposContatoRequest tiposContato : contato.getTiposContato()) {
//                            Optional<TipoContatoRequest> tipoContatoExistente = tipoContatoRequestRepository.findByDescricao(tiposContato.getTipoContato().getDescricao());
//                            if (tipoContatoExistente.isPresent()) {
//                                tiposContato.setTipoContato(tipoContatoExistente.get());
//                            } else {
//                                TipoContatoRequest novoTipoContato = new TipoContatoRequest();
//                                novoTipoContato.setDescricao(tiposContato.getTipoContato().getDescricao());
//                                tipoContatoRequestRepository.save(novoTipoContato);
//                                tiposContato.setTipoContato(novoTipoContato);
//                            }
//                            tiposContato.setContatoRequest(contato);
//                        }
//                        contatoRequestRepository.save(contato);
//                        RetornoRequestContato.Contatos contatoRequestPut = new RetornoRequestContato.Contatos();
//                        contatoRequestPut.setContato(contato);
//                        contatosRequest.add(contatoRequestPut);
//                    }

//                    String nomeContato = doc.getElementsByTagName("cpf_cnpj").item(0).getTextContent();
//                    List<ContatoRequest> contatoExistente = contatoRequestRepository.findByCpfCnpj(nomeContato);
//                    boolean contatoJaExiste = !contatoExistente.isEmpty();
//                    if (!contatoJaExiste) {
//                        contatoRequestRepository.save(contatoRequest);
//                    }

                    String cpfCnpj = doc.getElementsByTagName("cpf_cnpj").item(0).getTextContent();
                    List<ContatoRequest> contatoExistente = contatoRequestRepository.findByCpfCnpj(cpfCnpj);
                    boolean contatoJaExiste = !contatoExistente.isEmpty();

                    if (!contatoJaExiste) {
                        // Se o contato não existe, você pode salvá-lo
                        contatoRequestRepository.save(contatoRequest);
                    } else {
                        // Se o contato já existe, você pode atualizá-lo
                        ContatoRequest contatoRequestExistente = contatoExistente.get(0); // Suponhamos que haja apenas um contato com o mesmo cpf_cnpj
                        // Faça as atualizações necessárias no contato existente com base nos dados do 'contatoRequest'
                        contatoRequestExistente.setNome(contatoRequest.getNome());
                        contatoRequestExistente.setCodigo(contatoRequest.getCodigo());
                        contatoRequestExistente.setFantasia(contatoRequest.getFantasia());
                        contatoRequestExistente.setTipoPessoa(contatoRequest.getTipoPessoa());
                        contatoRequestExistente.setCpf_cnpj(contatoRequest.getCpf_cnpj());
                        contatoRequestExistente.setIe_rg(contatoRequest.getIe_rg());
                        contatoRequestExistente.setEndereco(contatoRequest.getEndereco());
                        contatoRequestExistente.setNumero(contatoRequest.getNumero());
                        contatoRequestExistente.setBairro(contatoRequest.getBairro());
                        contatoRequestExistente.setCep(contatoRequest.getCep());
                        contatoRequestExistente.setCidade(contatoRequest.getCidade());
                        contatoRequestExistente.setComplemento(contatoRequest.getComplemento());
                        contatoRequestExistente.setUf(contatoRequest.getUf());
                        contatoRequestExistente.setFone(contatoRequest.getFone());
                        contatoRequestExistente.setEmail(contatoRequest.getEmail());
                        contatoRequestExistente.setContribuinte(contatoRequest.getContribuinte());
                        contatoRequestExistente.setSite(contatoRequest.getSite());
                        contatoRequestExistente.setCelular(contatoRequest.getCelular());
                        contatoRequestExistente.setSexo(contatoRequest.getSexo());
                        contatoRequestExistente.setLimiteCredito(contatoRequest.getLimiteCredito());
                        contatoRequestExistente.setSituacao(contatoRequest.getSituacao());
                        contatoRequestExistente.setDataNascimento(contatoRequest.getDataNascimento());
//                        contatoRequestExistente.setInformacaoContato(contatoRequest.getInformacaoContato());
//                        contatoRequestExistente.setObs(contatoRequest.getObs());

                        contatoRequestRepository.save(contatoRequestExistente);
                    }


                    // Mapeamento para ContatoResponse
                    ContatoResponse contatoResponse = new ContatoResponse();
                    contatoResponse.setId(Long.valueOf(id));
                    contatoResponse.setNome(contatoRequest.getNome());
                    contatoResponse.setFantasia(contatoRequest.getFantasia());
                    contatoResponse.setTipo(contatoRequest.getTipoPessoa());
                    contatoResponse.setContribuinte(String.valueOf(contatoRequest.getContribuinte()));
                    contatoResponse.setCnpj(contatoRequest.getCpf_cnpj());
                    contatoResponse.setIe_rg(contatoRequest.getIe_rg());
                    contatoResponse.setEndereco(contatoRequest.getEndereco());
                    contatoResponse.setNumero(contatoRequest.getNumero());
                    contatoResponse.setComplemento(contatoRequest.getComplemento());
                    contatoResponse.setBairro(contatoRequest.getBairro());
                    contatoResponse.setCep(contatoRequest.getCep());
                    contatoResponse.setCidade(contatoRequest.getCidade());
                    contatoResponse.setUf(contatoRequest.getUf());
                    contatoResponse.setFone(contatoRequest.getFone());
                    contatoResponse.setCelular(contatoRequest.getCelular());
                    contatoResponse.setEmail(contatoRequest.getEmail());
                    contatoResponse.setLimiteCredito(String.valueOf(contatoRequest.getLimiteCredito()));
                    contatoResponse.setCodigo(contatoRequest.getCodigo());
                    contatoResponse.setSite(contatoRequest.getSite());
                    contatoResponse.setSexo(contatoRequest.getSexo());
                    contatoResponse.setSituacao(contatoRequest.getSituacao());
                    contatoResponse.setDataNascimento(contatoRequest.getDataNascimento());

//                    // Preenchimento dos tipos de contato
//                    List<TiposContatoResponse> tiposContatoResponseList = new ArrayList<>();
//                    for (TipoContatoRequest tipoContatoRequest : tipoContatoList) {
//                        TiposContatoResponse tiposContatoResponse = new TiposContatoResponse();
//
//                        // Aqui, supondo que tipoContatoRequest seja um objeto TipoContatoRequest
//                        TipoContatoResponse tipoContatoResponse = new TipoContatoResponse();
//                        tipoContatoResponse.setDescricao(tipoContatoRequest.getDescricao()); // Configure o TipoContatoResponse com base no TipoContatoRequest
//
//                        tiposContatoResponse.setTipoContato(tipoContatoResponse); // Configure TiposContatoResponse com o objeto TipoContatoResponse
//                        tiposContatoResponseList.add(tiposContatoResponse);
//                    }
//                    contatoResponse.setTiposContato(tiposContatoResponseList);

                    // Verifique se o contaato existe na tabela de ContatoResponse
                    String cnpjContatoResponse = doc.getElementsByTagName("cpf_cnpj").item(0).getTextContent();
                    List<ContatoResponse> contatoResponseExistente = contatoResponseRepository.findByCpfCnpj(cnpjContatoResponse);
                    boolean contatoResponseExiste = !contatoResponseExistente.isEmpty();
                    //Se o contato existe na tabela, o mesmo é atualizado
                    if (contatoResponseExiste) {
                        logger.info("Dados atualizados no banco de dados.");
                        contatoResponseRepository.save(contatoResponse);
                    }

                    return ResponseEntity.status(HttpStatus.CREATED).body(""); // Retorna um ResponseEntity vazio com status 200 (OK)

                } catch (NumberFormatException | ParserConfigurationException | IOException | SAXException ex) {
                    throw new ApiContatoException("Erro ao processar XML", ex);
                }
            } else {
                throw new ApiContatoException("Contato não encontrado no banco de dados", e);
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
     * Verifica o status da API externa e atualiza o banco de dados local com os contatos cadastrados na API Bling.
     * Este método é executado periodicamente, com o intervalo de tempo definido na propriedade "api.check.delay".
     * Se um contato existir apenas no banco de dados local, será cadastrada na API do BLING.
     * Se um contato existir tanto no banco de dados local quanto no Bling, o contato será deletada do banco de dados local.
     *
     * @throws ApiContatoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponível para a consulta.
     */
    @Transactional
    @Scheduled(fixedDelayString = "${api.check.delay}")
    public void scheduledPostContato() {
        try {
            logger.info("---------- Scheduled POST Contato ----------");
//            String url = "http://www.teste.com/";
            String url = apiBaseUrl + "/contatos/json/" + apikeyparam + apiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<ContatoRequest> contatos = contatoRequestRepository.findAll();
                List<String> descricaoContatos = contatoResponseRepository.findAllDescricao();

                if (contatos.isEmpty() || contatos.stream().noneMatch(request -> "POST".equals(request.getFlag()))) {
                    logger.info("Não há contato no banco de dados para cadastrar no Bling");
                    logger.info("--------------------------------------------");
                } else {
                    for (ContatoRequest contato : contatos) {
                        if (contato.getFlag() != null && contato.getFlag().equals("POST")) {
                            if (!descricaoContatos.contains(contato.getCpf_cnpj())) {

                                StringBuilder xmlContato = new StringBuilder("<contato>");
                                appendIfNotNull(xmlContato, "nome", contato.getNome());
                                appendIfNotNull(xmlContato, "fantasia", contato.getFantasia());
                                appendIfNotNull(xmlContato, "codigo", contato.getCodigo());
                                appendIfNotNull(xmlContato, "tipoPessoa", contato.getTipoPessoa());
                                appendIfNotNull(xmlContato, "contribuinte", String.valueOf(contato.getContribuinte()));
                                appendIfNotNull(xmlContato, "cpf_cnpj", contato.getCpf_cnpj());
                                appendIfNotNull(xmlContato, "ie_rg", contato.getIe_rg());
                                appendIfNotNull(xmlContato, "endereco", contato.getEndereco());
                                appendIfNotNull(xmlContato, "numero", contato.getNumero());
                                appendIfNotNull(xmlContato, "complemento", contato.getComplemento());
                                appendIfNotNull(xmlContato, "bairro", contato.getBairro());
                                appendIfNotNull(xmlContato, "cep", contato.getCep());
                                appendIfNotNull(xmlContato, "cidade", contato.getCidade());
                                appendIfNotNull(xmlContato, "uf", contato.getUf());
                                appendIfNotNull(xmlContato, "fone", contato.getFone());
                                appendIfNotNull(xmlContato, "celular", contato.getCelular());
                                appendIfNotNull(xmlContato, "email", contato.getEmail());
                                appendIfNotNull(xmlContato, "emailNfe", contato.getEmailNfe());
                                appendIfNotNull(xmlContato, "limiteCredito", String.valueOf(contato.getLimiteCredito()));
                                appendIfNotNull(xmlContato, "sexo", contato.getSexo());
                                appendIfNotNull(xmlContato, "site", contato.getSite());
                                appendIfNotNull(xmlContato, "situacao", contato.getSituacao());
                                appendIfNotNull(xmlContato, "dataNascimento", contato.getDataNascimento());

//                                appendIfNotNull(xmlContato, "informacaoContato", contato.getInformacaoContato());
//                                appendIfNotNull(xmlContato, "obs", contato.getObs());


//                                xmlContato.append("<tipos_contatos>");
//                                for (TiposContatoRequest tipoContato : contato.getTiposContato()) {
//                                    xmlContato.append("<tipo_contato>");
//                                    appendIfNotNull(xmlContato,"descricao", tipoContato.getTipoContato().getDescricao());
//                                    xmlContato.append("</tipo_contato>");
//                                }
//                                xmlContato.append("</tipos_contatos>");
                                xmlContato.append("</contato>");

                                // Chama a função createContact e armazena o valor de HttpStatus.
                                ResponseEntity<String> createResponse = createContact(xmlContato.toString());

                                if (createResponse.getStatusCode() == HttpStatus.CREATED) {
                                    logger.info("O Contato " + contato.getNome() + " foi cadastrado na plataforma Bling, deletando da fila do banco de dados.");
                                    contatoRequestRepository.delete(contato);
                                }
                            } else {
                                logger.info("Contato existe na API, deletando...");
                                contatoRequestRepository.delete(contato);
                            }
                        }
                        logger.info("--------------------------------------------");
                    }
                }
            } else {
                logger.info(" ERRO na função scheduledPostContato [POST]");
            }
        } catch (RestClientException e) {
            logger.info("API Bling contato está offline, nada a fazer");
        }
    }

    public void appendIfNotNull(StringBuilder xml, String tagName, String value) {
        if (value != null && !value.equals("null")) {
            xml.append("<").append(tagName).append(">").append(value).append("</").append(tagName).append(">");
        }
    }

    /**
     * Verifica o status da API externa e atualiza o banco de dados local com o Contato na API Bling.
     * Este método é executado periodicamente, com o intervalo de tempo definido na propriedade "api.check.delay".
     * Se um contato existir apenas no banco de dados local, ela será adicionada na API.
     * Se um contato existir tanto no banco de dados local quanto na API, será deletada do banco de dados local.
     *
     * @throws ApiContatoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponível para a consulta.
     */
    @Scheduled(fixedDelayString = "${api.check.delay}")
    public void scheduledUpdateContato() {
        try {
            logger.info("---------- Scheduled PUT Contato ----------");
//            String url = "http://www.teste.com/";
            String url = apiBaseUrl + "/contatos/json/" + apikeyparam + apiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<ContatoRequest> contatoRequests = contatoRequestRepository.findAll();

                if (contatoRequests.isEmpty() || contatoRequests.stream().noneMatch(request -> "PUT".equals(request.getFlag()))) {
                    logger.info("Não há contato no banco de dados para atualizar no Bling");
                    logger.info("--------------------------------------------");
                } else {
                    for (ContatoRequest contato : contatoRequests) {
                        if ("PUT".equals(contato.getFlag())) { // verifica se a flag é "PUT"
                            String id = String.valueOf(contato.getId());

                            StringBuilder xmlContato = new StringBuilder("<contato>");
                            appendIfNotNull(xmlContato, "nome", contato.getNome());
                            appendIfNotNull(xmlContato, "fantasia", contato.getFantasia());
                            appendIfNotNull(xmlContato, "codigo", contato.getCodigo());
                            appendIfNotNull(xmlContato, "tipoPessoa", contato.getTipoPessoa());
                            appendIfNotNull(xmlContato, "contribuinte", String.valueOf(contato.getContribuinte()));
                            appendIfNotNull(xmlContato, "cpf_cnpj", contato.getCpf_cnpj());
                            appendIfNotNull(xmlContato, "ie_rg", contato.getIe_rg());
                            appendIfNotNull(xmlContato, "endereco", contato.getEndereco());
                            appendIfNotNull(xmlContato, "numero", contato.getNumero());
                            appendIfNotNull(xmlContato, "complemento", contato.getComplemento());
                            appendIfNotNull(xmlContato, "bairro", contato.getBairro());
                            appendIfNotNull(xmlContato, "cep", contato.getCep());
                            appendIfNotNull(xmlContato, "cidade", contato.getCidade());
                            appendIfNotNull(xmlContato, "uf", contato.getUf());
                            appendIfNotNull(xmlContato, "fone", contato.getFone());
                            appendIfNotNull(xmlContato, "celular", contato.getCelular());
                            appendIfNotNull(xmlContato, "email", contato.getEmail());
                            appendIfNotNull(xmlContato, "emailNfe", contato.getEmailNfe());
                            appendIfNotNull(xmlContato, "limiteCredito", String.valueOf(contato.getLimiteCredito()));
                            appendIfNotNull(xmlContato, "situacao", contato.getSituacao());
                            appendIfNotNull(xmlContato, "dataNascimento", contato.getDataNascimento());
//                            appendIfNotNull(xmlContato, "informacaoContato", contato.getInformacaoContato());
//                            appendIfNotNull(xmlContato, "obs", contato.getObs());

//                            xmlContato.append("<tipos_contatos>");
//                            for (TiposContatoRequest tipoContato : contato.getTiposContato()) {
//                                xmlContato.append("<tipo_contato>");
//                                appendIfNotNull(xmlContato,"descricao", tipoContato.getTipoContato().getDescricao());
//                                xmlContato.append("</tipo_contato>");
//                            }
//                            xmlContato.append("</tipos_contatos>");
                            xmlContato.append("</contato>");

                            // Chama a função updateContact e armazena o valor de HttpStatus.
                            ResponseEntity<String> updateResponse = updateContact(xmlContato.toString(), id);

                            if (updateResponse.getStatusCode() == HttpStatus.OK) {
                                logger.info("O Contato " + contato.getNome() + " foi atualizado na plataforma Bling, deletando da fila do banco de dados...");
                                contatoRequestRepository.delete(contato);
                            }
                            logger.info("--------------------------------------------");
                        }
                    }
                }
            } else {
                logger.info("ERRO na função scheduledUpdateContato [PUT]");
            }
        } catch (RestClientException e) {
            logger.info("API Bling contato está offline, nada a fazer");
        }
    }

    /**
     * ---------------------------------------------------- VERSÃO 1 - SEM CONEXÃO AO BANCO DE DADOS. ----------------------------------------------------------
     */

    /**
     * GET "BUSCAR A LISTA DE PRODUTOS CADASTRADO NO BLING".
     *///    @Override
//    public JsonResponse getAllContacts() throws ApiContatoException {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> request = new HttpEntity<>(headers);
//
//            String url = apiBaseUrl + "/contatos/json/" + apikeyparam + apiKey;
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonResponse result = objectMapper.readValue(response.getBody(), JsonResponse.class);
//
//            return result;
//
//        } catch (JsonProcessingException e) {
//            throw new ApiContatoException("Erro ao processar JSON", e);
//        } catch (RestClientException e) {
//            throw new ApiContatoException("Erro ao chamar API", e);
//        }
//    }

    /**
     * GET "BUSCAR UM PRODUTO PELO CÒDIGO (ID)".
     */
//    @Override
//    public JsonResponse getContactsById(String id) throws ApiContatoException {
//        try {
//            /* TESTE BANCO DE DADOS, DESCOMENTAR LINHA ABAIXO */
////            String url = "http://www.teste.com/";
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> request = new HttpEntity<>(id, headers);
//
//            String url = apiBaseUrl + "/contato/" + id + "/json/" +apikeyparam + apiKey + "&identificador=2";
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonResponse jsonResponse = objectMapper.readValue(response.getBody(), JsonResponse.class);
//
//            return jsonResponse;
//
//        } catch (JsonProcessingException e) {
//            throw new ApiContatoException("Erro ao processar JSON: ", e);
//        } catch (RestClientException e) {
//            Optional<ContatoResponse> contatoExistente = contatoResponseRepository.findById(Long.valueOf(id));
//            if (contatoExistente.isPresent()) {
//                RetornoResponse.Contatos contato = new RetornoResponse.Contatos();
//                contato.setContato(contatoExistente.get());
//
//                JsonResponse jsonResponse = new JsonResponse();
//                jsonResponse.setRetorno(new RetornoResponse());
//                jsonResponse.getRetorno().setContatos(new ArrayList<>());
//                jsonResponse.getRetorno().getContatos().add(contato);
//
//                return jsonResponse;
//
//            } else {
//                throw new ApiContatoException("A API está indisponível e o contato não foi encontrado no banco de dados.", e);
//            }
//        }
//    }

    /**
     * POST "CADASTRAR UM NOVO PRODUTO" UTILIZANDO XML.
     */
//    @Override
//    public JsonRequest createContact(String xmlContato) throws ApiContatoException {
//        try {
//            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//            map.add("apikey", apiKey);
//            map.add("xml", xmlContato);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
//            String url = apiBaseUrl + "/contato/json/";
//
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonRequest result = objectMapper.readValue(response.getBody(), JsonRequest.class);
//
//            return result;
//
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Erro ao processar JSON: ", e);
//        } catch (RestClientException e) {
//            throw new RuntimeException("Erro ao chamar API: ", e);
//        }
//    }

    /**
     * PUT "ATUALIZAR PRODUTO PELO ID UTILIZANDO XML.
     */
//    @Override
//    public JsonRequest updateContact(@RequestBody @Valid String xmlContato, @PathVariable("id") String id) throws ApiContatoException {
//        try {
//            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//            map.add("apikey", apiKey);
//            map.add("xml", xmlContato);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
//            String url = apiBaseUrl + "/contato/" + id + "/json/";
//
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonRequest result = objectMapper.readValue(response.getBody(), JsonRequest.class);
//
//            return result;
//
//        } catch (JsonProcessingException e) {
//            throw new ApiContatoException("Erro ao processar JSON: ", e);
//        } catch (RestClientException e) {
//            throw new ApiContatoException("Erro ao chamar API", e);
//        }
//    }
}