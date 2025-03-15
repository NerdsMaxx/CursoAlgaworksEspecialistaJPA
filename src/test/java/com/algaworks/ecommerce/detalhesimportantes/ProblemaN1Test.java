package com.algaworks.ecommerce.detalhesimportantes;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.EntityGraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProblemaN1Test extends EntityManagerTest {
    
    @Test
    public void problemaNmais1() {
        
        //Ela acaba fazendo consulta da nota fiscal e pagamento para cada pedido.
        //Se tiver 6 pedidos, então vai ter 6 consultas para nota fiscal e
        //6 consultas para pagamento.
        //Obs.: Só é gerado uma consulta para pedidos, ou seja, busca de uma vez.
        //Mas quando é nota fiscal ou pagamento, a coisa é diferente.
        //consulta = SQL
        List<Pedido> lista = entityManager
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();
        
        assertFalse(lista.isEmpty());
    }
    
    @Test
    public void resolverComFetch() {
        //Com esta solução, gera só uma consulta (um SQL)
        List<Pedido> lista = entityManager
                .createQuery("select p from Pedido p " +
                             "join fetch p.cliente c " +
                             "join fetch p.pagamento pag " +
                             "join fetch p.notaFiscal nf", Pedido.class)
                .getResultList();
        
        assertFalse(lista.isEmpty());
    }
    
    @Test
    public void resolverComEntityGraph() {
        //Com esta solução, gera só uma consulta (um SQL)
        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
        
        entityGraph.addAttributeNodes("cliente", "notaFiscal", "pagamento");
        
        List<Pedido> lista = entityManager
                .createQuery("select p from Pedido p", Pedido.class)
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
        
        assertFalse(lista.isEmpty());
    }
}