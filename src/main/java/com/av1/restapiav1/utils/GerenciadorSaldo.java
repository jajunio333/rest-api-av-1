package com.av1.restapiav1.utils;

import java.util.concurrent.Semaphore;

public class GerenciadorSaldo extends Thread{
    private double saldo;
    private Semaphore controleAcesso;

    public  GerenciadorSaldo (double saldo)
    {
        this.saldo = saldo;
        this.controleAcesso = new Semaphore(1);
    }

    public synchronized double GetSaldo()
    {
        if (controleAcesso.tryAcquire())
        {
            controleAcesso.release();
            return this.saldo;
        }

        return -1;
    }
    public synchronized void SetSaldo(double saldo)
    {
        if (controleAcesso.tryAcquire())
        {
            this.saldo=saldo;
            controleAcesso.release();
        }
    }

}
