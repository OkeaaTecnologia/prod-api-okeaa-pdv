package br.com.okeaa.apiokeaapdv.controllers.response.pedido;

import br.com.okeaa.apiokeaapdv.controllers.request.contato.ContatoRequest;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_PEDIDO_PARCELAS_RESPONSE")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParcelasResponse {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pedidoitens_id") // Nome da coluna de chave estrangeira que referencia o pedido em ItensResponse
    @JsonBackReference // Anotação para indicar que esta é a ponta "de volta" da relação
    public PedidoResponse pedidoitens;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parcela_id")
    @JsonProperty("parcela")
    public ParcelaResponse parcela;
}
