package org.example.domain;

import org.example.interfaces.CriaturaInterface;

public abstract class Criatura implements CriaturaInterface {

    private String nome;
    private int vida;
    private int nivelDePerigo;
    private Double danoDeAtaque;

    public Criatura(String nome, int vida, int nivelDePerigo, Double danoDeAtaque){

        this.nome = nome;
        this.vida = vida;
        this.nivelDePerigo = nivelDePerigo;
        this.danoDeAtaque = danoDeAtaque;

    }

    public String getNome(){
        return this.nome;
    }

    public int getVida(){
        return this.vida;
    }

    public int getNivelDePerigo(){
        return this.nivelDePerigo;
    }

    public Double getDanoDeAtaque() {
        return danoDeAtaque;
    }

    public void setDanoDeAtaque(Double danoDeAtaque) {
        this.danoDeAtaque = danoDeAtaque;
    }
}
