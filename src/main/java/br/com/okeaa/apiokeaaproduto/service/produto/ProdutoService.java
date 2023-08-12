package br.com.okeaa.apiokeaaproduto.service.produto;

import br.com.okeaa.apiokeaaproduto.controllers.request.produto.JsonRequest;
import br.com.okeaa.apiokeaaproduto.controllers.response.produto.JsonResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface ProdutoService {

    public JsonResponse getAllProducts();

    public JsonResponse getProductByCode(@PathVariable("codigo") String codigo);

    public JsonResponse getProductByCodeSupplier(@PathVariable("codigoFabricante") String codigoFabricante, @PathVariable("idFabricante") String idFabricante);

    public void deleteProductByCode(@PathVariable("codigo") String codigo);

    public JsonRequest createProduct(@RequestBody String xmlProdutos);

    public JsonRequest updateProduct(@RequestBody @Valid String xmlProdutos, @PathVariable("codigo") String codigo);
}
