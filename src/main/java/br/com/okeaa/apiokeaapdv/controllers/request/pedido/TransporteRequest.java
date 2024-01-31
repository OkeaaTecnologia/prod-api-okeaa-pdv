package br.com.okeaa.apiokeaapdv.controllers.request.pedido;

import br.com.okeaa.apiokeaapdv.controllers.response.pedido.PedidoResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_PEDIDO_TRANSPORTE_REQUEST")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransporteRequest {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @JsonProperty("transportadora")
    public String transportadora;

    @JsonProperty("tipo_frete")
    public String tipo_frete;

    @JsonProperty("servico_correios")
    public String servico_correios;

    @JsonProperty("codigo_cotacao")
    public String codigo_cotacao;

    @JsonProperty("peso_bruto")
    public BigDecimal peso_bruto;

    @JsonProperty("qtde_volumes")
    public Integer qtde_volumes;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dados_etiqueta_id")
    @JsonProperty("dados_etiqueta")
    public DadosEtiquetaRequest dados_etiqueta;

//    @JsonProperty("volumes")
//    public VolumesRequest volumes;

    @OneToMany(mappedBy = "transporte", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<PedidoRequest> pedidoTransporte = new ArrayList<>();

    @Override
    public String toString() {
        return "TransporteRequest{" +
                "id=" + id +
                ", transportadora='" + transportadora + '\'' +
                ", tipo_frete='" + tipo_frete + '\'' +
                ", servico_correios='" + servico_correios + '\'' +
                ", codigo_cotacao='" + codigo_cotacao + '\'' +
                ", peso_bruto=" + peso_bruto +
                ", qtde_volumes=" + qtde_volumes +
                ", dados_etiqueta=" + (dados_etiqueta != null ? dados_etiqueta.toString() : "null") +
//                ", volumes=" + (volumes != null ? volumes.toString() : "null") +
                '}';
    }
}
