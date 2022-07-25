package com.av1.restapiav1.utils;

import com.av1.restapiav1.entities.Ativo;

public class Cliente implements Runnable{
    private Ativo ativoA, ativoB, ativoC, ativoD;
    private Corretora corretora;

    public Cliente(Ativo ativoA, Ativo ativoB, Ativo ativoC, Ativo ativoD)
    {
        this.ativoA = ativoA;
        this.ativoA = ativoA;
        this.ativoB = ativoB;
        this.ativoC = ativoC;
        this.ativoD = ativoD;
        this.corretora = new Corretora(ativoA,ativoB,ativoC,ativoD);
    }

    @Override
    public void run()//TODO: verificar por alteração de size do ativo
    {
        //houve alteração no tamanho?
        //sim -> atualiza as médias -> verifica os indicadores de compra e venda -> caso disparado realiza a compra/venda SE POSSÍVEL com semaforo -> atualiza o valor de tamanho ->fim
        //nao -> aguarda alteração

        corretora.run();
        int size = ativoA.valores.size();
        int aux = 100;
        boolean alteracao = false;
        while (corretora.fim)
        {
            try
            {
                if (ativoA.valores.size() > size)
                {
                    System.out.println("incrementou " + ativoA.valores.size());
                    size = ativoA.valores.size();
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


