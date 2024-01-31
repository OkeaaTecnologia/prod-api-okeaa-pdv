package br.com.okeaa.apiokeaapdv.controllers.request.formaPagamento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_FORMAPAGAMENTO_DADOSTAXA_REQUEST")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosTaxasRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    public Long id;

    @JsonProperty("valoraliquota")
    public BigDecimal valoraliquota;

    @JsonProperty("valorfixo")
    public BigDecimal valorfixo;

    @JsonProperty("prazo")
    public BigDecimal prazo;

    @OneToMany(mappedBy = "dadostaxas", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<FormaPagamentoRequest> formaspagamento = new ArrayList<>();

    @Override
    public String toString() {
        return "DadosTaxasRequest{" +
                ", valoraliquota='" + valoraliquota + '\'' +
                ", valorfixo='" + valorfixo + '\'' +
                ", prazo='" + prazo +
                '}';
    }
}
