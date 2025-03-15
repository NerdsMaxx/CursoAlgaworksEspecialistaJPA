package com.algaworks.ecommerce.mapeamentoavancado;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropriedadesTransientesTest extends EntityManagerTest {

    @Test
    public void validarPrimeiroNome() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        assertEquals("Guilherme", cliente.getPrimeiroNome());
    }
}