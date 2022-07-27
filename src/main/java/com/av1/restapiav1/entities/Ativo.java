package com.av1.restapiav1.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Ativo {
    public String nome;
    public ArrayList<Double> valores;
    public double Drawdown, max, min, risco;
    public ArrayList<Double> mediaCurta;
    public ArrayList<Double> mediaIntermediaria;
    public ArrayList<Double> mediaLonga;

    public Ativo(String nome)
    {
        this.nome = nome;
        this.valores = new ArrayList<>();
        this.mediaCurta = new ArrayList<>();
        this.mediaIntermediaria = new ArrayList<>();
        this.mediaLonga = new ArrayList<>();

    }
}
