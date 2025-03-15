package com.algaworks.ecommerce.consultasnativas;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.dto.CategoriaDto;
import com.algaworks.ecommerce.dto.ProdutoDto;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ConsultaNativaTest extends EntityManagerTest {
    
    @Test
    public void executarSQLArrayObject() {
        String sql = "select id, nome, preco from produto";
        Query query = entityManager.createNativeQuery(sql);
        
        List<Object[]> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r[0] +
                        ", nome = " + r[1] +
                        ", preco = " + r[2]));
    }
    
    @Test
    public void executarSQLRetornandoEntidadeProduto01() {
        String sql = "select * from produto";
        Query query = entityManager.createNativeQuery(sql, Produto.class);
        
        List<Produto> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome() +
                        ", preco = " + r.getPreco()));
    }
    
    @Test
    public void executarSQLRetornandoEntidadeProduto02() {
        //Se ficar faltando uma coluna, por exemplo, a coluna "nome" vai dar erro.
        //Precisa estar todas as colunas presentes menos as relações One-To-Many e Many-To-Many.
        //A relação One-To-One pode ficar de fora caso a entidade não seja onwer da relação (dono da relação).
        //Agora o restante dos casos precisa estar presente na consulta, ou melhor, TODAS AS CHAVES ESTRANGEIRAS
        //QUE ESTÃO NA TABELA, PRECISA ESTAR PRESENTE NA CONSULTA.
        //
        //Erro:
        // nov. 11, 2024 10:53:13 PM org.hibernate.engine.jdbc.spi.SqlExceptionHelper logExceptions
        //WARN: SQL Error: 0, SQLState: S0022
        //nov. 11, 2024 10:53:13 PM org.hibernate.engine.jdbc.spi.SqlExceptionHelper logExceptions
        //ERROR: Column 'nome' not found.
        //
        //org.hibernate.exception.SQLGrammarException: Unable to find column position by name: nome [Column 'nome' not found.] [n/a]
//        String sql = "select id, descricao, data_criacao, data_ultima_atualizacao, preco, foto " +
//                     "from produto";
        
        String sql = "select id, nome, descricao, data_criacao, data_ultima_atualizacao, preco, foto " +
                     "from produto";
        Query query = entityManager.createNativeQuery(sql, Produto.class);
        
        List<Produto> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome() +
                        ", preco = " + r.getPreco()));
    }
    
    @Test
    public void executarSQLRetornandoEntidadeProduto03() {
        //A tabela produto_loja é diferente da tabela produto, porém eles contém as mesmas colunas, por isso dá certo.
        //Se quiser mais detalhes: olhe o método executarSQLRetornandoEntidadeProduto02
        
        String sql = "select id, nome, descricao, data_criacao, data_ultima_atualizacao, preco, foto " +
                     "from produto_loja";
        Query query = entityManager.createNativeQuery(sql, Produto.class);
        
        List<Produto> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome() +
                        ", preco = " + r.getPreco()));
    }
    
    @Test
    public void executarSQLRetornandoEntidadeProduto04() {
        //A tabela ecm_produto é diferente da tabela produto e ele contém colunas com nomes parecidos mas diferente.
        //Se tentar executar com nomes diferentes vai dar erro, pois JPA não acha coluna com nome igual.
        //Por isso tem que colocar ALIAS para orientar JPA, ai sim dá certo.
        //Se quiser mais detalhes: olhe o método executarSQLRetornandoEntidadeProduto02
        
        String sql = "select prd_id id, prd_nome nome, prd_descricao descricao, prd_data_criacao data_criacao, " +
                     "prd_data_ultima_atualizacao data_ultima_atualizacao, prd_preco preco, prd_foto foto " +
                     "from ecm_produto";
        Query query = entityManager.createNativeQuery(sql, Produto.class);
        
        List<Produto> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome() +
                        ", preco = " + r.getPreco()));
    }
    
    @Test
    public void executarSQLRetornandoEntidadeProduto05() {
        //A tabela erp_produto é diferente da tabela produto e contém menos colunas que tabela produto.
        //A tabela erp_produto não têm as colunas data_criacao, data_ultima_atualizacao e foto.
        //Para resolver isso, basta colocar null junto com ALIAS. As colunas que não tem  na entidade original vão vir com valor nulo.
        //Se quiser mais detalhes: olhe o método executarSQLRetornandoEntidadeProduto02
        
        String sql = "select id, nome, descricao, null data_criacao, null data_ultima_atualizacao, preco, null foto " +
                     "from erp_produto";
        Query query = entityManager.createNativeQuery(sql, Produto.class);
        
        List<Produto> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome() +
                        ", preco = " + r.getPreco() +
                        ", dataCriacao = " + r.getDataCriacao() +
                        ", dataUltimaAtualizacao = " + r.getDataUltimaAtualizacao() +
                        ", foto = " + Arrays.toString(r.getFoto())));
    }
    
    
    @Test
    public void passarParametros() {
        //Se quiser mais detalhes: olhe o método executarSQLRetornandoEntidadeProduto04
        
        String sql = "select prd_id id, prd_nome nome, prd_descricao descricao, prd_data_criacao data_criacao, " +
                     "prd_data_ultima_atualizacao data_ultima_atualizacao, prd_preco preco, prd_foto foto " +
                     "from ecm_produto " +
                     "where prd_id = :id ";
        Query query = entityManager.createNativeQuery(sql, Produto.class);
        query.setParameter("id", 201);
        
        List<Produto> resultado = query.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome() +
                        ", preco = " + r.getPreco()));
    }
    
    
    @Test
    public void usarSQLResultSetMapping01() {
        
        String sql = "select id, nome, descricao, data_criacao, data_ultima_atualizacao, preco, foto " +
                     "from produto_loja";
        Query query = entityManager.createNativeQuery(sql, "produto_loja.Produto");
        
        List<Produto> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome() +
                        ", preco = " + r.getPreco() +
                        ", dataCriacao = " + r.getDataCriacao() +
                        ", dataUltimaAtualizacao = " + r.getDataUltimaAtualizacao() +
                        ", foto = " + Arrays.toString(r.getFoto())));
    }
    
    @Test
    public void usarSQLResultSetMapping02() {
        
        String sql = "select ip.*, p.* " +
                     "from item_pedido ip join produto p on p.id = ip.produto_id";
        Query query = entityManager.createNativeQuery(
                sql,
                "item_produto-produto.ItemProduto-Produto");
        
        List<Object[]> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        for (Object[] r : resultado) {
            for(Object o : r) {
                if(o instanceof Produto p) {
                    out.println(
                            "Produto -> id = " + p.getId() +
                            ", nome = " + p.getNome() +
                            ", preco = " + p.getPreco() +
                            ", dataCriacao = " + p.getDataCriacao() +
                            ", dataUltimaAtualizacao = " + p.getDataUltimaAtualizacao() +
                            ", foto = " + Arrays.toString(p.getFoto()));
                }
                else if(o instanceof ItemPedido ip) {
                    out.println(
                            "ItemPedido -> produtoId = " + ip.getId().getProdutoId() +
                            ", pedidoId = " + ip.getId().getPedidoId() +
                            ", precoProduto = " + ip.getPrecoProduto() +
                            ", quantidade = " + ip.getQuantidade()
                            );
                }
            }
            
            out.println();
        }
    }
    
    @Test
    public void usarFieldResult() {
        String sql = "select * from ecm_produto";
        
        Query query = entityManager.createNativeQuery(sql, "ecm_produto.Produto");
        
        List<Produto> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome() +
                        ", preco = " + r.getPreco()));
    }
    
    @Test
    public void usarColumnResultRetornarDTO() {
        String sql = "select * from ecm_produto";
        
        Query query = entityManager.createNativeQuery(sql, "ecm_produto.ProdutoDTO");
        
        List<ProdutoDto> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome()));
    }
    
    @Test
    public void usarUmaNamedNativeQuery01() {
        //Não vai dar muito certo o segundo parâmetro, por isso
        //tem q colocar Produto.class na propriedade resultClass
        //na anotação @NamedNativeQuery
//        Query query = entityManager.createNamedQuery(
//                "produto_loja.listar", Produto.class);
        Query query = entityManager.createNamedQuery(
                "produto_loja.listar");
        
        List<Produto> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome() +
                        ", preco = " + r.getPreco() +
                        ", dataCriacao = " + r.getDataCriacao() +
                        ", dataUltimaAtualizacao = " + r.getDataUltimaAtualizacao() +
                        ", foto = " + Arrays.toString(r.getFoto())));
    }
    
    @Test
    public void usarUmaNamedNativeQuery02() {
        Query query = entityManager.createNamedQuery(
                "ecm_produto.listar");
        
        List<Produto> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome() +
                        ", preco = " + r.getPreco() +
                        ", dataCriacao = " + r.getDataCriacao() +
                        ", dataUltimaAtualizacao = " + r.getDataUltimaAtualizacao() +
                        ", foto = " + Arrays.toString(r.getFoto())));
    }
    
    @Test
    public void usarArquivoXML() {
        Query query = entityManager.createNamedQuery("ecm_categoria.listar");
        
        List<Categoria> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome()));
    }
    
    @Test
    public void mapearConsultaParaDTOEmArquivoExternoExercicio() {
        Query query = entityManager.createNamedQuery("ecm_categoria.listar.dto");
        
        List<CategoriaDto> resultado = query.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(
                r -> out.println(
                        "id = " + r.getId() +
                        ", nome = " + r.getNome()));
    }
}