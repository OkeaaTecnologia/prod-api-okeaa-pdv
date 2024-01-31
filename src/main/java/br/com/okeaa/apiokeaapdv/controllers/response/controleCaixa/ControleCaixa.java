package br.com.okeaa.apiokeaapdv.controllers.response.controleCaixa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_CONTROLE_CAIXA")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ControleCaixa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("idBd")
    public Long idBd;

    @JsonProperty("id")
    public String id;

    @JsonProperty("idLoja")
    public String idLoja;

    @JsonProperty("descricaoLoja")
    public String descricaoLoja;

    @JsonProperty("operadorAbertura")
    public String operadorAbertura;

    @JsonProperty("operadorFechamento")
    public String operadorFechamento;

    @JsonProperty("dataAbertura")
    public String dataAbertura;

    @JsonProperty("dataFechamento")
    public String dataFechamento;

    @JsonProperty("trocoCaixaAnterior")
    public String trocoCaixaAnterior;

    @JsonProperty("trocoCaixa")
    public String trocoCaixa;

//    @JsonProperty("totalRegistrado")
//    public String totalRegistrado;
//
//    @JsonProperty("totalInformado")
//    public String totalInformado;

    @JsonProperty("diferencas")
    public String diferencas;

    @JsonProperty("situacao")
    public String situacao;

    @JsonManagedReference
    @OneToMany(mappedBy = "formaPagamentoCaixa", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<FormaPagamentoCaixa> formaPagamentoCaixa = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "totalInformadoCaixa", cascade = CascadeType.ALL)
    public List<TotalInformadoCaixa> totalInformadoCaixa = new ArrayList<>();

    @Override
    public String toString() {
        return "ControleCaixa{" +
                "id='" + id + '\'' +
                ", idLoja='" + idLoja + '\'' +
                ", descricaoLoja='" + descricaoLoja + '\'' +
                ", operadorAbertura='" + operadorAbertura + '\'' +
                ", operadorFechamento='" + operadorFechamento + '\'' +
                ", dataAbertura='" + dataAbertura + '\'' +
                ", dataFechamento='" + dataFechamento + '\'' +
                ", trocoCaixaAnterior='" + trocoCaixaAnterior + '\'' +
                ", trocoCaixa='" + trocoCaixa + '\'' +
//                ", totalRegistrado='" + totalRegistrado + '\'' +
//                ", totalInformado='" + totalInformado + '\'' +
                ", diferencas='" + diferencas + '\'' +
                ", situacao='" + situacao + '\'' +
                ", formaPagamentoCaixa=" + formaPagamentoCaixa.size() +
                '}';
    }
}