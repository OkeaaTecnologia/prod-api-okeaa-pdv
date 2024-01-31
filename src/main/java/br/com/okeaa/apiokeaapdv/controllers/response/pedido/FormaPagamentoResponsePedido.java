package br.com.okeaa.apiokeaapdv.controllers.response.pedido;

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
@Table(name = "TB_PEDIDO_FORMAPAGAMENTO_RESPONSE")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormaPagamentoResponsePedido {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idBd;

    @JsonProperty("id")
    public String id;

    @JsonProperty("descricao")
    public String descricao;

    @JsonProperty("codigoFiscal")
    public String codigoFiscal;

    @JsonProperty("bandeira")
    public String bandeira;

    @OneToMany(mappedBy = "forma_pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<ParcelaResponse> pedidoParcela = new ArrayList<>();

    @Override
    public String toString() {
        return "FormaPagamentoResponsePedido{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", codigoFiscal='" + codigoFiscal + '\'' +
                ", bandeira='" + bandeira + '\'' +
                '}';
    }
}
