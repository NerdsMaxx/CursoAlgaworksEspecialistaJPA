package com.algaworks.ecommerce.consultasnativas;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StoredProceduresTest extends EntityManagerTest {
    
    @Test
    public void usarParametrosInEOut() {
        StoredProcedureQuery storedProcedureQuery =
                entityManager.createStoredProcedureQuery("buscar_nome_produto");
        
        storedProcedureQuery.registerStoredProcedureParameter(
                "produto_id", Integer.class, ParameterMode.IN);
        
        storedProcedureQuery.registerStoredProcedureParameter("produto_nome", String.class, ParameterMode.OUT);
        
        storedProcedureQuery.setParameter("produto_id", 1);
        
        String resultado = (String) storedProcedureQuery.getOutputParameterValue("produto_nome");
        
        assertEquals("Kindle", resultado);
        
        out.println(resultado);
    }
    
    @Test
    public void receberListaDeProcedure() {
        StoredProcedureQuery storedProcedureQuery =
                entityManager.createStoredProcedureQuery(
                        "compraram_acima_media", Cliente.class);
        
        storedProcedureQuery.registerStoredProcedureParameter(
                "ano", Integer.class, ParameterMode.IN);
        
        storedProcedureQuery.setParameter("ano", 2024);
        
        List<Cliente> resultado = storedProcedureQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r ->
                out.println("id = " + r.getId() +
                            ", cpf = " + r.getCpf() +
                            ", nome = " + r.getNome()));
    }
    
    @Test
    public void atualizarPrecoProdutoExercicio() {
        StoredProcedureQuery storedProcedureQuery =
                entityManager.createStoredProcedureQuery(
                        "ajustar_preco_produto");
        
        storedProcedureQuery.registerStoredProcedureParameter(
                "produto_id", Integer.class, ParameterMode.IN);
        
        storedProcedureQuery.registerStoredProcedureParameter(
                "percentual_ajuste", BigDecimal.class, ParameterMode.IN);
        
        storedProcedureQuery.registerStoredProcedureParameter(
                "preco_ajustado", BigDecimal.class, ParameterMode.OUT);
        
        storedProcedureQuery.setParameter("produto_id", 1);
        
        storedProcedureQuery.setParameter("percentual_ajuste", new BigDecimal("0.1"));
        
        BigDecimal resultado = (BigDecimal) storedProcedureQuery.getOutputParameterValue("preco_ajustado");
        
        assertEquals(new BigDecimal("878.9"), resultado);
        
        out.println(resultado);
    }
    
    @Test
    public void chamarNamedStoredProcedureQuery() {
        StoredProcedureQuery storedProcedureQuery =
                entityManager.createNamedStoredProcedureQuery("compraram_acima_media");
        
        storedProcedureQuery.setParameter("ano", 2024);
        
        List<Cliente> resultado = storedProcedureQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r ->
                                  out.println("id = " + r.getId() +
                                              ", cpf = " + r.getCpf() +
                                              ", nome = " + r.getNome()));
    }
}