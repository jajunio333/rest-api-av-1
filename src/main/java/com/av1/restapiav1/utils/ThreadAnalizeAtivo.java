package com.av1.restapiav1.utils;

import com.av1.restapiav1.entities.Ativo;

public class ThreadAnalizeAtivo extends Thread{

    private Ativo ativo;
    private Corretora corretora;
    private int size;
    private long idCiente;
    private double saldo;//TODO: SALDO ESTA ERRADO, PRECISA FAZER CONTROLE DE ACESSO

    public ThreadAnalizeAtivo(Ativo ativo, Corretora corretora, int size, long idCiente, double saldo) {
        this.ativo = ativo;
        this.corretora = corretora;
        this.size = size;
        this.idCiente = idCiente;
        this.saldo = saldo;
    }

    @Override
    public void run()//TODO: verificar por alteração de size do ativo
    {
        //houve alteração no tamanho?
        //sim -> atualiza as médias -> verifica os indicadores de compra e venda -> caso disparado realiza a compra/venda SE POSSÍVEL e com semaforo -> atualiza o valor de tamanho ->fim
        //nao -> aguarda alteração

        while (this.corretora.fim)
        {
            try
            {
                if (ativo.valores.size() > size)
                {
                    System.out.println("incrementou " + ativo.valores.size() +"id Cliente: " + idCiente + " ativo: " + ativo.getNome());
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

}
