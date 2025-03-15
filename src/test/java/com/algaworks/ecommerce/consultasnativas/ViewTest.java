package com.algaworks.ecommerce.consultasnativas;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;


import java.util.List;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ViewTest extends EntityManagerTest {
    
    @Test
    public void executarView() {
        Query query = entityManager.createNativeQuery(
                "select cli.id, cli.nome, sum(ped.total) " +
                " from pedido ped " +
                " join view_clientes_acima_media cli on cli.id = ped.cliente_id " +
                " group by ped.cliente_id");
        
        List<Object[]> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(
                String.format("id = %s, nome = %s, total = %s", r)));
    }
    
    @Test
    public void executarViewRetornandoCliente() {
        
        //pode imaginar view como tabela, então as aulas anteriores
        // de consultas nativas serve para view também
        Query query = entityManager.createNativeQuery(
                "select * from view_clientes_acima_media",
                Cliente.class);
        
        List<Cliente> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        
        resultado.forEach(r -> out.println("id = " + r.getId() +
                                           ", cpf = " + r.getCpf() +
                                           ", nome = " + r.getNome()));
    }
}