package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FlushTest extends EntityManagerTest {
    
    @Test
    public void testChamarFlush() {
        Assertions.assertThrows(Exception.class, this::chamarFlush);
    }
    
    private void chamarFlush() {
        try {
            entityManager.getTransaction().begin();
            
            Pedido pedido = entityManager.find(Pedido.class, 1);
            pedido.setStatus(StatusPedido.PAGO);
            
            entityManager.flush(); //NÃO É MUITO USADO, MELHOR EVITAR, MAS PODE SER USADO EM UM CASO MUITO ESPECÍFICO
            
//            entityManager.clear(); //Poderia salvar antes de limpar a memória
            
            if(pedido.getPagamento() == null) {
                throw new RuntimeException("Pedido ainda não foi pago.");
            }
            
            Pedido pedido1 = entityManager
                    .createQuery("select p from Pedido p where p.id = 1", Pedido.class)
                    .getSingleResult();
            
            entityManager.getTransaction().commit();
        }
        catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
}