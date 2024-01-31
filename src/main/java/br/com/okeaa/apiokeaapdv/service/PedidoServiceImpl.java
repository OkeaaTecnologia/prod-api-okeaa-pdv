package br.com.okeaa.apiokeaapdv.service;

import br.com.okeaa.apiokeaapdv.controllers.ContatoController;
import br.com.okeaa.apiokeaapdv.controllers.request.pedido.*;
import br.com.okeaa.apiokeaapdv.controllers.response.pedido.*;
import br.com.okeaa.apiokeaapdv.exceptions.contato.ApiContatoException;
import br.com.okeaa.apiokeaapdv.exceptions.pedido.ApiPedidoException;
import br.com.okeaa.apiokeaapdv.repositories.pedido.*;
import br.com.okeaa.apiokeaapdv.service.controleCaixa.ControleCaixaService;
import br.com.okeaa.apiokeaapdv.service.pedido.PedidoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
import java.util.Random;


@Service
public class PedidoServiceImpl implements PedidoService {

    public static final Logger logger = LoggerFactory.getLogger(ContatoController.class);

    @Value("${external.api.url}")
    public String apiBaseUrl;

    @Value("${external.api.apikey}")
    public String apiKey;

    @Value("${external.api.apikeyparam}")
    public String apikeyparam;

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public ControleCaixaService controleCaixaService;

    @Autowired
    public PedidoResponseRepository pedidoResponseRepository;

    @Autowired
    public ItemResponseRepository itemResponseRepository;

    @Autowired
    public ParcelaResponseRepository parcelaResponseRepository;

    @Autowired
    public PedidoRequestRepository pedidoRequestRepository;

    @Autowired
    public ItemRequestRepository itemRequestRepository;

    @Autowired
    public ParcelaRequestRepository parcelaRequestRepository;

    /**
     * GET "BUSCAR A LISTA DE PEDIDOS CADASTRADOS NO BLING".
     * Método responsável por buscar a lista de pedidos, tanto na API externa quanto no banco de dados local.
     *
     * @throws ApiPedidoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    @Transactional
    public JsonResponsePedido getAllPedido() throws ApiPedidoException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(headers);

            /*
             * TESTE BANCO DE DADOS, DESCOMENTAR LINHA ABAIXO
             * Esta parte do código é para fins de teste, você pode descomentá-la
             * se desejar testar com um banco de dados diferente.
             */
//             String url = "http://www.teste.com/";

            // Construa a URL da API com base nas variáveis configuradas
            String url = apiBaseUrl + "/pedidos/json/" + apikeyparam + apiKey;
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonResponsePedido jsonResponsePedido = objectMapper.readValue(responseEntity.getBody(), JsonResponsePedido.class);

//            List<PedidoResponse> pedidos = new ArrayList<>();
//            for (RetornoResponsePedido.Pedidos pedido : jsonResponsePedido.getRetorno().getPedidos()) {
//                pedidos.add(pedido.getPedido());
//            }

            List<PedidoResponse> pedidos = new ArrayList<>();
            if (jsonResponsePedido.getRetorno() != null && jsonResponsePedido.getRetorno().getPedidos() != null) {
                for (RetornoResponsePedido.Pedidos pedido : jsonResponsePedido.getRetorno().getPedidos()) {
                    pedidos.add(pedido.getPedido());
                }
            }

            ArrayList<RetornoResponsePedido.Pedidos> pedidosResponse = new ArrayList<>();
            for (PedidoResponse pedido : pedidos) {
                Optional<PedidoResponse> pedidoExistente = pedidoResponseRepository.findByNumero(pedido.getNumero());
                if (pedidoExistente.isPresent()) {
                    // Pedido já existe, aqui pode ser adicionado a forma de atualizar o pedido existente no banco de dados.
                } else {
                    // percorre as parcelas associadas ao pedido
                    for (ParcelasResponse parcelasPedido : pedido.getParcelas()) {
                        ParcelaResponse parcelaResponse = new ParcelaResponse();
                        parcelaResponse.setIdLancamento(parcelasPedido.getParcela().getIdLancamento());
                        parcelaResponse.setValor(parcelasPedido.getParcela().getValor());
                        parcelaResponse.setDataVencimento(parcelasPedido.getParcela().getDataVencimento());
                        parcelaResponse.setObs(parcelasPedido.getParcela().getObs());
                        parcelaResponse.setDestino(parcelasPedido.getParcela().getDestino());

                        // Associa a forma de pagamento à parcela
                        parcelaResponse.setForma_pagamento(parcelasPedido.getParcela().getForma_pagamento());

                        // Salva a parcela após associar a forma de pagamento
                        parcelaResponseRepository.save(parcelaResponse);
                        // Associa a parcela ao pedido
                        parcelasPedido.setParcela(parcelaResponse);
                    }

                    // Agora, vamos salvar os itens associados ao pedido.
                    for (ItensResponse itensPedido : pedido.getItens()) {
                        ItemResponse itemResponse = new ItemResponse();
                        itemResponse.setId(Long.valueOf(pedido.getNumero()));
                        itemResponse.setCodigo(itensPedido.getItem().getCodigo());
                        itemResponse.setDescricao(itensPedido.getItem().getDescricao());
                        itemResponse.setQuantidade(itensPedido.getItem().getQuantidade());
                        itemResponse.setValorunidade(itensPedido.getItem().getValorunidade());
                        itemResponse.setPrecocusto(itensPedido.getItem().getPrecocusto());
                        itemResponse.setDescontoItem(itensPedido.getItem().getDescontoItem());
                        itemResponse.setUn(itensPedido.getItem().getUn());
                        itemResponse.setPesoBruto(itensPedido.getItem().getPesoBruto());
                        itemResponse.setLargura(itensPedido.getItem().getLargura());
                        itemResponse.setAltura(itensPedido.getItem().getAltura());
                        itemResponse.setProfundidade(itensPedido.getItem().getProfundidade());
                        itemResponse.setDescricaoDetalhada(itensPedido.getItem().getDescricaoDetalhada());
                        itemResponse.setUnidadeMedida(itensPedido.getItem().getUnidadeMedida());
                        itemResponse.setGtin(itensPedido.getItem().getGtin());

                        // Salva o item após associar a forma de pagamento
                        itemResponseRepository.save(itemResponse);

                        // Associa o item ao pedido
                        itensPedido.setItem(itemResponse);
                    }

                    pedidoResponseRepository.save(pedido);
                }

                RetornoResponsePedido.Pedidos pedidoResponse = new RetornoResponsePedido.Pedidos();
                pedidoResponse.setPedido(pedido);
                pedidosResponse.add(pedidoResponse);
            }

            RetornoResponsePedido retornoResponse = new RetornoResponsePedido();
            retornoResponse.setPedidos(pedidosResponse);

            JsonResponsePedido jsonRetornoResponse = new JsonResponsePedido();
            jsonRetornoResponse.setRetorno(retornoResponse);

            return jsonResponsePedido;

        } catch (JsonProcessingException e) {
            throw new ApiPedidoException("Erro ao processar JSON", e);
        } catch (RestClientException e) {

            List<PedidoResponse> contatos = pedidoResponseRepository.findAll();
            if (!contatos.isEmpty()) {
                ArrayList<RetornoResponsePedido.Pedidos> pedidosResponse = new ArrayList<>();
                for (PedidoResponse pedido : contatos) {
                    RetornoResponsePedido.Pedidos pedidoResponse = new RetornoResponsePedido.Pedidos();
                    pedidoResponse.setPedido(pedido);
                    pedidosResponse.add(pedidoResponse);
                }

                RetornoResponsePedido retornoResponse = new RetornoResponsePedido();
                retornoResponse.setPedidos(pedidosResponse);
                JsonResponsePedido jsonResponse = new JsonResponsePedido();
                jsonResponse.setRetorno(retornoResponse);

                return jsonResponse;
            } else {
                logger.info("API Bling Contato [GET] indisponível, não há dados para inserir no banco de dados local.");
                return null;
            }
        }
    }

    /**
     * GET "BUSCA O PEDIDO PELO NUMERO".
     * Método responsável por localizar um pedido a partir do seu numero, tanto na API externa quanto no banco de dados local.
     *
     * @param numero numero do pedido a ser localizado.
     * @throws ApiPedidoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public JsonResponsePedido getPedidoByIdPedido(String numero) throws ApiPedidoException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(numero, headers);

            /*
             * TESTE BANCO DE DADOS, DESCOMENTAR LINHA ABAIXO
             * Esta parte do código é para fins de teste, você pode descomentá-la
             * se desejar testar com um banco de dados diferente.
             */
            // String url = "http://www.teste.com/";

            // Construa a URL da API com base nas variáveis configuradas
            String url = apiBaseUrl + "/pedido/" + numero + "/json/" + apikeyparam + apiKey;
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonResponsePedido response = objectMapper.readValue(responseEntity.getBody(), JsonResponsePedido.class);

            return response;

        } catch (JsonProcessingException e) {
            // Captura de exceção em caso de erro ao processar JSON
            throw new ApiPedidoException("Erro ao processar JSON", e);
        } catch (RestClientException e) {
            // Captura de exceção em caso de erro de cliente REST

            // Verifique se o pedido existe no banco de dados
            Optional<PedidoResponse> pedidoExistente = pedidoResponseRepository.findByNumero(numero);
            if (pedidoExistente.isPresent()) {
                // Se o pedido existe no banco de dados, retorne-o como uma resposta
                RetornoResponsePedido.Pedidos pedido = new RetornoResponsePedido.Pedidos();
                pedido.setPedido(pedidoExistente.get());

                JsonResponsePedido jsonResponse = new JsonResponsePedido();
                jsonResponse.setRetorno(new RetornoResponsePedido());
                jsonResponse.getRetorno().setPedidos(new ArrayList<>());
                jsonResponse.getRetorno().getPedidos().add(pedido);

                return jsonResponse;

            } else {
                // Se o pedido não existe no banco de dados, lance uma exceção personalizada
                throw new ApiContatoException("A API está indisponível e o contato não foi encontrado no banco de dados.", e);
            }
        }
    }


    /**
     * POST "CADASTRA UMA NOVO PEDIDO UTILIZANDO XML".
     * Método responsável por cadastrar um pedido, tanto na API externa quanto no banco de dados local.
     *
     * @param xmlPedido xml com os dados do cadastro do novo pedido.
     * @throws ApiPedidoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public ResponseEntity<String> createPedido(String xmlPedido) throws ApiPedidoException {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("apikey", apiKey);
            map.add("xml", xmlPedido);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            String url = apiBaseUrl + "/pedido/json/";
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlPedido));
            Document doc = builder.parse(is);

            String lojaValue = getTextContent(doc, "loja");
            Integer loja = 0;

            if (!lojaValue.isEmpty()) {
                loja = Integer.parseInt(lojaValue);
            }

            // Extração dos dados de parcelas
            Element parcelasElement = (Element) doc.getElementsByTagName("parcelas").item(0);
            List<ParcelaRequest> parcelaList = new ArrayList<>();

            if (parcelasElement != null) {
                NodeList parcelaNodes = parcelasElement.getElementsByTagName("parcela");
                for (int i = 0; i < parcelaNodes.getLength(); i++) {
                    Element parcelaElement = (Element) parcelaNodes.item(i);
                    ParcelaRequest parcelaRequest = new ParcelaRequest();
                    parcelaRequest.setVlr(getBigDecimalContent(parcelaElement, "vlr"));

                    // Supondo que "forma_pagamento" está dentro de "parcela"
                    Element formaPagamentoElement = (Element) parcelaElement.getElementsByTagName("forma_pagamento").item(0);
                    if (formaPagamentoElement != null) {
                        FormaPagamentoPedidoRequest formaPagamento = new FormaPagamentoPedidoRequest();
                        formaPagamento.setId(getTextContent(formaPagamentoElement, "id"));
                        formaPagamento.setDescricao(getTextContent(formaPagamentoElement, "descricaoPagamento"));
                        parcelaRequest.setForma_pagamento(formaPagamento);
                    }

                    parcelaList.add(parcelaRequest);
                }
            }

            controleCaixaService.saveControleCaixaOP(loja.toString(), parcelaList);

            return responseEntity;

        } catch (RestClientException e) {
            logger.info("API Bling Contato [POST] indisponível, inserindo o contato na fila do banco de dados");
            try {
                if (!xmlPedido.contains("POST")) {

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(xmlPedido));
                    Document doc = builder.parse(is);

                    Random random = new Random();
                    int randomNumber = random.nextInt(900000000) + 100000000; // gera número entre 100000000 e 999999999

                    PedidoRequest pedidoRequest = new PedidoRequest();
                    pedidoRequest.setId((long) randomNumber);
//                    pedidoRequest.setData(getTextContent(doc, "data"));
//                    pedidoRequest.setData_saida(getTextContent(doc, "data_saida"));
                    pedidoRequest.setData_prevista(getTextContent(doc, "data_prevista"));
//                    pedidoRequest.setNumero(getTextContent(doc, "numero"));
                    pedidoRequest.setNumero_loja(getTextContent(doc, "numero_loja"));
                    String lojaValue = getTextContent(doc, "loja");
                    Integer loja = 0;

                    if (!lojaValue.isEmpty()) {
                        loja = Integer.parseInt(lojaValue);
                    }

                    pedidoRequest.setLoja(loja);
                    pedidoRequest.setNat_operacao(getTextContent(doc, "nat_operacao"));
                    pedidoRequest.setVendedor(getTextContent(doc, "vendedor"));
                    pedidoRequest.setIdFormaPagamento(getIntegerContent(doc, "idFormaPagamento"));
                    pedidoRequest.setVlr_frete(getBigDecimalContent(doc, "vlr_frete"));
                    pedidoRequest.setVlr_desconto(getTextContent(doc, "vlr_desconto"));
                    pedidoRequest.setObs(getTextContent(doc, "obs"));
                    pedidoRequest.setObs_internas(getTextContent(doc, "obs_internas"));
                    pedidoRequest.setNumeroOrdemCompra(getTextContent(doc, "numeroOrdemCompra"));
                    pedidoRequest.setOutrasDespesas(getBigDecimalContent(doc, "outrasDespesas"));
                    pedidoRequest.setFlag("POST");

                    // Extração dos dados do cliente
                    Element clienteElement = (Element) doc.getElementsByTagName("cliente").item(0);
                    if (clienteElement != null) {
                        ClienteRequest cliente = new ClienteRequest();
                        cliente.setId(randomNumber);
                        cliente.setNome(getTextContent(doc, "nome"));
                        cliente.setTipoPessoa(getTextContent(doc, "tipoPessoa"));
                        cliente.setCpf_cnpj(getTextContent(doc, "cpf_cnpj"));
                        cliente.setIe(getTextContent(doc, "ie"));
                        cliente.setRg(getTextContent(doc, "rg"));
                        cliente.setContribuinte(getTextContent(doc, "contribuinte"));
                        cliente.setEndereco(getTextContent(doc, "endereco"));
                        cliente.setNumero(getTextContent(doc, "numero"));
                        ;
                        cliente.setComplemento(getTextContent(doc, "complemento"));
                        cliente.setBairro(getTextContent(doc, "bairro"));
                        cliente.setCep(getTextContent(doc, "cep"));
                        cliente.setCidade(getTextContent(doc, "cidade"));
                        cliente.setUf(getTextContent(doc, "uf"));
                        cliente.setFone(getTextContent(doc, "fone"));
                        cliente.setCelular(getTextContent(doc, "celular"));
                        cliente.setEmail(getTextContent(doc, "email"));

                        // Define o cliente no pedido
                        pedidoRequest.setCliente(cliente);
                    }

                    // Extração dos dados de transporte
                    Element transporteElement = (Element) doc.getElementsByTagName("transporte").item(0);
                    if (transporteElement != null) {
                        TransporteRequest transporte = new TransporteRequest();
                        transporte.setId((long) randomNumber);
                        transporte.setTransportadora(getTextContent(doc, "transportadora"));
                        transporte.setTipo_frete(getTextContent(doc, "tipo_frete"));
                        transporte.setServico_correios(getTextContent(doc, "servico_correios"));
                        transporte.setCodigo_cotacao(getTextContent(doc, "codigo_cotacao"));
                        transporte.setPeso_bruto(getBigDecimalContent(doc, "peso_bruto"));
                        transporte.setQtde_volumes(getIntegerContent(doc, "qtde_volumes"));

                        // Extração dos dados de etiqueta
                        Element etiquetaElement = (Element) transporteElement.getElementsByTagName("dados_etiqueta").item(0);
                        if (etiquetaElement != null) {
                            DadosEtiquetaRequest etiqueta = new DadosEtiquetaRequest();
                            etiqueta.setId(String.valueOf(randomNumber));
                            etiqueta.setNome(getTextContent(doc, "nome"));
                            etiqueta.setEndereco(getTextContent(doc, "endereco"));
                            etiqueta.setNumero(getTextContent(doc, "numero"));
                            etiqueta.setComplemento(getTextContent(doc, "complemento"));
                            etiqueta.setMunicipio(getTextContent(doc, "municipio"));
                            etiqueta.setUf(getTextContent(doc, "uf"));
                            etiqueta.setCep(getTextContent(doc, "cep"));
                            etiqueta.setBairro(getTextContent(doc, "bairro"));

                            transporte.setDados_etiqueta(etiqueta);
                        }

//                    // Extrair informações sobre volumes
//                    NodeList volumeNodes = transporteElement.getElementsByTagName("volume");
//                    List<VolumeRequest> volumes = new ArrayList<>();
//                    for (int i = 0; i < volumeNodes.getLength(); i++) {
//                        Element volumeElement = (Element) volumeNodes.item(i);
//                        VolumeRequest volume = new VolumeRequest();
//                        volume.setServico(getTextContent(doc, "servico"));
//                        volume.setCodigoRastreamento(getTextContent(doc, "codigoRastreamento"));
//                        volumes.add(volume);
//                    }
//                    transporte.setVolumes(volumes);

                        pedidoRequest.setTransporte(transporte);
                    }

                    // Extração dos dados de itens
                    List<PedidoRequest> pedidoItens = new ArrayList<>();
                    Node nodeItens = doc.getElementsByTagName("itens").item(0);
                    Element itensElement = (Element) nodeItens;
                    List<ItemRequest> itemList = new ArrayList<>();

                    if (itensElement != null) {
                        NodeList nodes = doc.getElementsByTagName("item");
                        for (int i = 0; i < nodes.getLength(); i++) {
                            Element doc1 = (Element) nodes.item(i);
                            ItemRequest itemRequest = new ItemRequest();
                            itemRequest.setCodigo(doc1.getElementsByTagName("codigo").item(0).getTextContent());
                            itemRequest.setDescricao(doc1.getElementsByTagName("descricao").item(0).getTextContent());
//                            itemRequest.setUn(doc1.getElementsByTagName("un").item(0).getTextContent());

                            // Converta os valores de String para BigDecimal
                            String qtdeStr = doc1.getElementsByTagName("qtde").item(0).getTextContent();
                            BigDecimal qtde = new BigDecimal(qtdeStr);
                            itemRequest.setQtde(qtde);

                            String vlrUnitStr = doc1.getElementsByTagName("vlr_unit").item(0).getTextContent();
                            BigDecimal vlrUnit = new BigDecimal(vlrUnitStr);
                            itemRequest.setVlr_unit(vlrUnit);

                            String vlrDescontoStr = doc1.getElementsByTagName("vlr_desconto").item(0).getTextContent();
                            BigDecimal vlrDesconto;

                            if (vlrDescontoStr != null && !vlrDescontoStr.equals("undefined")) {
                                vlrDesconto = new BigDecimal(vlrDescontoStr);
                            } else {
                                vlrDesconto = BigDecimal.ZERO; // Ou qualquer outro valor padrão desejado
                            }

                            itemRequest.setVlr_desconto(vlrDesconto);

                            itemList.add(itemRequest);
                        }
                    }

                    List<ItensRequest> itensList = new ArrayList<>();
                    for (ItemRequest itemRequest : itemList) {
                        ItensRequest itensContato = new ItensRequest();
                        itensContato.setItem(itemRequest);
                        itensList.add(itensContato);
                    }
                    pedidoRequest.setItens(itensList);
                    pedidoItens.add(pedidoRequest);

                    // Inserção dos dados de itens ao banco de dados
                    for (PedidoRequest pedido : pedidoItens) {
                        for (ItensRequest itens : pedido.getItens()) {
                            ItemRequest novoItem = new ItemRequest();
                            novoItem.setCodigo(itens.getItem().getCodigo());
                            novoItem.setDescricao(itens.getItem().getDescricao());
                            novoItem.setUn(itens.getItem().getUn());
                            novoItem.setQtde(itens.getItem().getQtde());
                            novoItem.setVlr_unit(itens.getItem().getVlr_unit());
                            novoItem.setVlr_desconto(itens.getItem().getVlr_desconto());
                            itemRequestRepository.save(novoItem);

                            itens.setItem(novoItem);
                            itens.setPedidoitens(pedido);
                        }
                    }

                    // Extração dos dados de parcelas
                    List<PedidoRequest> pedidoParcelas = new ArrayList<>();
                    Element parcelasElement = (Element) doc.getElementsByTagName("parcelas").item(0);
                    List<ParcelaRequest> parcelaList = new ArrayList<>();

                    if (parcelasElement != null) {
                        NodeList parcelaNodes = parcelasElement.getElementsByTagName("parcela");
                        for (int i = 0; i < parcelaNodes.getLength(); i++) {
                            Element parcelaElement = (Element) parcelaNodes.item(i);
                            ParcelaRequest parcelaRequest = new ParcelaRequest();
                            parcelaRequest.setDias(getIntegerContent(parcelaElement, "dias"));
                            parcelaRequest.setData(getTextContent(parcelaElement, "data"));
                            parcelaRequest.setVlr(getBigDecimalContent(parcelaElement, "vlr"));
                            parcelaRequest.setObs(getTextContent(parcelaElement, "obs"));

                            // Supondo que "forma_pagamento" está dentro de "parcela"
                            Element formaPagamentoElement = (Element) parcelaElement.getElementsByTagName("forma_pagamento").item(0);
                            if (formaPagamentoElement != null) {
                                FormaPagamentoPedidoRequest formaPagamento = new FormaPagamentoPedidoRequest();
                                formaPagamento.setId(getTextContent(formaPagamentoElement, "id"));
                                parcelaRequest.setForma_pagamento(formaPagamento);
                            }

                            parcelaList.add(parcelaRequest);
                        }
                    }

                    List<ParcelasRequest> parcelasList = new ArrayList<>();
                    for (ParcelaRequest parcelaRequest : parcelaList) {
                        ParcelasRequest parcelasPedido = new ParcelasRequest();
                        parcelasPedido.setParcela(parcelaRequest);
                        parcelasList.add(parcelasPedido);
                    }
                    pedidoRequest.setParcelas(parcelasList);
                    pedidoParcelas.add(pedidoRequest);

                    // Inserção dos dados de parcelas ao banco de dados
                    for (PedidoRequest pedido : pedidoParcelas) {
                        for (ParcelasRequest parcelas : pedido.getParcelas()) {
                            ParcelaRequest novaParcela = new ParcelaRequest();
                            novaParcela.setDias(parcelas.getParcela().getDias());
                            novaParcela.setData(parcelas.getParcela().getData());
                            novaParcela.setVlr(parcelas.getParcela().getVlr());
                            novaParcela.setObs(parcelas.getParcela().getObs());

                            // Inserção dos dados de forma pagamento ao banco de dados
                            novaParcela.setForma_pagamento(parcelas.getParcela().getForma_pagamento());

                            parcelaRequestRepository.save(novaParcela);

                            parcelas.setParcela(novaParcela);
                            parcelas.setPedidoparcelas(pedido);
                        }
                    }

                    // Extração dos dados do intermediador
                    Element intermediadorElement = (Element) doc.getElementsByTagName("intermediador").item(0);
                    if (intermediadorElement != null) {
                        IntermediadorRequest intermediador = new IntermediadorRequest();
                        intermediador.setId(String.valueOf(randomNumber));
                        intermediador.setCnpj(getTextContent(doc, "cnpj"));
                        intermediador.setNomeUsuario(getTextContent(doc, "nomeUsuario"));
                        intermediador.setCnpjInstituicaoPagamento(getTextContent(doc, "cnpjInstituicaoPagamento"));

                        pedidoRequest.setIntermediador(intermediador);
                    }

                    pedidoRequestRepository.save(pedidoRequest);
                }

                return ResponseEntity.status(HttpStatus.OK).body(""); // Retorna um ResponseEntity vazio com status 200 (OK)

            } catch (ParserConfigurationException | SAXException | IOException ex) {
                throw new ApiContatoException("Erro ao processar XML: ", ex);
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * PUT "ATUALIZA UM PEDIDO EXISTENTE UTILIZANDO XML".
     * Método responsável por atualizar um pedido, tanto na API externa quanto no banco de dados local.
     *
     * @param xmlPedido xml com os dados do cadastro do novo pedido.
     * @param numero    Id para acesso direto ao pedido cadastrado.
     * @throws ApiPedidoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponivel para a consulta.
     */
    @Override
    public ResponseEntity<String> updatePedido(String xmlPedido, String numero) throws ApiPedidoException {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("apikey", apiKey);
            map.add("xml", xmlPedido);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            String url = apiBaseUrl + "/pedido/" + numero + "/json/";

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

            return responseEntity;

        } catch (RestClientException e) {
            throw new ApiPedidoException("Erro ao chamar API", e);
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

    public String getTextContent(Element element, String tagName) {
        NodeList nodes = element.getElementsByTagName(tagName);
        if (nodes != null && nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    // Função auxiliar para obter o conteúdo de um nó de texto como Integer
    public Integer getIntegerContent(Element element, String tagName) {
        String content = getTextContent(element, tagName);
        return content != null ? Integer.valueOf(content) : null;
    }

    // Função auxiliar para obter o conteúdo de um nó de texto como BigDecimal
    public BigDecimal getBigDecimalContent(Element element, String tagName) {
        String content = getTextContent(element, tagName);
        return content != null ? new BigDecimal(content) : null;
    }

    /**
     * Verifica o status da API externa e atualiza o banco de dados local com os pedidos cadastrados na API Bling.
     * Este método é executado periodicamente, com o intervalo de tempo definido na propriedade "api.check.delay".
     * Se um pedido existir apenas no banco de dados local, será cadastrada na API do BLING.
     * Se um pedido existir tanto no banco de dados local quanto no Bling, o pedido será deletada do banco de dados local.
     *
     * @throws ApiContatoException Caso ocorra algum erro na comunicação com a API externa o banco de dados fica disponível para a consulta.
     */
    @Transactional
    @Scheduled(fixedDelayString = "${api.check.delay}")
    public void scheduledPostPedido() {
        try {
            logger.info("---------- Scheduled POST Pedido ----------");
//            String url = "http://www.teste.com/";
            String url = apiBaseUrl + "/contatos/json/" + apikeyparam + apiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<PedidoRequest> pedidos = pedidoRequestRepository.findAll();
                List<String> descricaoPedido = pedidoResponseRepository.findAllNumero();

                if (pedidos.isEmpty() || pedidos.stream().noneMatch(request -> "POST".equals(request.getFlag()))) {
                    logger.info("Não há pedido no banco de dados para cadastrar no Bling");
                    logger.info("--------------------------------------------");
                } else {
                    for (PedidoRequest pedido : pedidos) {
                        if (pedido.getFlag() != null && pedido.getFlag().equals("POST")) {
                            if (!descricaoPedido.contains(pedido.getNumero())) {

                                StringBuilder xmlPedido = new StringBuilder("<pedido>");
//                                appendIfNotNull(xmlPedido, "data", pedido.getData());
//                                appendIfNotNull(xmlPedido, "data_saida", pedido.getData_saida());
                                appendIfNotNull(xmlPedido, "data_prevista", pedido.getData_prevista());
//                                appendIfNotNull(xmlPedido, "numero", pedido.getNumero());
                                appendIfNotNull(xmlPedido, "numero_loja", String.valueOf(pedido.getNumero_loja()));
                                appendIfNotNull(xmlPedido, "loja", String.valueOf(pedido.getLoja()));
                                appendIfNotNull(xmlPedido, "nat_operacao", pedido.getNat_operacao());
                                appendIfNotNull(xmlPedido, "vendedor", pedido.getVendedor());
                                appendIfNotNull(xmlPedido, "idFormaPagamento", String.valueOf(pedido.getIdFormaPagamento()));
                                appendIfNotNull(xmlPedido, "vlr_frete", String.valueOf(pedido.getVlr_frete()));
                                appendIfNotNull(xmlPedido, "vlr_desconto", pedido.getVlr_desconto());
                                appendIfNotNull(xmlPedido, "obs", pedido.getObs());
                                appendIfNotNull(xmlPedido, "obs_internas", pedido.getObs_internas());
                                appendIfNotNull(xmlPedido, "numeroOrdemCompra", pedido.getNumeroOrdemCompra());
                                appendIfNotNull(xmlPedido, "outrasDespesas", String.valueOf(pedido.getOutrasDespesas()));
                                appendIfNotNull(xmlPedido, "flag", String.valueOf(pedido.getFlag()));

                                if (pedido.getCliente() != null) {
                                    xmlPedido.append("<cliente>");
                                    appendIfNotNull(xmlPedido, "nome", pedido.getCliente().getNome());
                                    appendIfNotNull(xmlPedido, "tipoPessoa", pedido.getCliente().getTipoPessoa());
                                    appendIfNotNull(xmlPedido, "cpf_cnpj", pedido.getCliente().getCpf_cnpj());
                                    appendIfNotNull(xmlPedido, "ie", pedido.getCliente().getIe());
                                    appendIfNotNull(xmlPedido, "rg", pedido.getCliente().getRg());
                                    appendIfNotNull(xmlPedido, "contribuinte", pedido.getCliente().getContribuinte());
                                    appendIfNotNull(xmlPedido, "endereco", pedido.getCliente().getEndereco());
                                    appendIfNotNull(xmlPedido, "numero", pedido.getCliente().getNumero());
                                    appendIfNotNull(xmlPedido, "complemento", pedido.getCliente().getComplemento());
                                    appendIfNotNull(xmlPedido, "bairro", pedido.getCliente().getBairro());
                                    appendIfNotNull(xmlPedido, "cep", pedido.getCliente().getCep());
                                    appendIfNotNull(xmlPedido, "cidade", pedido.getCliente().getCidade());
                                    appendIfNotNull(xmlPedido, "uf", pedido.getCliente().getUf());
                                    appendIfNotNull(xmlPedido, "fone", pedido.getCliente().getFone());
                                    appendIfNotNull(xmlPedido, "celular", pedido.getCliente().getCelular());
                                    appendIfNotNull(xmlPedido, "email", pedido.getCliente().getEmail());
                                    xmlPedido.append("</cliente>");
                                }

                                if (pedido.getTransporte() != null) {
                                    xmlPedido.append("<transporte>");
                                    appendIfNotNull(xmlPedido, "transportadora", pedido.getTransporte().getTransportadora());
                                    appendIfNotNull(xmlPedido, "tipo_frete", pedido.getTransporte().getTipo_frete());
                                    appendIfNotNull(xmlPedido, "servico_correios", pedido.getTransporte().getServico_correios());
                                    appendIfNotNull(xmlPedido, "codigo_cotacao", pedido.getTransporte().getCodigo_cotacao());
                                    appendIfNotNull(xmlPedido, "peso_bruto", String.valueOf(pedido.getTransporte().getPeso_bruto()));
                                    appendIfNotNull(xmlPedido, "qtde_volumes", String.valueOf(pedido.getTransporte().getQtde_volumes()));

                                    if (pedido.getTransporte().getDados_etiqueta() != null) {
                                        DadosEtiquetaRequest etiqueta = pedido.getTransporte().getDados_etiqueta();
                                        xmlPedido.append("<dados_etiqueta>");
                                        appendIfNotNull(xmlPedido, "nome", etiqueta.getNome());
                                        appendIfNotNull(xmlPedido, "endereco", etiqueta.getEndereco());
                                        appendIfNotNull(xmlPedido, "numero", etiqueta.getNumero());
                                        appendIfNotNull(xmlPedido, "complemento", etiqueta.getComplemento());
                                        appendIfNotNull(xmlPedido, "municipio", etiqueta.getMunicipio());
                                        appendIfNotNull(xmlPedido, "uf", etiqueta.getUf());
                                        appendIfNotNull(xmlPedido, "cep", etiqueta.getCep());
                                        appendIfNotNull(xmlPedido, "bairro", etiqueta.getBairro());
                                        xmlPedido.append("</dados_etiqueta>");
                                    }
                                    xmlPedido.append("</transporte>");
                                }

                                if (!pedido.getItens().isEmpty()) {
                                    xmlPedido.append("<itens>");
                                    for (ItensRequest itens : pedido.getItens()) {
                                        xmlPedido.append("<item>");
                                        appendIfNotNull(xmlPedido, "codigo", itens.getItem().getCodigo());
                                        appendIfNotNull(xmlPedido, "descricao", itens.getItem().getDescricao());
                                        appendIfNotNull(xmlPedido, "un", itens.getItem().getUn());
                                        appendIfNotNull(xmlPedido, "qtde", String.valueOf(itens.getItem().getQtde()));
                                        appendIfNotNull(xmlPedido, "vlr_unit", String.valueOf(itens.getItem().getVlr_unit()));
                                        appendIfNotNull(xmlPedido, "vlr_desconto", String.valueOf(itens.getItem().getVlr_desconto()));
                                        xmlPedido.append("</item>");
                                    }
                                    xmlPedido.append("</itens>");
                                }

                                if (!pedido.getParcelas().isEmpty()) {
                                    xmlPedido.append("<parcelas>");
                                    for (ParcelasRequest parcelas : pedido.getParcelas()) {
                                        xmlPedido.append("<parcela>");
                                        appendIfNotNull(xmlPedido, "dias", String.valueOf(parcelas.getParcela().getDias()));
                                        appendIfNotNull(xmlPedido, "data", parcelas.getParcela().getData());
                                        appendIfNotNull(xmlPedido, "vlr", String.valueOf(parcelas.getParcela().getVlr()));
                                        appendIfNotNull(xmlPedido, "obs", parcelas.getParcela().getObs());
                                        if (parcelas.getParcela().getForma_pagamento() != null) {
                                            appendIfNotNull(xmlPedido, "forma_pagamento", parcelas.getParcela().getForma_pagamento().getId());
                                        }
                                        xmlPedido.append("</parcela>");
                                    }
                                    xmlPedido.append("</parcelas>");
                                }

                                if (pedido.getIntermediador() != null) {
                                    xmlPedido.append("<intermediador>");
                                    appendIfNotNull(xmlPedido, "cnpj", pedido.getIntermediador().getCnpj());
                                    appendIfNotNull(xmlPedido, "nomeUsuario", pedido.getIntermediador().getNomeUsuario());
                                    appendIfNotNull(xmlPedido, "cnpjInstituicaoPagamento", pedido.getIntermediador().getCnpjInstituicaoPagamento());
                                    xmlPedido.append("</intermediador>");
                                }
                                xmlPedido.append("</pedido>");

                                // Chama a função createPedido e armazena o valor de HttpStatus.
                                ResponseEntity<String> createResponse = createPedido(xmlPedido.toString());

                                if (createResponse.getStatusCode() == HttpStatus.CREATED) {
                                    logger.info("O Pedido " + pedido.getNumero() + " foi cadastrado na plataforma Bling, deletando da fila do banco de dados.");
                                    pedidoRequestRepository.delete(pedido);
                                }
                            } else {
                                logger.info("Pedido existe na API, deletando...");
                                pedidoRequestRepository.delete(pedido);
                            }
                        }
                        logger.info("--------------------------------------------");
                    }
                }
            } else {
                logger.info(" ERRO na função scheduledPostPedido [POST]");
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
}