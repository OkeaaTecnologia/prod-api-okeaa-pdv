package br.com.okeaa.apiokeaapdv.controllers.response.pedido;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_PEDIDO_PARCELA_RESPONSE")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParcelaResponse {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @JsonProperty("idLancamento")
    public String idLancamento;

    @JsonProperty("valor")
    public String valor;

    @JsonProperty("dataVencimento")
    public String dataVencimento;

    @JsonProperty("obs")
    public String obs;

    @JsonProperty("destino")
    public String destino;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "forma_pagamento_id")
    @JsonProperty("forma_pagamento")
    public FormaPagamentoResponsePedido forma_pagamento;
}
