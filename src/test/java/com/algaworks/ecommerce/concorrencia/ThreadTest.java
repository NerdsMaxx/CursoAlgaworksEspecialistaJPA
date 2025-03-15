package com.algaworks.ecommerce.concorrencia;

import com.algaworks.ecommerce.EntityManagerTest;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static java.lang.System.out;

public class ThreadTest extends EntityManagerTest {
    
    @Test
    public void entenderThreads() throws InterruptedException {
        Runnable runnable1 = () -> {
            out.println(Thread.currentThread().getId() + " Runnable 01 vai esperar 3 segundos.");
            esperar(3);
            out.println(Thread.currentThread().getId() + " Runnable 01 concluído.");
        };
        
        Runnable runnable2 = () -> {
            out.println(Thread.currentThread().getId() + " Runnable 02 vai esperar 1 segundo.");
            esperar(1);
            out.println(Thread.currentThread().getId() + " Runnable 02 concluído.");
        };
        
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        out.println(Thread.currentThread().getId() + " Encerrando método de teste. [PRINCIPAL]");
    }
}