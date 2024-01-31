package br.com.okeaa.apiokeaapdv.controllers.request.pedido;

import br.com.okeaa.apiokeaapdv.controllers.response.pedido.PedidoResponse;
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
@Table(name = "TB_PEDIDO_CONTATO_REQUEST")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClienteRequest {

    @Id
    @JsonProperty("id")
    public Integer id;

    @JsonProperty("nome")
    public String nome;

    @JsonProperty("tipoPessoa")
    public String tipoPessoa;

    @JsonProperty("cpf_cnpj")
    public String cpf_cnpj;

    @JsonProperty("ie")
    public String ie;

    @JsonProperty("rg")
    public String rg;

    @JsonProperty("contribuinte")
    public String contribuinte;

    @JsonProperty("endereco")
    public String endereco;

    @JsonProperty("numero")
    public String numero;

    @JsonProperty("complemento")
    public String complemento;

    @JsonProperty("bairro")
    public String bairro;

    @JsonProperty("cep")
    public String cep;

    @JsonProperty("cidade")
    public String cidade;

    @JsonProperty("uf")
    public String uf;

    @JsonProperty("fone")
    public String fone;

    @JsonProperty("celular")
    public String celular;

    @JsonProperty("email")
    public String email;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<PedidoRequest> pedidoCliente = new ArrayList<>();

    @Override
    public String toString() {
        return "ClienteRequest{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipoPessoa='" + tipoPessoa + '\'' +
                ", cpf_cnpj='" + cpf_cnpj + '\'' +
                ", ie='" + ie + '\'' +
                ", rg='" + rg + '\'' +
                ", contribuinte='" + contribuinte + '\'' +
                ", endereco='" + endereco + '\'' +
                ", numero='" + numero + '\'' +
                ", complemento='" + complemento + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cep='" + cep + '\'' +
                ", cidade='" + cidade + '\'' +
                ", uf='" + uf + '\'' +
                ", fone='" + fone + '\'' +
                ", celular='" + celular + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
