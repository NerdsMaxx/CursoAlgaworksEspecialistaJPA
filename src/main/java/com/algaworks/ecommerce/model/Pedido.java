package com.algaworks.ecommerce.model;

import com.algaworks.ecommerce.listener.GenericoListener;
import com.algaworks.ecommerce.listener.GerarNotaFiscalListener;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNullElse;

@Getter
@Setter
//@EqualsAndHashCode(of = {"id"})
@EntityListeners({GenericoListener.class, GerarNotaFiscalListener.class})
@Entity
@Table(name = "pedido")
public class Pedido extends EntidadeBase {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

//    @Column(name = "cliente_id")
//    private Integer clienteId;
    
//    @ManyToOne(optional = false, cascade = CascadeType.PERSIST) //NÃO É RECOMENDADO
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_pedido_cliente"))
    //, columnDefinition = "bigint") pode usar tmb column definition, mas não precisa, não tem necessidade
    private Cliente cliente;
    
    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;
    
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_ultima_atualizacao", insertable = false)
    private LocalDateTime dataUltimaAtualizacao;
    
    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;
    
    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal total;
    
    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    
    @Embedded
    private EnderecoEntregaPedido enderecoEntrega;
    
    @ToString.Exclude
//    @OneToMany(mappedBy = "pedido", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @OneToMany(mappedBy = "pedido", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OneToMany(mappedBy = "pedido")
//    @OneToMany(mappedBy = "pedido", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ItemPedido> itemPedidos;

//    @OneToOne(mappedBy = "pedido")
//    private PagamentoCartao pagamentoCartao;
    
    @OneToOne(mappedBy = "pedido")
    private Pagamento pagamento;
    
    //    @Column(name = "nota_fiscal_id")
//    private Integer notaFiscalId;
    
    @OneToOne(mappedBy = "pedido")
    private NotaFiscal notaFiscal;
    
    
    public boolean isPago() {
        return status == StatusPedido.PAGO;
    }
    
    //    @PrePersist NÃO É PERMITIDO ANOTAR MAIS DE UM MÉTODO
//    @PreUpdate
    public void calcularTotal() {
        total = BigDecimal.ZERO;
        
        List<ItemPedido> itemPedidosTemp = requireNonNullElse(itemPedidos, List.of());
        if (! itemPedidosTemp.isEmpty()) {
            Function<ItemPedido,BigDecimal> multiplicarPrecoQuantidade =
                    itemPedido -> BigDecimal.valueOf(itemPedido.getQuantidade()).multiply(itemPedido.getPrecoProduto());
            
            total = itemPedidosTemp.stream().map(multiplicarPrecoQuantidade)
                                   .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
    
    @PrePersist
    public void aoPersistir() {
        System.out.println("PERSISTINDO!!!!");
        dataCriacao = LocalDateTime.now();
        calcularTotal();
    }
    
    @PreUpdate
    public void aoAtualizar() {
        System.out.println("ATUALIZANDO!!!!");
        dataUltimaAtualizacao = LocalDateTime.now();
        calcularTotal();
    }
    
    @PostPersist
    public void aposPersistir() {
        System.out.println("PÓS PERSISTINDO!!!!");
    }
    
    @PostUpdate
    public void aposAtualizar() {
        System.out.println("PÓS ATUALIZANDO!!!!");
    }
    
    @PreRemove
    public void aoRemover() {
        System.out.println("REMOVENDO!!!!");
    }
    
    @PostRemove
    public void aposRemover() {
        System.out.println("PÓS REMOVENDO!!!!");
    }
    
    @PostLoad
    public void aoCarregar() {
        System.out.println("Após carregar o pedido!");
    }
}