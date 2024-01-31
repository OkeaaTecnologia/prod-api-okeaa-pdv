package br.com.okeaa.apiokeaapdv.controllers.request.pedido;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_PEDIDO_FORMAPAGAMENTO_REQUEST")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormaPagamentoPedidoRequest {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idBd;

    @JsonProperty("id")
    public String id;

    @JsonProperty("descricao")
    public String descricao;

    @OneToMany(mappedBy = "forma_pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<ParcelaRequest> pedidoParcela = new ArrayList<>();

    @Override
    public String toString() {
        return "FormaPagamentoPedidoRequest{" +
                "idBd=" + idBd +
                ", id='" + id + '\'' +
                '}';
    }
}
