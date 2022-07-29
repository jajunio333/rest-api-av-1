package com.av1.restapiav1.service;

import com.av1.restapiav1.entities.Ativo;
import com.av1.restapiav1.repositories.CsvRepository;
import com.av1.restapiav1.utils.AtualizaTodosAtivos;
import com.av1.restapiav1.utils.Cliente;
import com.av1.restapiav1.utils.Corretora;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    private CsvRepository csvRepository;

    public TradeService()
    {
        this.csvRepository = csvRepository;
    }


    public void Iniciar () //método para gerenciar a simulação de trades
    {
        Ativo ativo1 = new Ativo("ativoA");
        Ativo ativo2 = new Ativo("ativoB");
        Ativo ativo3 = new Ativo("ativoC");
        Ativo ativo4 = new Ativo("ativoD");
        Corretora corretora1 = new Corretora(ativo1, ativo2, ativo3, ativo4);
        AtualizaTodosAtivos atualizaTodosAtivos = new AtualizaTodosAtivos(corretora1, ativo1, ativo2, ativo3, ativo4);
        Cliente cliente0 = new Cliente(corretora1,1);
        Cliente cliente1 = new Cliente(corretora1,2);
        Cliente cliente2 = new Cliente(corretora1,3);
        Cliente cliente3 = new Cliente(corretora1,4);
        Cliente cliente4 = new Cliente(corretora1,5);
        Cliente cliente5 = new Cliente(corretora1,6);

        Thread corretora = new Thread(corretora1);
        Thread att = new Thread(atualizaTodosAtivos);
        Thread c2 = new Thread(cliente0);
        Thread c3 = new Thread(cliente1);
        Thread c4 = new Thread(cliente2);
        Thread c5 = new Thread(cliente3);
        Thread c6 = new Thread(cliente4);
        Thread c7 = new Thread(cliente5);

        corretora.start();
        att.start();

        c2.start();
        c3.start();
        c4.start();
        c5.start();
        c6.start();
        c7.start();


    }

}
