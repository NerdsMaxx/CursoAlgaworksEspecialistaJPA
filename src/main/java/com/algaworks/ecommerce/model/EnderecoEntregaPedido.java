package com.algaworks.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class EnderecoEntregaPedido {

//    @Column(name = "end_cep")
    @NotBlank
    @Pattern(regexp = "\\d{5}-?\\d{2}")
    @Column(length = 9)
    private String cep;
    
//    @Column(name = "end_logradouro")
    @NotBlank
    @Column(length = 100)
    private String logradouro;
    
//    @Column(name = "end_numero")
    @NotBlank
    @Column(length = 10)
    private String numero;
    
//    @Column(name = "end_complemento")
    @Column(length = 50)
    private String complemento;
    
//    @Column(name = "end_bairro")
    @NotBlank
    @Column(length = 50)
    private String bairro;
    
//    @Column(name = "end_cidade")
    @NotBlank
    @Column(length = 50)
    private String cidade;
    
    @NotBlank
    @Pattern(regexp = "[A-Z]{2}")
    @Column(length = 2)
    private String estado;
}