package com.av1.restapiav1.service;

import com.av1.restapiav1.entities.Ativo;
import com.av1.restapiav1.repositories.CsvRepository;
import com.av1.restapiav1.utils.CotacaoExterna;
import com.av1.restapiav1.utils.Cliente;
import com.av1.restapiav1.utils.Corretora;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

        ArrayList<Cliente> listaClientes = new ArrayList<>();
        ArrayList<Thread> listaThreadsClientes = new ArrayList<>();

        Corretora corretora1 = new Corretora(ativo1, ativo2, ativo3, ativo4);
        CotacaoExterna atualizaTodosAtivos = new CotacaoExterna(corretora1, ativo1, ativo2, ativo3, ativo4);

        for (int i = 0; i < 10; i++)
        {
            listaClientes.add(new Cliente(corretora1, i));
        }

        Thread corretora = new Thread(corretora1);
        Thread att = new Thread(atualizaTodosAtivos);

        for (int i = 0; i < 10; i++)
        {
            listaThreadsClientes.add(new Thread(listaClientes.get(i)));
        }

        corretora.start();
        att.start();

        for (int i = 0; i < 10; i++)
        {
           listaThreadsClientes.get(i).start();
        }
    }

}
