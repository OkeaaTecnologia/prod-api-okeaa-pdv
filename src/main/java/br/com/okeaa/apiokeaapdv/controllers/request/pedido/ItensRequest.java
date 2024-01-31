package br.com.okeaa.apiokeaapdv.controllers.request.pedido;

import br.com.okeaa.apiokeaapdv.controllers.response.pedido.PedidoResponse;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_PEDIDO_ITENS_REQUEST")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItensRequest implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    @JsonProperty("item")
    public ItemRequest item;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_request_id") // Nome da coluna de chave estrangeira que referencia o pedido em ItensResponse
    @JsonBackReference // Anotação para indicar que esta é a ponta "de volta" da relação
    public PedidoRequest pedidoitens;

    @Override
    public String toString() {
        return "ItensRequest{" +
                "id=" + id +
                ", item=" + (item != null ? item.toString() : "null") +
                '}';
    }
}
