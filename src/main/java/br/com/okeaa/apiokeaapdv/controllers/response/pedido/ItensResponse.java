package br.com.okeaa.apiokeaapdv.controllers.response.pedido;

import br.com.okeaa.apiokeaapdv.controllers.response.contato.ContatoResponse;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_PEDIDO_ITENS_RESPONSE")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItensResponse implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @JsonProperty("item")
    public ItemResponse item;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id") // Nome da coluna de chave estrangeira que referencia o pedido em ItensResponse
    @JsonBackReference // Anotação para indicar que esta é a ponta "de volta" da relação
    public PedidoResponse pedidoitens;

}
