package br.com.okeaa.apiokeaapdv.controllers.request.pedido;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_PEDIDO_PARCELA_REQUEST")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParcelaRequest {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @JsonProperty("dias")
    public Integer dias;

    @JsonProperty("data")
    public String data;

    @JsonProperty("vlr")
    public BigDecimal vlr;

    @JsonProperty("obs")
    public String obs;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "forma_pagamento_id")
    @JsonProperty("forma_pagamento")
    public FormaPagamentoPedidoRequest forma_pagamento;

    @Override
    public String toString() {
        return "ParcelaRequest{" +
                "id=" + id +
                ", dias=" + dias +
                ", data='" + data + '\'' +
                ", vlr=" + vlr +
                ", obs='" + obs + '\'' +
                ", forma_pagamento=" + (forma_pagamento != null ? forma_pagamento.toString() : "null") +
                '}';
    }
}
