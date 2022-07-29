package com.av1.restapiav1.utils;

import com.av1.restapiav1.entities.Ativo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Corretora extends Thread{

    public Ativo ativoA, ativoB, ativoC, ativoD;

    private Semaphore caixa1, caixa2;
    public static final int tamMml= 100; //média mais longa requer 100 valores
    public int numNegociacao;
    public static boolean fim = true;
    public ArrayList<String> timeOperacoes;
    public ArrayList<String> nomeAtivosOperacao;
    public ArrayList<Long> idCliente;
    public ArrayList<Double> qtdAtivo;
    public ArrayList<Character> compraOuVenda; //c para compra e v para venda



    public Corretora(Ativo ativoA, Ativo ativoB, Ativo ativoC,Ativo ativoD)
    {
        this.ativoA = ativoA;
        this.ativoB = ativoB;
        this.ativoC = ativoC;
        this.ativoD = ativoD;
        this.timeOperacoes = new ArrayList<>();
        this.nomeAtivosOperacao = new ArrayList<>();
        this.idCliente = new ArrayList<>();
        this.qtdAtivo = new ArrayList<>();
        this.compraOuVenda = new ArrayList<>();
        this.numNegociacao = 0;

        // inicializar os semáforos
        this.caixa1 = new Semaphore(1);
        this.caixa2 = new Semaphore(1);
    }

    @Override
    public synchronized void run() {

    }
    public void Caixas (String nomeAtivo, long idCiente, int quantidade, char CV, double valor) throws InterruptedException {
        if(caixa1.tryAcquire()){
            atualizaCompras(nomeAtivo, idCiente, quantidade, CV);
            numNegociacao++;
            System.out.println (" id Cliente: " + idCiente + " tipo de operacao: " +
                                CV + " ativo: " + nomeAtivo+" quantia de ativo: "+ quantidade +
                                " preço de C/V: " + new DecimalFormat("#,##00.00").format(valor) +
                                " no CAIXA 1");
            caixa1.release();
        }
        else
        {
            if(caixa2.tryAcquire()){
                atualizaCompras(nomeAtivo, idCiente, quantidade, CV);
                numNegociacao++;
                System.out.println (" id Cliente: " + idCiente + " tipo de operacao: " +
                        CV + " ativo: " + nomeAtivo+" quantia de ativo: "+ quantidade +
                        " preço de C/V: " + new DecimalFormat("#,##00.00").format(valor) +
                        " no CAIXA 2");
                caixa2.release();
            }
        }
    }

    public void atualizaCompras(String nomeAtivo, long idCiente, int quantidade, char CV)
    {
        if (CV == 'v')
        {
            int passo = ativoA.dataTime.size()-1;

            timeOperacoes.add(ativoA.dataTime.get(passo));
            nomeAtivosOperacao.add(nomeAtivo);
            this.idCliente.add(idCiente);
            this.qtdAtivo.add((double) 0);
            this.compraOuVenda.add(CV);
        }
        else
        {
            int passo = ativoA.dataTime.size()-1;

            timeOperacoes.add(ativoA.dataTime.get(passo));
            nomeAtivosOperacao.add(nomeAtivo);
            this.idCliente.add(idCiente);
            this.qtdAtivo.add(quantidade*ativoA.valores.get(passo));
            this.compraOuVenda.add(CV);
        }
    }
}
