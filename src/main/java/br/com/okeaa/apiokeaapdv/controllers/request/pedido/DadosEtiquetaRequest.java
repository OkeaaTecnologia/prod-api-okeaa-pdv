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
@Table(name = "TB_PEDIDO_DADOS_ETIQUETA_REQUEST")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosEtiquetaRequest {

    @Id
    @JsonProperty("id")
    public String id;

    @JsonProperty("nome")
    public String nome;

    @JsonProperty("endereco")
    public String endereco;

    @JsonProperty("numero")
    public String numero;

    @JsonProperty("complemento")
    public String complemento;

    @JsonProperty("municipio")
    public String municipio;

    @JsonProperty("uf")
    public String uf;

    @JsonProperty("cep")
    public String cep;

    @JsonProperty("bairro")
    public String bairro;

    @OneToMany(mappedBy = "dados_etiqueta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<TransporteRequest> pedidoTransporteEtiqueta = new ArrayList<>();

    @Override
    public String toString() {
        return "ClienteResponse{" +
                "id=" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", numero='" + numero + '\'' +
                ", complemento='" + complemento + '\'' +
                ", municipio='" + municipio + '\'' +
                ", uf='" + uf + '\'' +
                ", cep='" + cep + '\'' +
                ", bairro='" + bairro + '\'' +
                '}';
    }
}
