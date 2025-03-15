package com.algaworks.ecommerce.detalhesimportantes;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Cliente_;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Pedido_;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Subgraph;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EntityGraphTest extends EntityManagerTest {
    
    @Test
    public void buscarAtributosEssenciaisDePedidoComFetchGraph() {
        
        //NOTA DA AULA
        /*
         Sobre as propriedades javax.persistence.fetchgraph e javax.persistence.loadgraph, podemos alterá-las agora para jakarta.persistence.fetchgraph e jakarta.persistence.loadgraph.

        Sobre o problema citado no minuto 09:55 da aula, sobre o EntityGraph trazer o Cliente quando não está no array de AttrbuteNodes, esse problema foi corrigido, logo, não somente o Cliente mas o Pagamento e a NotaFiscal não necessariamente precisariam estar LAZY pois, mesmo se forem EAGER o que será respeitado será a composição dos atributos listados no EntityGraph.
        
        Além dessa correção, também não há mais necessidade de colocar qualquer código referente ao PersistentAttributeInterceptable, assim como o @LazyToOne para trabalhar com o EntityGraph, logo, para que o EntityGraph funcione com os comportamentos mostrados na aula, não precisamos nos preocupar em manter no código nenhuma tratativa em relação ao PersistentAttributeInterceptable.
         */
        
        //Com a propriedade "jakarta.persistence.fetchgraph".
        //Ele só vai trazer o que vc precisar, vai deixar o resto
        //como LAZY, mesmo que seja marcado como EAGER.
        //O resto que eu falo são relacionamentos com outras entidades
        //marcado @OneToOne, @OneToMany, @ManyToOne ou @ManyToMany.
        //Antigamente precisava de workaround para funcionar,
        //Mas a nova versão corrigiu problemas, agora está OK.
        
//        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
//
//        entityGraph.addAttributeNodes("dataCriacao", "status",
//                                      "total", "cliente");
//
//        Map<String,Object> properties = Map.of("jakarta.persistence.fetchgraph", entityGraph);
//
//        Pedido pedido = entityManager.find(Pedido.class, 1, properties);
//        assertNotNull(pedido);
        
        //Outra maneira
        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
        entityGraph.addAttributeNodes("dataCriacao", "status",
                                      "total", "cliente");
                                      
        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery("select p from Pedido p", Pedido.class);
        typedQuery.setHint("jakarta.persistence.fetchgraph", entityGraph);
        List<Pedido> lista = typedQuery.getResultList();
        assertFalse(lista.isEmpty());
    }
    
    @Test
    public void buscarAtributosEssenciaisDePedidoComLoadGraph() {
        
        
        //Com a propriedade "jakarta.persistence.loadgraph".
        //Ele vai trazer o que vc precisar, mas o resto ele
        //vai deixar como está configurado.
        //Por exemplo:
        //se está marcado como EAGER, ele vai trazer,
        //se está como LAZY, não vai trazer direto.
        //Esta propriedade é diferente "jakarta.persistence.fetchgraph".
        
        //AVISO:
        //@OneToOne como LAZY não está funcionando, ele acaba fazendo mais
        //consultas do que deveria, então para resolver problema de N+1,
        //o jeito é colocar de forma explicita "notaFiscal" e "pagamentos"
        //na lista do Attribute Nodes.
//        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
//
//        entityGraph.addAttributeNodes("dataCriacao", "status",
//                                      "total", "cliente");
//
//        Map<String,Object> properties = Map.of("jakarta.persistence.loadgraph", entityGraph);
//
//        Pedido pedido = entityManager.find(Pedido.class, 1, properties);
//        assertNotNull(pedido);
        
        //Outra maneira
        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
        entityGraph.addAttributeNodes("dataCriacao", "status",
                                      "total", "cliente");
        
        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery("select p from Pedido p", Pedido.class);
        typedQuery.setHint("jakarta.persistence.loadgraph", entityGraph);
        List<Pedido> lista = typedQuery.getResultList();
        assertFalse(lista.isEmpty());
    }
    
    //OLHE O Q ESTÁ DOCUMENTADO NOS PRIMEIROS MÉTODOS
    @Test
    public void buscarAtributosEssenciaisDePedidoComSubGraph() {
        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
        entityGraph.addAttributeNodes("dataCriacao", "status", "total");
        
        Subgraph<Cliente> subgraphCliente = entityGraph.addSubgraph("cliente", Cliente.class);
        subgraphCliente.addAttributeNodes("nome", "cpf");
        
        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery("select p from Pedido p", Pedido.class);
        typedQuery.setHint("jakarta.persistence.fetchgraph", entityGraph);
        List<Pedido> lista = typedQuery.getResultList();
        assertFalse(lista.isEmpty());
    }
    
    //OLHE O Q ESTÁ DOCUMENTADO NOS PRIMEIROS MÉTODOS
    @Test
    public void buscarAtributosEssenciaisDePedidoComMetamodel() {
        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
        entityGraph.addAttributeNodes(Pedido_.dataCriacao, Pedido_.status, Pedido_.total);
        
        Subgraph<Cliente> subgraphCliente = entityGraph.addSubgraph("cliente");
        subgraphCliente.addAttributeNodes(Cliente_.nome, Cliente_.cpf);
        
        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery("select p from Pedido p", Pedido.class);
        typedQuery.setHint("jakarta.persistence.fetchgraph", entityGraph);
        List<Pedido> lista = typedQuery.getResultList();
        assertFalse(lista.isEmpty());
    }
    
    //OLHE O Q ESTÁ DOCUMENTADO NOS PRIMEIROS MÉTODOS
    @Test
    public void buscarAtributosEssenciaisDePedidoComNamedEntityGraph() {
        EntityGraph<?> entityGraph = entityManager.createEntityGraph("Pedido.dadosEssenciais");
        
//        entityGraph.addAttributeNodes("pagamento");
        entityGraph.addSubgraph("pagamento").addAttributeNodes("status");
        
        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery("select p from Pedido p", Pedido.class);
        typedQuery.setHint("jakarta.persistence.fetchgraph", entityGraph);
        List<Pedido> lista = typedQuery.getResultList();
        assertFalse(lista.isEmpty());
    }
}