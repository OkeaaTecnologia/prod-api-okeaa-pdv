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
@Table(name = "TB_TOTAL_INFORMADO_CAIXA")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TotalInformadoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("idBd")
    public Long idBd;

    @JsonProperty("idPagamento")
    public Long idPagamento;

    @JsonProperty("descricaoPagamento")
    public String descricaoPagamento;

    @JsonProperty("totalInformado")
    public String totalInformado;

    @JsonProperty("totalDiferenca")
    public String totalDiferenca;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "totalInformadoCaixa_id", referencedColumnName = "id")
    public ControleCaixa totalInformadoCaixa;

    @Override
    public String toString() {
        return "FormaPagamentoCaixa{" +
                "idPagamento=" + idPagamento +
                ", descricaoPagamento='" + descricaoPagamento + '\'' +
                ", totalInformado='" + totalInformado + '\'' +
                ", totalDiferenca='" + totalDiferenca + '\'' +
                ", totalInformadoCaixa=" + (totalInformadoCaixa != null ? totalInformadoCaixa.getId() : null) +
                '}';
    }
}