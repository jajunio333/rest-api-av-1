package com.av1.restapiav1.utils;

import com.av1.restapiav1.entities.Ativo;

import java.text.DecimalFormat;

public class Cliente extends Thread{
    private Ativo ativoA, ativoB, ativoC, ativoD;
    private Corretora corretora;
    private long id;
    public GerenciadorSaldo gerenciadorSaldo;

    public Cliente(Corretora corretora, long id)
    {
        this.ativoA = corretora.ativoA;
        this.ativoB = corretora.ativoB;
        this.ativoC = corretora.ativoC;
        this.ativoD = corretora.ativoD;
        this.corretora = corretora;
        this.id = id;
        this.gerenciadorSaldo = new GerenciadorSaldo(100);
    }

    @Override
    public synchronized void run()
    {
        try
        {
            Thread.sleep(500);
            int size = corretora.tamMml;
            ThreadAnalizeAtivo ativo1 = new ThreadAnalizeAtivo(ativoA,corretora, size, id, gerenciadorSaldo);
            ThreadAnalizeAtivo ativo2 = new ThreadAnalizeAtivo(ativoB,corretora, size, id, gerenciadorSaldo);
            ThreadAnalizeAtivo ativo3 = new ThreadAnalizeAtivo(ativoC,corretora, size, id, gerenciadorSaldo);
            ThreadAnalizeAtivo ativo4 = new ThreadAnalizeAtivo(ativoD,corretora, size, id, gerenciadorSaldo);

            Thread a1 = new Thread(ativo1);
            Thread a2 = new Thread(ativo2);
            Thread a3 = new Thread(ativo3);
            Thread a4 = new Thread(ativo4);

            a1.start();
            a2.start();
            a3.start();
            a4.start();

            VerificaFim(ativo1, ativo2, ativo3, ativo4);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    public void VerificaFim(ThreadAnalizeAtivo ativo1, ThreadAnalizeAtivo ativo2, ThreadAnalizeAtivo ativo3, ThreadAnalizeAtivo ativo4) throws InterruptedException {
        if(!this.corretora.fim)
        {
            double saldo = gerenciadorSaldo.GetSaldo();

            if (saldo > 0) // filtro para verificar se conseguiu obter o saldo correto
            {
                double balancAt1 = ativo1.qtdAtivo * ativo1.ativo.valores.get(ativo1.ativo.valores.size()-1);
                double balancAt2 = ativo2.qtdAtivo * ativo2.ativo.valores.get(ativo2.ativo.valores.size()-1);
                double balancAt3 = ativo3.qtdAtivo * ativo3.ativo.valores.get(ativo3.ativo.valores.size()-1);
                double balancAt4 = ativo4.qtdAtivo * ativo4.ativo.valores.get(ativo4.ativo.valores.size()-1);
                saldo = saldo + balancAt1 + balancAt2 + balancAt3 + balancAt4;

                System.out.println( "Saldo final cliente id "
                                    + this.id + ": "
                                    + new DecimalFormat("#,##000.000").format(saldo));
                Imprime(ativo1, balancAt1);
                Imprime(ativo2, balancAt2);
                Imprime(ativo3, balancAt3);
                Imprime(ativo4, balancAt4);
                this.interrupt();
            }
            else
            {
                Thread.sleep(10);
                VerificaFim(ativo1, ativo2, ativo3, ativo4);
            }
        }
        else
        {
            Thread.sleep(500);
            VerificaFim(ativo1, ativo2, ativo3, ativo4);// recursão para verificar fim da corretora
        }
    }

    private void Imprime (ThreadAnalizeAtivo ativoAT, double balancoAtivo)
    {
        if ( ativoAT.qtdAtivo > 0)
        {
            System.out.println("Saldo final cliente id "
                    + ativoAT.idCiente + ": "
                    + " no ativo "
                    + ativoAT.ativo.getNome()
                    + ": "
                    + new DecimalFormat("#,##000.000").format(balancoAtivo));
        }
    }
}


