package com.av1.restapiav1.utils.RD;


public class Rec {

    public double RecDados (double a, double b) {
        //CASO ADOTADO PARA ESTA QUESTÃO
        //   F1     F2
        //=====>O=====>


        double[] y = new double[] { a, b};

        double[] v = new double[] { 0.0001, 0.01 };

        double[][] A = new double[][] { { 1, -1 }};

        Reconciliation rec = new Reconciliation(y, v, A);
        double[] _m = rec.getReconciledFlow();
        return _m[1];
    }
}
