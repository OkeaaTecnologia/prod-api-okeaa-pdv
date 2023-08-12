package br.com.okeaa.apiokeaaproduto.controllers.request.deposito;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_DEPOSITO_REQUEST")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositoRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public Long idBd;

    @JsonProperty("id")
    public Long id;

    @NotBlank(message = "Campo Obrigatorio")
    @Size(max = 120, message = "Descrição do depósito")
    @JsonProperty("descricao")
    public String descricao;

    @Pattern(regexp = "[AI]", message = "Situação do depósito (A ou I)")
    @JsonProperty("situacao")
    public String situacao = "A";

    @NotNull(message = "Define se o depósito vai ser o padrão")
    @JsonProperty("depositoPadrao")
    public boolean depositoPadrao = false;

    @NotNull(message = "Desconsidera saldo deste depósito")
    @JsonProperty("desconsiderarSaldo")
    public boolean desconsiderarSaldo = false;

    @JsonIgnore
    public String flag;
}


