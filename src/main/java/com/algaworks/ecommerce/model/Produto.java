package com.algaworks.ecommerce.model;

import com.algaworks.ecommerce.converter.BooleanToSimNaoConverter;
import com.algaworks.ecommerce.dto.ProdutoDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
//@EqualsAndHashCode(of = {"id"})
@Entity
//@SqlResultSetMapping(name = "produto_loja.Produto",
//                     entities = { @EntityResult(entityClass = Produto.class)})

//@NamedNativeQuery(name = "produto_loja.listar",
//                  query = "select id, nome, descricao, data_criacao, data_ultima_atualizacao, preco, foto " +
//                          "from produto_loja",
//                  resultClass = Produto.class)

@NamedNativeQueries({
        @NamedNativeQuery(name = "produto_loja.listar",
                          query = "select id, nome, descricao, data_criacao, data_ultima_atualizacao, preco, foto " +
                                  "from produto_loja",
                          resultClass = Produto.class),
        @NamedNativeQuery(
                name = "ecm_produto.listar",
                query = "select * from ecm_produto",
                resultSetMapping = "ecm_produto.Produto")
})

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "produto_loja.Produto",
                entities = {@EntityResult(entityClass = Produto.class)}),
        @SqlResultSetMapping(
                name = "ecm_produto.Produto",
                entities = {@EntityResult(
                        entityClass = Produto.class,
                        fields = {
                                @FieldResult(name = "id",
                                             column = "prd_id"),
                                
                                @FieldResult(name = "nome",
                                             column = "prd_nome"),
                                
                                @FieldResult(name = "descricao",
                                             column = "prd_descricao"),
                                
                                @FieldResult(name = "preco",
                                             column = "prd_preco"),
                                
                                @FieldResult(name = "foto",
                                             column = "prd_foto"),
                                
                                @FieldResult(name = "dataCriacao",
                                             column = "prd_data_criacao"),
                                
                                @FieldResult(name = "dataUltimaAtualizacao",
                                             column = "prd_data_ultima_atualizacao")
                        })}),
        
        @SqlResultSetMapping(
                name = "ecm_produto.ProdutoDTO",
                classes = {
                        @ConstructorResult(
                                targetClass = ProdutoDto.class,
                                columns = {
                                        @ColumnResult(name = "prd_id",
                                                type = Integer.class),
                                        
                                        @ColumnResult(name = "prd_nome",
                                                      type = String.class),
                                })
                })
})

@NamedQueries({
        @NamedQuery(name = "Produto.listar", query = "select p from Produto p"),
        @NamedQuery(name = "Produto.listarPorCategoria", query = "select p from Produto p where exists (select 1 from Categoria c2 join c2.produtos p2 where p2 = p and c2.id = :categoria)")
})

@Table(name = "produto",
       uniqueConstraints = {@UniqueConstraint(name = "unq_produto_nome", columnNames = "nome")},
       indexes = {@Index(name = "idx_produto_nome", columnList = "nome")})
public class Produto extends EntidadeBase {


//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tabela")
//    @TableGenerator(name = "tabela",
//                    table = "hibernate_sequences",
//                    pkColumnName = "sequence_name",
//                    pkColumnValue = "produto",
//                    valueColumnName = "next_val")

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

//    @Version
//    private Integer versao;
    
    @PastOrPresent
    @NotNull
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;
    
    @PastOrPresent
    @Column(name = "data_ultima_atualizacao", insertable = false)
    private LocalDateTime dataUltimaAtualizacao;
    
    @NotBlank
    //tem como colocar unique=true, mas ja tá configurado na @Table
    @Column(nullable = false, length = 100) // nome da coluna: "nome", tipo: varchar(255)
    private String nome;
    
    //    @Column(columnDefinition = "varchar(275) default 'descricao'")
    @Lob //descricao tinytext
    @Column(length = Length.LONG32) // 2Gb - OBRIGANDO COLUNA TIPO DE DADOS COM MAIS BYTES, vai ser longtext agora.
    private String descricao;
    
    @Positive
    //    @Column(precision = 10, scale = 2) // nome da coluna: "preco", tipo: decimal(10, 2)
    private BigDecimal preco;
    
    @Convert(converter = BooleanToSimNaoConverter.class)
    @NotNull
    @Column(length = 3, nullable = false)
    private Boolean ativo = Boolean.TRUE;
    
    @Lob
    @Column(length = 1000)
    private byte[] foto;
    
    //    @ManyToMany(cascade = CascadeType.PERSIST)
//    @ManyToMany(cascade = CascadeType.MERGE)
    @ManyToMany
    @JoinTable(name = "produto_categoria",
//               foreignKey = @ForeignKey(name = "fk_produto_produto_categoria"),
//               inverseForeignKey = @ForeignKey(name = "fk_categoria_produto_categoria"),
               joinColumns = @JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name = "fk_produto_produto_categoria")),
               inverseJoinColumns = @JoinColumn(name = "categoria_id", foreignKey = @ForeignKey(name = "fk_categoria_produto_categoria")))
    private List<Categoria> categorias;

//    @ToString.Exclude
//    @OneToMany(mappedBy = "produto")
//    private List<ItemPedido> itemPedidos;
    
    @OneToOne(mappedBy = "produto")
    private Estoque estoque;
    
    @ElementCollection
    @CollectionTable(
            name = "produto_tag",
            joinColumns = @JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name = "fk_produto_tag")))
    @Column(name = "tag", length = 50, nullable = false)
    private List<String> tags;
    
    @ElementCollection
    @CollectionTable(name = "produto_atributo",
                     joinColumns = @JoinColumn(name = "produto_id"), foreignKey = @ForeignKey(name = "fk_produto_atributo"))
    private List<Atributo> atributos;
}