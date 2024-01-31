package br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_FORMAPAGAMENTO_DADOSCARTAO_REQUEST")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosCartaoRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    public Long id;

    @JsonProperty("bandeira")
    public BigDecimal bandeira;

    @JsonProperty("tipointegracao")
    public BigDecimal tipointegracao;

    @JsonProperty("cnpjcredenciadora")
    public String cnpjcredenciadora;

    @JsonProperty("autoliquidacao")
    public BigDecimal autoliquidacao;

    @OneToMany(mappedBy = "dadoscartao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<FormaPagamentoRequest> formaspagamento = new ArrayList<>();

    @Override
    public String toString() {
        return "DadosCartaoRequest{" +
                ", bandeira='" + bandeira + '\'' +
                ", tipointegracao='" + tipointegracao + '\'' +
                ", cnpjcredenciadora='" + cnpjcredenciadora + '\'' +
                ", autoliquidacao='" + autoliquidacao +
                '}';
    }
}
