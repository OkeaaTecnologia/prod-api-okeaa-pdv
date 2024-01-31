package br.com.okeaa.apiokeaapdv.controllers.response.pedido;

import br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento.FormaPagamentoRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_PEDIDO_CONTATO_RESPONSE")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClienteResponse {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("idBd")
    public Integer idBd;

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

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<PedidoResponse> pedidoCliente = new ArrayList<>();

    @Override
    public String toString() {
        return "ClienteResponse{" +
                "id=" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", ie='" + ie + '\'' +
                ", indIEDest='" + indIEDest + '\'' +
                ", rg='" + rg + '\'' +
                ", endereco='" + endereco + '\'' +
                ", numero='" + numero + '\'' +
                ", complemento='" + complemento + '\'' +
                ", cidade='" + cidade + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cep='" + cep + '\'' +
                ", uf='" + uf + '\'' +
                ", email='" + email + '\'' +
                ", celular='" + celular + '\'' +
                ", fone='" + fone + '\'' +
                '}';
    }

}
