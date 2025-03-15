package com.algaworks.ecommerce.mapeamentoavancado;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Atributo;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElementCollectionTest extends EntityManagerTest {
    
    @Test
    public void aplicarTags() {
        entityManager.getTransaction().begin();
        
        Produto produto = entityManager.find(Produto.class, 1);
        produto.setTags(List.of("ebook", "livro-digital"));
        
        entityManager.getTransaction().commit();
        entityManager.clear();
        
        Produto produtoVerificado = entityManager.find(Produto.class, produto.getId());
        assertEquals(2, produtoVerificado.getTags().size());
    }
    
    @Test
    public void aplicarAtributos() {
        entityManager.getTransaction().begin();
        
        Produto produto = entityManager.find(Produto.class, 1);
        produto.setTags(List.of("ebook", "livro-digital"));
        produto.setAtributos(List.of(new Atributo("cor", "vermelho"),
                                     new Atributo("peso","2.0kg" ),
                                     new Atributo("paginas", "200")));
        
        entityManager.getTransaction().commit();
        entityManager.clear();
        
        Produto produtoVerificado = entityManager.find(Produto.class, produto.getId());
        assertEquals(3, produtoVerificado.getAtributos().size());
    }
    
    @Test
    public void aplicarContatos() {
        entityManager.getTransaction().begin();
        
        Cliente cliente = entityManager.find(Cliente.class, 1);
        
        cliente.setContatos(Map.of("casa", "99999-9999",
                                   "trabalho", "88888-8888",
                                   "emergencia", "22222-2222",
                                   "email", "teste@gmail.com"));
        
        entityManager.getTransaction().commit();
        entityManager.clear();
        
        Cliente clienteVerificado = entityManager.find(Cliente.class, cliente.getId());
        Map<String, String> contatos = cliente.getContatos();
        
        assertEquals("99999-9999", contatos.get("casa"));
        assertEquals("88888-8888", contatos.get("trabalho"));
        assertEquals("22222-2222", contatos.get("emergencia"));
        assertEquals("teste@gmail.com", contatos.get("email"));
        assertEquals(4, clienteVerificado.getContatos().size());
        
    }
}