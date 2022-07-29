package com.av1.restapiav1.utils;

import com.av1.restapiav1.entities.Ativo;

import static java.lang.Thread.currentThread;

public class Cliente extends Thread{
    private Ativo ativoA, ativoB, ativoC, ativoD;
    private Corretora corretora;
    private long id;
    public double saldo;

    public Cliente(Corretora corretora, long id)
    {
        this.ativoA = corretora.ativoA;
        this.ativoB = corretora.ativoB;
        this.ativoC = corretora.ativoC;
        this.ativoD = corretora.ativoD;
        this.corretora = corretora;
        this.id = id;
        this.saldo = 100;
    }

    @Override
    public void run()//TODO: verificar cada ativo independente
    {
        try
        {
            Thread.sleep(500);
            int size = corretora.tamMml;
            ThreadAnalizeAtivo ativo1 = new ThreadAnalizeAtivo(ativoA,corretora, size, id, saldo);
            ThreadAnalizeAtivo ativo2 = new ThreadAnalizeAtivo(ativoB,corretora, size, id, saldo);
            ThreadAnalizeAtivo ativo3 = new ThreadAnalizeAtivo(ativoC,corretora, size, id, saldo);
            ThreadAnalizeAtivo ativo4 = new ThreadAnalizeAtivo(ativoD,corretora, size, id, saldo);

            Thread a1 = new Thread(ativo1);
            Thread a2 = new Thread(ativo2);
            Thread a3 = new Thread(ativo3);
            Thread a4 = new Thread(ativo4);

            a1.start();
            a2.start();
            a3.start();
            a4.start();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}


