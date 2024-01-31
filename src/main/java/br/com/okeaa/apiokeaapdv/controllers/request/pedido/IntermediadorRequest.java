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
@Table(name = "TB_PEDIDO_INTERMEDIADOR_REQUEST")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntermediadorRequest {

    @Id
    @JsonProperty("id")
    public String id;

    @JsonProperty("cnpj")
    public String cnpj;

    @JsonProperty("nomeUsuario")
    public String nomeUsuario;

    @JsonProperty("cnpjInstituicaoPagamento")
    public String cnpjInstituicaoPagamento;

    @OneToMany(mappedBy = "intermediador", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<PedidoRequest> pedidoIntermediador = new ArrayList<>();

    @Override
    public String toString() {
        return "IntermediadorRequest{" +
                "id='" + id + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", cnpjInstituicaoPagamento='" + cnpjInstituicaoPagamento + '\'' +
                '}';
    }
}
