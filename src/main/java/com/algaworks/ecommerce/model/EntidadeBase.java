package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@MappedSuperclass
public abstract class EntidadeBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    //LOCK OTIMISTA
    //DA PARA USAR LONG, LOCALDATETIME, DATE, CALENDAR
    //MAS É RECOMENDÁVEL USAR COMO NÚMERO MESMO
    //DÁ PARA USAR JUNTO LOCK PESSIMISTA DEPENDENDO DO CASO,
    //MAS AS VEZES ATRAPALHA.
    @Version
    private Integer versao;
}