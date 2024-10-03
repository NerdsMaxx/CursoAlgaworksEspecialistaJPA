package com.algaworks.ecommerce.mapeamentoavancado;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.NotaFiscal;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SalvandoArquivosTest extends EntityManagerTest {
    
    @Test
    public void salvarXmlNota() {
        Pedido pedido = entityManager.find(Pedido.class, 1);
        
        NotaFiscal notaFiscal = new NotaFiscal();
        notaFiscal.setPedido(pedido);
        notaFiscal.setDataEmissao(new Date());
        notaFiscal.setXml(carregarNotaFiscal());
        
        entityManager.getTransaction().begin();
        entityManager.persist(notaFiscal);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        NotaFiscal notaFiscalVerificado = entityManager.find(NotaFiscal.class, notaFiscal.getId());
        assertNotNull(notaFiscalVerificado.getXml());
        assertTrue(notaFiscalVerificado.getXml().length > 0);
        
//        escreverArquivo(notaFiscalVerificado);
    }
    
    @Test
    public void salvarFotoProduto() {
        Produto produto = entityManager.find(Produto.class, 1);
        
        produto.setFoto(carregarFoto());
        
        entityManager.getTransaction().begin();
        entityManager.persist(produto);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Produto produtoVerificado = entityManager.find(Produto.class, produto.getId());
        assertNotNull(produto.getFoto());
        assertTrue(produto.getFoto().length > 0);
        
        escreverFoto(produtoVerificado);
    }
    
    private byte[] carregarNotaFiscal() {
        try {
            return SalvandoArquivosTest.class.getResourceAsStream("/nota-fiscal.xml").readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private byte[] carregarFoto() {
        try {
            return SalvandoArquivosTest.class.getResourceAsStream("/foto.jpg").readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void escreverArquivo(NotaFiscal notaFiscalVerificado) {
        try {
            OutputStream out = new FileOutputStream(Files.createFile(Paths.get(
                    System.getProperty("user.home") + "/xml.xml")).toFile());
            out.write(notaFiscalVerificado.getXml());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private void escreverFoto(Produto produtoVerificado) {
        try {
            OutputStream out = new FileOutputStream(Files.createFile(Paths.get(
                    System.getProperty("user.home") + "/foto.jpg")).toFile());
            out.write(produtoVerificado.getFoto());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}