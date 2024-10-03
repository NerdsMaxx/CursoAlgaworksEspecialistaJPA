package com.algaworks.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class EnderecoEntregaPedido {

//    @Column(name = "end_cep")
    @Column(length = 9)
    private String cep;
    
//    @Column(name = "end_logradouro")
    @Column(length = 100)
    private String logradouro;
    
//    @Column(name = "end_numero")
    @Column(length = 10)
    private String numero;
    
//    @Column(name = "end_complemento")
    @Column(length = 50)
    private String complemento;
    
//    @Column(name = "end_bairro")
    @Column(length = 50)
    private String bairro;
    
//    @Column(name = "end_cidade")
    @Column(length = 50)
    private String cidade;
    
    @Column(length = 2)
    private String estado;
}