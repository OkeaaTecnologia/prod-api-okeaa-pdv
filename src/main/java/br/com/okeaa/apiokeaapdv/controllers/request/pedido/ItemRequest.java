package br.com.okeaa.apiokeaapdv.controllers.request.pedido;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_PEDIDO_ITEM_REQUEST")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemRequest {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @JsonProperty("codigo")
    public String codigo;

    @JsonProperty("descricao")
    public String descricao;

    @JsonProperty("un")
    public String un;

    @JsonProperty("qtde")
    public BigDecimal qtde;

    @JsonProperty("vlr_unit")
    public BigDecimal vlr_unit;

    @JsonProperty("vlr_desconto")
    public BigDecimal vlr_desconto;

    // Classe ItemRequest
    @Override
    public String toString() {
        return "ItemRequest{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", un='" + un + '\'' +
                ", qtde=" + qtde +
                ", vlr_unit=" + vlr_unit +
                ", vlr_desconto=" + vlr_desconto +
                '}';
    }
}
