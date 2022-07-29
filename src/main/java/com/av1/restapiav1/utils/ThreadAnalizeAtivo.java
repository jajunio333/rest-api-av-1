package com.av1.restapiav1.utils;

import com.av1.restapiav1.entities.Ativo;

import java.util.ArrayList;
import java.util.Collections;

public class ThreadAnalizeAtivo extends Thread{

    private Ativo ativo;
    private Corretora corretora;
    private int size;
    private long idCiente;
    private double saldo;//TODO: SALDO - PRECISA FAZER CONTROLE DE ACESSO
    private boolean comprado;

    public ThreadAnalizeAtivo(Ativo ativo, Corretora corretora, int size, long idCiente, double saldo) {
        this.ativo = ativo;
        this.corretora = corretora;
        this.size = size;
        this.idCiente = idCiente;
        this.saldo = saldo; //VAI VIRAR FUNÇÃO
    }

    @Override
    public void run()
    {
        int numNegociacao = 0;

        while (this.corretora.fim && numNegociacao < 1000)
        {
            try
            {
                if (ativo.valores.size() > size)
                {
                   boolean comprar = VerificaCompra(ativo.valores);
                   boolean vender = VerificaVenda(ativo.valores);

                    ArrayList<Double> volatilidade = Volatilidade(ativo.valores,8);
                    int prioridade = 5;

                    if(volatilidade.get(volatilidade.size()-1) > 0.01 )
                    {
                        prioridade = 10;
                    }

                   //gerenciar saldo


                   if( comprar && !comprado)
                   {
                       if (saldo>10)
                       {
                           corretora.Caixas (ativo.getNome(), idCiente, 10, 'c', prioridade);
                           System.out.println(" id Cliente: " + idCiente + " COMPROU 10 ativos: " + ativo.getNome()+" no preço de : "+ ativo.valores.get(ativo.valores.size()-1));
                       }
                       numNegociacao++;
                       comprado= true;
                       saldo = saldo - 10;
                   }
                   if(vender)
                   {
                       if (saldo<1000)
                       {
                           corretora.Caixas (ativo.getNome(), idCiente, 10, 'c', prioridade);
                           System.out.println(" id Cliente: " + idCiente + " VENDEU 10 ativos: " + ativo.getNome()+" no preço de : "+ ativo.valores.get(ativo.valores.size()-1));
                       }
                       numNegociacao++;
                       comprado= false;
                       saldo = saldo + 10;
                   }

                    size = ativo.valores.size();
                }
                else
                {
                    Thread.sleep(10);
                }
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean VerificaCompra(ArrayList<Double> valores)
    {
        ArrayList<Double> media8 = MediaExp(valores,8);
        ArrayList<Double> media20 = MediaExp(valores,20);
        if(media8.get(media8.size()-2) < media20.get(media20.size()-2) && media8.get(media8.size()-1) > media20.get(media20.size()-1))
        {//cruzamento sem confirmação, tendencia curta de alta
            return true;
        }
        return false;
    }
    public boolean VerificaVenda(ArrayList<Double> valores)
    {
        ArrayList<Double> media8 = MediaExp(valores,8);
        ArrayList<Double> media20 = MediaExp(valores,20);
        if(media8.get(media8.size()-2) > media20.get(media20.size()-2) && media8.get(media8.size()-1) < media20.get(media20.size()-1))
        {//cruzamento sem confirmação, tendencia curta de baixa
            return true;
        }
        return false;
    }

    public ArrayList<Double> MediaSimples(ArrayList<Double> valores, int tamanhoMedia)
    {
        ArrayList<Double> medias = new ArrayList<>();
        Collections.reverse(valores);

        for (int i = 0; i <= (valores.size() - tamanhoMedia); i++) {
            double valorMedia = 0;
            for (int j = i; j< (i+tamanhoMedia); j++)
            {
                valorMedia += valores.get(j);
            }
            medias.add(Math.round((valorMedia/tamanhoMedia)*100000.0)/100000.0) ;
        }
        Collections.reverse(medias);

        return medias;
    }
    public ArrayList<Double> MediaExp(ArrayList<Double> valores, int tamanhoMedia)
    {
        ArrayList<Double> listaMediasExp = MediaSimples(valores,tamanhoMedia);
        double cte = (double)2/(tamanhoMedia+1);

        for (int i = 1; i < listaMediasExp.size() ; i++) {
            double val = ((valores.get(i) - listaMediasExp.get(i-1)) * cte) + listaMediasExp.get(i-1);
            listaMediasExp.set(i, (double) Math.round(val*100000.0)/100000.0) ;
        }

        return listaMediasExp;
    }
    public ArrayList<Double> Volatilidade(ArrayList<Double> valores, int tamanhoMedia)
    {
        ArrayList<Double> listaMedias = MediaSimples(valores,tamanhoMedia);
        ArrayList<Double> listaVolatilidade = new ArrayList<>();

        for (int i = 0; i < (listaMedias.size()); i++) {
            double aux = (Math.pow((valores.get(i+(tamanhoMedia-1))-listaMedias.get(i)),2)/tamanhoMedia);
            listaVolatilidade.add(Math.pow(aux, 0.5));
        }

        return listaVolatilidade;
    }

}
