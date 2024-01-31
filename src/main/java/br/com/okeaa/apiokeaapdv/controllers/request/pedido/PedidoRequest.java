package br.com.okeaa.apiokeaapdv.controllers.request.pedido;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_PEDIDO_REQUEST")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PedidoRequest {

    @Id
    @JsonIgnore
    public Long id;

    @JsonProperty("data")
    @JsonFormat(pattern = "dd/MM/yyyy")
    public String data;

    @JsonProperty("data_saida")
    @JsonFormat(pattern = "dd/MM/yyyy")
    public String data_saida;

    @JsonProperty("data_prevista")
    @JsonFormat(pattern = "dd/MM/yyyy")
    public String data_prevista;

    @JsonProperty("numero")
    public String numero;

    @JsonProperty("numero_loja")
    public String numero_loja;

    @JsonProperty("loja")
    public Integer loja;

    @JsonProperty("nat_operacao")
    public String nat_operacao;

    @JsonProperty("vendedor")
    public String vendedor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id")
    @JsonProperty("cliente")
    public ClienteRequest cliente;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transporte_id")
    @JsonProperty("transporte")
    public TransporteRequest transporte;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pedidoitens", orphanRemoval = true)
    @JsonManagedReference // Anotação para indicar que esta é a ponta "gerenciada" da relação
    @JsonProperty("itens")
    public List<ItensRequest> itens = new ArrayList<>();

    @JsonProperty("idFormaPagamento")
    public Integer idFormaPagamento;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pedidoparcelas", orphanRemoval = true)
    @JsonManagedReference // Anotação para indicar que esta é a ponta "gerenciada" da relação
    @JsonProperty("parcelas")
    public List<ParcelasRequest> parcelas = new ArrayList<>();

    @JsonProperty("vlr_frete")
    public BigDecimal vlr_frete;

    @JsonProperty("vlr_desconto")
    public String vlr_desconto;

    @JsonProperty("obs")
    public String obs;

    @JsonProperty("obs_internas")
    public String obs_internas;

    @JsonProperty("numeroOrdemCompra")
    public String numeroOrdemCompra;

    @JsonProperty("outrasDespesas")
    public BigDecimal outrasDespesas;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intermediador_id")
    @JsonProperty("intermediador")
    public IntermediadorRequest intermediador;

    @JsonProperty("flag")
    public String flag;

    @Override
    public String toString() {
        return "PedidoResponse{" +
                "id='" + id + '\'' +
                ", data=" + data +
                ", data_saida='" + data_saida + '\'' +
                ", data_prevista='" + data_prevista + '\'' +
                ", numero='" + numero + '\'' +
                ", numero_loja=" + numero_loja +
                ", loja=" + loja +
                ", nat_operacao=" + nat_operacao +
                ", vendedor=" + vendedor +
                ", cliente=" + (cliente != null ? cliente.getId() : null) + // Verifique se o cliente não é nulo antes de acessar seu id
                ", transporte='" + transporte + '\'' +
                ", itens=" + (itens != null ? itens.toString() : "null") +
                ", parcelas=" + (parcelas != null ? parcelas.toString() : "null") +
                ", vlr_frete=" + vlr_frete +
                ", vlr_desconto='" + vlr_desconto + '\'' +
                ", obs='" + obs + '\'' +
                ", obs_internas='" + obs_internas + '\'' +
                ", numeroOrdemCompra='" + numeroOrdemCompra + '\'' +
                ", outrasDespesas='" + outrasDespesas + '\'' +
                ", intermediador='" + intermediador + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
