package com.algaworks.ecommerce.detalhesimportantes;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.System.out;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class OneToOneLazyTest extends EntityManagerTest {
    
    //Olhe comentários abaixos!!!!!!!
//    @Test
//    public void mostrarProblema() {
//        out.println("BUSCANDO UM PEDIDO: ");
//        Pedido pedido = entityManager.find(Pedido.class, 1);
//        assertNotNull(pedido);
//
//        out.println("-".repeat(30));
//
//        out.println("BUSCANDO UMA LISTA DE PEDIDOS:");
//        List<Pedido> lista = entityManager
//                .createQuery("select p from Pedido p", Pedido.class)
//                .getResultList();
//
//        assertFalse(lista.isEmpty());
//    }

//    Infelizmente, a solução adotada pelo instrutor na versão 5 do Hibernate não tem mais efeito na versão 6.
//
//    A versão 6 trouxe algumas formalidades/encapsulamentos que tornaram algumas classes um pouco mais "fechadas" e, por consequência, trouxe também alguns comportamentos os quais não são simples de encontrar workarounds.
//
//    Isso posto, a solução que entemdemos ser a melhor para o caso é assumir que de todo modo será realizado um fetch para as entidades
//     Pagamento e NotaFiscal e, sendo assim, podemos manipular nosso JPQL para trazer os dados em uma só consulta:
    
    @Test
    public void mostrarProblema() {
        System.out.println("BUSCANDO UM PEDIDO:");
        Pedido pedido = entityManager
                .createQuery("select p from Pedido p " +
                             "left join fetch p.pagamento " +
                             "left join fetch p.cliente " +
                             "left join fetch p.notaFiscal " +
                             "where p.id = 1", Pedido.class)
                .getSingleResult();
        Assertions.assertNotNull(pedido);
        
        System.out.println("----------------------------------------------------");
        
        System.out.println("BUSCANDO UMA LISTA DE PEDIDOS:");
        List<Pedido> lista = entityManager
                .createQuery("select p from Pedido p " +
                             "left join fetch p.pagamento " +
                             "left join fetch p.cliente " +
                             "left join fetch p.notaFiscal", Pedido.class)
                .getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

//    Na próxima aula, será visto uma outra forma de trazer somente os atributos desejados, respeitando os FetchTypes, através do EntityGraph, mas, sem o uso de tal abordagem, acabamos por ter que usar a forma citada acima.


}