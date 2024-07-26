package com.algaworks.ecommerce.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class EnderecoEntregaPedido {

//    @Column(name = "end_cep")
    private String cep;
    
//    @Column(name = "end_logradouro")
    private String logradouro;
    
//    @Column(name = "end_numero")
    private String numero;
    
//    @Column(name = "end_complemento")
    private String complemento;
    
//    @Column(name = "end_bairro")
    private String bairro;
    
//    @Column(name = "end_cidade")
    private String cidade;
    
    private String estado;
}