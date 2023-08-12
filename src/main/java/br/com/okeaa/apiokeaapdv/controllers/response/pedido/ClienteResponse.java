package br.com.okeaa.apiokeaapdv.controllers.response.pedido;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@Entity
//@Table(name = "TB_CATEGORIA_RESPONSE")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClienteResponse {

    @JsonProperty("id")
    public String id;

    @JsonProperty("nome")
    public String nome;

    @JsonProperty("cnpj")
    public String cnpj;

    @JsonProperty("ie")
    public String ie;

    @JsonProperty("indIEDest")
    public String indIEDest;

    @JsonProperty("rg")
    public String rg;

    @JsonProperty("endereco")
    public String endereco;

    @JsonProperty("numero")
    public String numero;

    @JsonProperty("complemento")
    public String complemento;

    @JsonProperty("cidade")
    public String cidade;

    @JsonProperty("bairro")
    public String bairro;

    @JsonProperty("cep")
    public String cep;

    @JsonProperty("uf")
    public String uf;

    @JsonProperty("email")
    public String email;

    @JsonProperty("celular")
    public String celular;

    @JsonProperty("fone")
    public String fone;
}
