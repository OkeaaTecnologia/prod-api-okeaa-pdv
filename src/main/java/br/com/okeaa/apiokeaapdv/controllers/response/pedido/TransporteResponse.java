package br.com.okeaa.apiokeaapdv.controllers.response.pedido;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_PEDIDO_TRANSPORTE_RESPONSE")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransporteResponse {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @JsonProperty("tipo_frete")
    public String tipo_frete;

    @JsonProperty("qtde_volumes")
    public String qtde_volumes;

    @JsonProperty("peso_bruto")
    public String peso_bruto;

    @OneToMany(mappedBy = "transporte", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<PedidoResponse> pedidoTransporte = new ArrayList<>();

    @Override
    public String toString() {
        return "TransporteResponse{" +
                "id=" + id +
                ", tipo_frete='" + tipo_frete + '\'' +
                ", qtde_volumes='" + qtde_volumes + '\'' +
                ", peso_bruto='" + peso_bruto + '\'' +
                '}';
    }
}
