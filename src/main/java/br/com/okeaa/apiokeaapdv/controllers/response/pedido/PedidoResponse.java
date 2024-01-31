package br.com.okeaa.apiokeaapdv.controllers.response.pedido;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_PEDIDO_RESPONSE")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PedidoResponse {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @JsonProperty("desconto")
    public String desconto;

    @JsonProperty("observacoes")
    public String observacoes;

    @JsonProperty("observacaointerna")
    public String observacaointerna;

    @JsonProperty("data")
    public String data;

    @JsonProperty("numero")
    public String numero;

    @JsonProperty("numeroOrdemCompra")
    public String numeroOrdemCompra;

    @JsonProperty("vendedor")
    public String vendedor;

    @JsonProperty("valorfrete")
    public String valorfrete;

    @JsonProperty("outrasdespesas")
    public String outrasdespesas;

    @JsonProperty("totalprodutos")
    public String totalprodutos;

    @JsonProperty("totalvenda")
    public String totalvenda;

    @JsonProperty("situacao")
    public String situacao;

    @JsonProperty("dataSaida")
    public String dataSaida;

    @JsonProperty("loja")
    public String loja;

    @JsonProperty("dataPrevista")
    public String dataPrevista;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_idBd")
    @JsonProperty("cliente")
    public ClienteResponse cliente;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transporte_id")
    @JsonProperty("transporte")
    public TransporteResponse transporte;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pedidoitens")
    @JsonManagedReference // Anotação para indicar que esta é a ponta "gerenciada" da relação
    @JsonProperty("itens")
    public List<ItensResponse> itens = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pedidoitens")
    @JsonManagedReference // Anotação para indicar que esta é a ponta "gerenciada" da relação
    @JsonProperty("parcelas")
    public List<ParcelasResponse> parcelas = new ArrayList<>();

    @Override
    public String toString() {
        return "PedidoResponse{" +
                "id='" + id + '\'' +
                ", desconto=" + desconto +
                ", observacoes='" + observacoes + '\'' +
                ", observacaointerna='" + observacaointerna + '\'' +
                ", data='" + data + '\'' +
                ", numero=" + numero +
                ", numeroOrdemCompra=" + numeroOrdemCompra +
                ", vendedor=" + vendedor +
                ", valorfrete=" + valorfrete +
                ", outrasdespesas=" + outrasdespesas +
                ", totalprodutos='" + totalprodutos + '\'' +
                ", totalvenda='" + totalvenda + '\'' +
                ", situacao='" + situacao + '\'' +
                ", dataSaida='" + dataSaida + '\'' +
                ", loja=" + loja +
                ", dataPrevista='" + dataPrevista + '\'' +
                ", cliente='" + cliente + '\'' +
                ", transporte='" + transporte + '\'' +
                ", itens='" + itens + '\'' +
                ", parcelas='" + parcelas + '\'' +
                '}';
    }
}