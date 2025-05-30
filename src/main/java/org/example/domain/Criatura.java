package org.example.domain;

import org.example.interfaces.CriaturaInterface;


public abstract class Criatura implements CriaturaInterface {

    private String nome;
    private Double vida;
    private final Double nivelDePerigo;
    private final Double danoDeAtaque;

    public Criatura(String nome, Double vida, Double nivelDePerigo, Double danoDeAtaque){

        this.nome = nome;
        this.vida = vida;
        this.nivelDePerigo = nivelDePerigo;
        this.danoDeAtaque = danoDeAtaque;

    }

    public String getNome(){
        return this.nome;
    }

    public Double getVida(){
        return this.vida;
    }

    public Double getNivelDePerigo(){
        return this.nivelDePerigo;
    }

    public Double getDanoDeAtaque() {
        return danoDeAtaque;
    }

    public void diminuirVida(Double vida) {
        this.vida -= vida;
    }

    public void aumentarVida(Double vida) {
        this.vida += vida;
    }
}
