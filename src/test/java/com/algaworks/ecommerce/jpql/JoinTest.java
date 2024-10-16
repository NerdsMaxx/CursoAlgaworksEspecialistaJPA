package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JoinTest extends EntityManagerTest {

    @Test
    public void fazerJoin() {
//        String jpql = "select p, pag from Pedido p join p.pagamento pag";
//        Teria q ser Object[]
        
//        String jpql = "select p, pag from Pedido p join p.pagamento pag where pag.status = 'PROCESSANDO'";

//        String jpql = "select p from Pedido p join p.itemPedidos i where i.precoProduto > 10";
        
        String jpql = "select p from Pedido p join p.pagamento pag"; //como tem join (inner), ele vai trazer somente pedidos que tenham pagamento registrado
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        
        List<Pedido> pedidos = typedQuery.getResultList();
        assertEquals(1, pedidos.size());
        
//        String jpql = "select p, i from Pedido p join p.itemPedidos";
//        Se pedido tiver 2 ou mais itens, vai trazer o mesmo pedido com itens diferentes, então pode repetir o pedido

//        String jpql = "select p, i, pag from Pedido p join p.itemPedidos i join p.pagamento pag";
        
        String jpql1 = "select p from Pedido p join p.itemPedidos"; //mesmo que só tenha pedido para ser retornada, ele repete tmb
        
        TypedQuery<Pedido> typedQuery1 = entityManager.createQuery(jpql1, Pedido.class);
        
        List<Pedido> pedidos1 = typedQuery1.getResultList();
        assertEquals(2, pedidos1.size());
    }
    
    @Test
    public void fazerLeftJoin() {
        String jpql = "select p from Pedido p left join p.pagamento pag";
        //left join = left outer join
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        
        List<Pedido> pedidos = typedQuery.getResultList();
        assertEquals(2, pedidos.size());
        
//        Caso pedido não tenha pagamento, ele não vai filtrar pelo pag.status = 'PROCESSANDO'. ON traz mais resultados, pois traz pedido que não tem pagamento tmb.
        jpql = "select p from Pedido p left join p.pagamento pag on pag.status = 'PROCESSANDO'";
        typedQuery = entityManager.createQuery(jpql, Pedido.class);
        
        pedidos = typedQuery.getResultList();
        assertEquals(2, pedidos.size());
        
//        Ele vai filtrar somente pedidos que tenham pagamento e status dele é 'PROCESSANDO'. WHERE traz menos resultados.
        jpql = "select p from Pedido p left join p.pagamento pag where pag.status = 'PROCESSANDO'";
        typedQuery = entityManager.createQuery(jpql, Pedido.class);
        
        pedidos = typedQuery.getResultList();
        assertEquals(1, pedidos.size());
    }
    
    @Test
    public void usarJoinFetch() {
        String jpql = "select p from Pedido p " +
                      "left join fetch p.itemPedidos ip " +
                      "join fetch p.cliente cli " +
                      "left join fetch p.notaFiscal nf " +
                      "where p.id = 1";
//        join fetch já vai carregar o objeto relacionado já na memória caso seja marcado como LAZY
//        Tmb funciona com left e right
//        Ele resolve o problema N+1, pois ele já carrega tudo o que é necessário na memória
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        
        Pedido pedido = typedQuery.getSingleResult();
        assertFalse(pedido.getItemPedidos().isEmpty());
    }
    
}