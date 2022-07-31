package com.av1.restapiav1.utils;

import com.av1.restapiav1.entities.Ativo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class ThreadAnalizeAtivo extends Thread{
    private Ativo ativo;
    private Corretora corretora;
    private int size;
    private long idCiente;
    private boolean comprado;
    private double qtdAtivo;
    public ArrayList<String> timeOperacoes;
    public ArrayList<Double> listaqtdAtivos;
    public ArrayList<Character> compraOuVenda;
    public GerenciadorSaldo gerenciadorSaldo;

    public ThreadAnalizeAtivo(Ativo ativo, Corretora corretora, int size, long idCiente, GerenciadorSaldo saldo) {
        this.ativo = ativo;
        this.corretora = corretora;
        this.size = size;
        this.idCiente = idCiente;
        this.gerenciadorSaldo = saldo;
        this.qtdAtivo = 0;
        this.timeOperacoes = new ArrayList<>();
        this.listaqtdAtivos = new ArrayList<>();
        this.compraOuVenda = new ArrayList<>();
    }

    @Override
    public synchronized void run()
    {
        while (this.corretora.fim && corretora.numNegociacao < 1000)
        {
            try
            {
                if (ativo.valores.size() > size)
                {
                   boolean comprar = VerificaCompra(ativo.valores);
                   boolean vender = VerificaVenda(ativo.valores);

                    if(VerificaRisco())
                    {
                        this.setPriority(10);//TODO: --------------------conferir ---------------------
                    }

                   if( comprar && !comprado && gerenciadorSaldo.GetSaldo()>10)
                   {
                       boolean comprou = corretora.Caixas (ativo.getNome(), idCiente, 10, 'c', ativo.valores.get(ativo.valores.size()-1));
                       if (comprou)
                       {
                           AtualizaComprasCliente(10, 'c');
                           comprado = true;
                       }
                       else
                       {
                           comprado= false;
                       }
                   }
                   if(vender && VerifificaVendaAtivoCorrente())
                   {
                       boolean vendeu = corretora.Caixas (ativo.getNome(), idCiente, 0, 'v', ativo.valores.get(ativo.valores.size()-1));
                       if (vendeu) {
                           AtualizaComprasCliente(0, 'v');
                           comprado= false;
                       }
                       else
                       {
                           comprado= true;
                       }
                   }
                    //Aguarda 0,5 segundos para realizar nova operação
                    Thread.sleep(500);
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

        Imprime();

        Thread.interrupted();
    }
    public boolean VerificaRisco()
    {
        ArrayList<Double> volatilidade = Volatilidade(ativo.valores,8);
        if(volatilidade.get(volatilidade.size()-1) > 0.005)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean VerificaCompra(ArrayList<Double> valores)
    {
        {
            ArrayList<Double> media8 = MediaExp(valores,8);
            ArrayList<Double> media20 = MediaExp(valores,20);
            if(media8.get(media8.size()-2) < media20.get(media20.size()-2) && media8.get(media8.size()-1) > media20.get(media20.size()-1))
            {//cruzamento sem confirmação, tendencia curta de alta
                return true;
            }
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
    public boolean VerifificaVendaAtivoCorrente()
    {
        if (qtdAtivo != 0)
        {
            return true;
        }
        return false;
    }
    public void AtualizaComprasCliente( int quantidade, char CV)
    {
        if (CV == 'v')
        {
            double saldo = gerenciadorSaldo.GetSaldo();
            if (saldo!= -1)
            {
                gerenciadorSaldo.SetSaldo(saldo - (qtdAtivo * ativo.valores.get(ativo.dataTime.size() - 1)));
                qtdAtivo = 0;

                int passo = ativo.dataTime.size()-1;
                listaqtdAtivos.add((double) 0);
                timeOperacoes.add(ativo.dataTime.get(passo));
                compraOuVenda.add(CV);
            }
            else
            {
                AtualizaComprasCliente(quantidade,CV);
            }
        }
        else
        {
            double saldo = gerenciadorSaldo.GetSaldo();
            if (saldo!= -1) {
                qtdAtivo = quantidade * ativo.valores.get(ativo.dataTime.size() - 1);
                gerenciadorSaldo.SetSaldo(saldo - 10);

                int passo = ativo.dataTime.size() - 1;
                listaqtdAtivos.add(qtdAtivo);
                timeOperacoes.add(ativo.dataTime.get(passo));
                compraOuVenda.add(CV);
            }
            else
            {
                AtualizaComprasCliente(quantidade,CV);
            }
        }
    }
    private void Imprime ()
    {
        double saldo = gerenciadorSaldo.GetSaldo();
        if (saldo > 0 && qtdAtivo > 0)
        {
            double balanc = qtdAtivo * ativo.valores.get(ativo.valores.size()-1);
            System.out.println("Saldo final cliente id "
                    + idCiente + ": "
                    + new DecimalFormat("#,##000.000").format(saldo)
                    + " ativo: "
                    + ativo.getNome()
                    + " saldo do ativo: "
                    + new DecimalFormat("#,##000.000").format(balanc));
        }
        else if (saldo > 0 && qtdAtivo <= 0)
        {
            System.out.println("Saldo final cliente id "
                    + idCiente + ": "
                    + new DecimalFormat("#,##000.000").format(saldo));
        }
        else
        {
            Imprime();
        }
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
