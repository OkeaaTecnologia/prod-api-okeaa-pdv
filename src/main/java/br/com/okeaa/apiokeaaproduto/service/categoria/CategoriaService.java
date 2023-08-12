package br.com.okeaa.apiokeaaproduto.service.categoria;

import br.com.okeaa.apiokeaaproduto.controllers.request.categoria.JsonRequest;
import br.com.okeaa.apiokeaaproduto.controllers.response.categoria.JsonResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface CategoriaService {

    JsonResponse getAllCategory();

    JsonResponse getCategoryByIdCategoria(@PathVariable("idCategoria") String idCategoria);

    JsonRequest createCategory(@RequestBody String xmlCategoria);

    JsonRequest updateCategory(@RequestBody String xmlCategoria, @PathVariable("idCategoria") String idCategoria) throws ParserConfigurationException, IOException, SAXException;
}