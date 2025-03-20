package org.example.domain;

public abstract class Criatura {

    private String nome;
    private int vida;
    private int nivelDePerigo;

    public Criatura(String nome, int vida, int nivelDePerigo){

        this.nome = nome;
        this.vida = vida;
        this.nivelDePerigo = nivelDePerigo;

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

}
