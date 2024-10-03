package com.algaworks.ecommerce.model;

import com.algaworks.ecommerce.listener.GenericoListener;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
//@EqualsAndHashCode(of = {"id"})
@Entity
@SecondaryTable(name = "cliente_detalhe",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "cliente_id"),
                foreignKey = @ForeignKey(name = "fk_cliente_detalhe_cliente"))
@EntityListeners(GenericoListener.class)
@Table(name = "cliente", uniqueConstraints = {
        @UniqueConstraint(name = "unq_cliente_cpf", columnNames = "cpf")},
       indexes = {@Index(name = "idx_cliente_nome", columnList = "nome")})//nome da coluna do BD, não atributo. Poderia ser index para cpf, mas prof usou para nome
public class Cliente extends EntidadeBase {
    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    
    @Column(length = 100, nullable = false)
    private String nome;
    
    @Column(length = 14, nullable = false)
    private String cpf;
    
    @ElementCollection
    @CollectionTable(
            name = "cliente_contato",
            joinColumns = @JoinColumn(name = "cliente_id",
                                      foreignKey = @ForeignKey(name = "fk_cliente_contatos")))
    @MapKeyColumn(name = "tipo")
    @Column(name = "descricao")
    private Map<String,String> contatos;
    
    @Transient
    private String primeiroNome;
    
    @Column(table = "cliente_detalhe", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private SexoCliente sexo;
    
    @Column(name = "data_nascimento", table = "cliente_detalhe")
    private LocalDate dataNascimento;
    
    @ToString.Exclude
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;
    
    @PostLoad
    public void configurarPrimeiroNome() {
//        if (nome != null && !nome.isBlank()) {
//            int index = nome.indexOf(" ");
//            if(index > -1) {
//                primeiroNome = nome.substring(0, index);
//            }
//        }
        
        primeiroNome = Optional.ofNullable(nome)
                               .filter(str -> ! str.isBlank() && str.contains(" "))
                               .map(str -> nome.substring(0, str.indexOf(" ")))
                               .orElse(null);
    }
}