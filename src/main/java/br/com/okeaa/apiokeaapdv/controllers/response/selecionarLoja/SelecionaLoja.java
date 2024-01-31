package br.com.okeaa.apiokeaapdv.controllers.response.selecionarLoja;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_SELECIONA_LOJA")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelecionaLoja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("idBd")
    public Long idBd;

    @JsonProperty("id")
    public String id;

    @JsonProperty("idLoja")
    public String idLoja;

    @JsonProperty("nomeLoja")
    public String nomeLoja;

    @JsonProperty("unidadeLoja")
    public String unidadeLoja;

}
