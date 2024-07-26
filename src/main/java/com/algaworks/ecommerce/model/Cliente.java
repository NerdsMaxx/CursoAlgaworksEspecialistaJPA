package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nome;
    
    @Enumerated(EnumType.STRING)
    private SexoCliente sexo;
    
    @ToString.Exclude
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;
}