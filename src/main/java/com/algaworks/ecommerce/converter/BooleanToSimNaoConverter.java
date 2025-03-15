package com.algaworks.ecommerce.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BooleanToSimNaoConverter implements AttributeConverter<Boolean, String> {
    
    @Override
    public String convertToDatabaseColumn(Boolean ativo) {
        return Boolean.TRUE.equals(ativo) ? "SIM" : "NAO";
    }
    
    @Override
    public Boolean convertToEntityAttribute(String ativo) {
        return "SIM".equals(ativo);
    }
}