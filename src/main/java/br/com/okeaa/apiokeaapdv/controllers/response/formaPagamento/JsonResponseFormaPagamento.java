package br.com.okeaa.apiokeaapdv.controllers.response.formaPagamento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonResponseFormaPagamento {

	@JsonProperty("retorno")
	public RetornoResponseFormaPagamento retorno;

}


