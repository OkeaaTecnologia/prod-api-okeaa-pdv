package br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento;

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
@Table(name = "TB_FORMAPAGAMENTO_REQUEST")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormaPagamentoRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public Long idBd;

    @JsonProperty("id")
    public Long id;

    @JsonProperty("descricao")
    public String descricao;

    @JsonProperty("codigoFiscal")
    public BigDecimal codigoFiscal;

    @JsonProperty("condicao")
    public String condicao;

    @JsonProperty("destino")
    public BigDecimal destino;

    @JsonProperty("padrao")
    public BigDecimal padrao;

    @JsonProperty("situacao")
    public BigDecimal situacao;

    @ManyToOne
    @JoinColumn(name = "dadoscartao_id") // Coluna de chave estrangeira referenciando a categoria
    @JsonProperty("dadoscartao")
    public DadosCartaoRequest dadoscartao;

    @ManyToOne
    @JoinColumn(name = "dadostaxas_id") // Coluna de chave estrangeira referenciando a categoria
    @JsonProperty("dadostaxas")
    public DadosTaxasRequest dadostaxas;

    @JsonIgnore
    public String flag;

    @Override
    public String toString() {
        return "ProdutoResponse{" +
                "id='" + id + '\'' +
                ", descricao='" + descricao + '\'' +
                ", codigoFiscal='" + codigoFiscal + '\'' +
                ", condicao='" + condicao + '\'' +
                ", destino='" + destino + '\'' +
                ", padrao='" + padrao + '\'' +
                ", situacao='" + situacao + '\'' +
                ", dadoscartao='" + dadoscartao + '\'' +
                ", dadostaxas='" + dadostaxas + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
