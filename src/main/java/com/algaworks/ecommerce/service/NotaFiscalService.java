package com.algaworks.ecommerce.service;

import com.algaworks.ecommerce.model.Pedido;

import static java.lang.System.out;

public class NotaFiscalService {
    public void gerar(Pedido pedido) {
        out.println("Gerando a nota fiscal do pedido!");
    }
}