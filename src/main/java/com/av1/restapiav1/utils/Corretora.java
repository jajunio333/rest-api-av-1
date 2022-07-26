package com.av1.restapiav1.utils;

import com.av1.restapiav1.entities.Ativo;
import com.av1.restapiav1.utils.RD.Rec;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Corretora extends Thread{

    public Ativo ativoA, ativoB, ativoC, ativoD;
    public Rec rec;
    private Semaphore caixa1, caixa2;
    public static final int tamMml= 100; //média mais longa requer 100 valores
    public int numNegociacao;
    public static boolean fim = true;
    public ArrayList<String> timeOperacoes;
    public ArrayList<String> nomeAtivosOperacao;
    public ArrayList<Long> idCliente;
    public ArrayList<Double> qtdAtivoEntrada;
    public ArrayList<Double> qtdAtivoSaida;
    public ArrayList<Double> qtdAtivoSaidaRec;
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
        this.qtdAtivoEntrada = new ArrayList<>();
        this.qtdAtivoSaida = new ArrayList<>();
        this.compraOuVenda = new ArrayList<>();
        this.qtdAtivoSaidaRec = new ArrayList<>();
        this.numNegociacao = 0;
        this.rec = new Rec();

        // inicializar os semáforos
        this.caixa1 = new Semaphore(1);
        this.caixa2 = new Semaphore(1);
    }

    @Override
    public synchronized void run() {
        try {
            System.out.println("*******************");
            System.out.println("Iniciando corretora");
            System.out.println("*******************");
            Thread.sleep(100);
            System.out.println();
            System.out.println("*******************");
            System.out.println("Corretora iniciada");
            System.out.println("*******************");
            ExportaFim();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean Caixas (String nomeAtivo, long idCiente, int quantidade, char CV, double valor) throws InterruptedException
    {
        if(caixa1.tryAcquire()){
            atualizaCompras(nomeAtivo, idCiente, quantidade, CV);
            numNegociacao++;
            System.out.println (" id Cliente: " + idCiente + " tipo de operacao: " +
                                CV + " ativo: " + nomeAtivo+" quantia de ativo: "+ quantidade +
                                " preço de C/V: " + new DecimalFormat("#,##00.00").format(valor) +
                                " no CAIXA 1");
            caixa1.release();
            return true;
        }

        else if(caixa2.tryAcquire()){
            atualizaCompras(nomeAtivo, idCiente, quantidade, CV);
            numNegociacao++;
            System.out.println (" id Cliente: " + idCiente + " tipo de operacao: " +
                        CV + " ativo: " + nomeAtivo+" quantia de ativo: "+ quantidade +
                        " preço de C/V: " + new DecimalFormat("#,##00.00").format(valor) +
                        " no CAIXA 2");
                caixa2.release();
                return true;
            }
        else
        {
            return false;
        }

    }
    public void atualizaCompras(String nomeAtivo, long idCiente, int quantidade, char CV)
    {
        Random gerador = new Random();
        if (CV == 'v')
        {
            double erro = gerador.nextDouble();
            double auxRec = rec.RecDados(0, erro);

            this.timeOperacoes.add(ativoA.dataTime.get(ativoA.dataTime.size()-1));
            this.nomeAtivosOperacao.add(nomeAtivo);
            this.idCliente.add(idCiente);
            this.qtdAtivoEntrada.add((double) 0);
            this.qtdAtivoSaida.add(erro);
            this.qtdAtivoSaidaRec.add(auxRec);
            this.compraOuVenda.add(CV);
        }
        else
        {
            double qtdAtiv = quantidade*ativoA.valores.get(ativoA.valores.size()-1);
            double erro = gerador.nextDouble();
            double auxRec = rec.RecDados(qtdAtiv, qtdAtiv + erro);


            this.timeOperacoes.add(ativoA.dataTime.get(ativoA.dataTime.size()-1));
            this.nomeAtivosOperacao.add(nomeAtivo);
            this.idCliente.add(idCiente);
            this.qtdAtivoEntrada.add(qtdAtiv);
            this.qtdAtivoSaida.add(qtdAtiv + erro);
            this.qtdAtivoSaidaRec.add(auxRec);
            this.compraOuVenda.add(CV);
        }
    }
    public void ExportaFim() throws InterruptedException {
        if (!this.fim)
        {
            Thread.sleep(5000);//tempo grande para não conflitar o print e finalizar execução dos clientes
            System.out.println("*******************");
            System.out.println("Fim execucao corretora");
            System.out.println("*******************");
            this.interrupt();

        }
        else
        {
            Thread.sleep(1000);
            ExportaFim();
        }
    }
}
