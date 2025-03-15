package com.algaworks.ecommerce.model;

import com.algaworks.ecommerce.listener.GenericoListener;
import com.algaworks.ecommerce.listener.GerarNotaFiscalListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
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
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Pedido.dadosEssenciais",
                attributeNodes = {
                        @NamedAttributeNode("dataCriacao"),
                        @NamedAttributeNode("status"),
                        @NamedAttributeNode("total"),
                        @NamedAttributeNode(value = "cliente", subgraph = "cli")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "cli",
                                attributeNodes = {
                                    @NamedAttributeNode("nome"),
                                    @NamedAttributeNode("cpf")
                                })
                }
        )
})
@Table(name = "pedido")
//@Cacheable(false)
//@Cacheable
public class Pedido extends EntidadeBase {
//        implements PersistentAttributeInterceptable --> esta solução do HIBERNATE para
//        resolver problema do OneToOne marcado com LAZY não funciona no HIBERNATE 6 e também NÃO é ideal.
//        Mesmo que funcionasse, o código fica poluído e a entidade não fica legal.
//        Outro jeito é simplesmente remover os atributos notaFiscal e pagamento,
//        pois não são necessários.
//        Mas se não é para remover, então deixa JOIN FETCH explicito no JPQL para ficar mais claro.
//        Mas parece que existe uma outra solução.

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

//    @Column(name = "cliente_id")
//    private Integer clienteId;

//    @ManyToOne(optional = false, cascade = CascadeType.PERSIST) //NÃO É RECOMENDADO
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_pedido_cliente"))
    //, columnDefinition = "bigint") pode usar tmb column definition, mas não precisa, não tem necessidade
    private Cliente cliente;
    
    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;
    
    @PastOrPresent
    @NotNull
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;
    
    @PastOrPresent
    @Column(name = "data_ultima_atualizacao", insertable = false)
    private LocalDateTime dataUltimaAtualizacao;
    
    @PastOrPresent
    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;
    
    @NotNull
    @Positive
    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal total;
    
    @NotNull
    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    
    @Embedded
    private EnderecoEntregaPedido enderecoEntrega;
    
    @NotEmpty
    @ToString.Exclude
//    @OneToMany(mappedBy = "pedido", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @OneToMany(mappedBy = "pedido", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    @OneToMany(mappedBy = "pedido", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itemPedidos;

//    @OneToOne(mappedBy = "pedido")
//    private PagamentoCartao pagamentoCartao;
    
    //    @LazyToOne(LazyToOneOption.NO_PROXY)
//    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    @OneToOne(mappedBy = "pedido")
    private Pagamento pagamento;
    
    //    @Column(name = "nota_fiscal_id")
//    private Integer notaFiscalId;
    
    //    @LazyToOne(LazyToOneOption.NO_PROXY)
//    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
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
    
    
    //    public NotaFiscal getNotaFiscal() {
//        if (this.persistentAttributeInterceptor != null) {
//            return (NotaFiscal) persistentAttributeInterceptor
//                    .readObject(this, "notaFiscal", this. notaFiscal);
//        }
//
//        return this.notaFiscal;
//    }
//
//    public void setNotaFiscal(NotaFiscal notaFiscal) {
//        if (this.persistentAttributeInterceptor != null) {
//            this.notaFiscal = (NotaFiscal) persistentAttributeInterceptor
//                    .writeObject(this, "notaFiscal", this.notaFiscal, notaFiscal);
//        } else {
//            this.notaFiscal = notaFiscal;
//        }
//    }
//
//    public Pagamento getPagamento() {
//        if (this.persistentAttributeInterceptor != null) {
//            return (Pagamento) persistentAttributeInterceptor
//                    .readObject(this, "pagamento", this.pagamento);
//        }
//
//        return this.pagamento;
//    }
//
//    public void setPagamento(Pagamento pagamento) {
//        if (this.persistentAttributeInterceptor != null) {
//            this.pagamento = (Pagamento) persistentAttributeInterceptor
//                    .writeObject(this, "pagamento", this.pagamento, pagamento);
//        } else {
//            this.pagamento = pagamento;
//        }
//    }
//
//    @Setter(AccessLevel.NONE)
//    @Getter(AccessLevel.NONE)
//    @Transient
//    private PersistentAttributeInterceptor persistentAttributeInterceptor;
//
//    @Override
//    public PersistentAttributeInterceptor $$_hibernate_getInterceptor() {
//        return this.persistentAttributeInterceptor;
//    }
//
//    @Override
//    public void $$_hibernate_setInterceptor(PersistentAttributeInterceptor persistentAttributeInterceptor) {
//        this.persistentAttributeInterceptor = persistentAttributeInterceptor;
//    }

}