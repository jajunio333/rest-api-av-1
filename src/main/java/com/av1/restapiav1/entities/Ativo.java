package com.av1.restapiav1.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Ativo {
    public String nome;
    public ArrayList<Double> valores;

    public Ativo(String nome)
    {
        this.nome = nome;
        this.valores = new ArrayList<>();
    }
}
