package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class PaginacaoJPQLTest extends EntityManagerTest {
    
    @Test
    public void paginarResultados() {
        String jpql = "select c from Categoria c order by c.nome";
        
        for(int i = 0; ; i+=2) {
            TypedQuery<Categoria> typedQuery = entityManager.createQuery(jpql, Categoria.class);
            typedQuery.setFirstResult(i);//0, 2, 4, 6, 8, ....
            // FIRST RESULT = MAX_RESULT * (pagina - 1), page é um pouco diferente aqui
            //Com pageable, page seria normal 0,1,2,3,4,5,6,...
            
            typedQuery.setMaxResults(2);//Tamanho da página.
            //Se não setar firstResult, ele maxResult vai definir o limite
            
            List<Categoria> categorias = typedQuery.getResultList();
            
            categorias.forEach(c -> System.out.println(c.getId() + ", " + c.getNome()));
            
            if(categorias.isEmpty()) {
                break;
            }
            
            assertFalse(categorias.isEmpty());
        }

    }
}