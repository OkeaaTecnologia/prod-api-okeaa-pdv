package br.com.okeaa.apiokeaapdv.controllers.response.controleCaixa;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_FORMA_PAGAMENTO_CAIXA")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormaPagamentoCaixa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("idBd")
    public Long idBd;

    @JsonProperty("id")
    public Long id;

    @JsonProperty("descricao")
    public String descricao;

    @JsonProperty("valorFaturado")
    public String valorFaturado;

    @JsonProperty("totalCalculado")
    public String totalCalculado;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "formaPagamentoCaixa_id", referencedColumnName = "id")
    public ControleCaixa formaPagamentoCaixa;

    @Override
    public String toString() {
        return "FormaPagamentoCaixa{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", valorFaturado='" + valorFaturado + '\'' +
                ", formaPagamentoCaixa=" + (formaPagamentoCaixa != null ? formaPagamentoCaixa.getId() : null) +
                '}';
    }

}

