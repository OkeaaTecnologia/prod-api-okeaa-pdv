package br.com.okeaa.apiokeaapdv.controllers.response.formaPagamento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_FORMAPAGAMENTO_RESPONSE")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormaPagamentoResponse {

    @Id
    @JsonProperty("id")
    public Long id;

    @JsonProperty("descricao")
    public String descricao;

    @JsonProperty("codigoFiscal")
    public String codigoFiscal;

    @JsonProperty("padrao")
    public String padrao;

    @JsonProperty("situacao")
    public String situacao;

    @JsonProperty("fixa")
    public String fixa;
}
