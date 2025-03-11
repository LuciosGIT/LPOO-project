package org.example.domain;

import org.example.itens.Inventario;

import java.util.Collections;


public abstract class Personagem {

    private String nome;

    private Double vida;

    private Double fome;

    private Double sede;

    private Double energia;

    private Double sanidade;

    private Inventario inventario;

    private double[] localizacao;

    public abstract void habilidade();

    public Personagem(String nome) {
        this.nome = nome;
        this.energia = 100.0;
        this.fome = 50.0;
        this.vida = 100.0;
        this.sede = 50.0;
        this.sanidade = 100.0;
        this.inventario = new Inventario(6, Collections.emptyList(), 0.0);
        this.localizacao = new double[]{0.0, 0.0};
    }

    public Double getEnergia() {
        return energia;
    }

    public void setEnergia(Double energia) {
        this.energia = energia;
    }

    public Double getFome() {
        return fome;
    }

    public void setFome(Double fome) {
        this.fome = fome;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public double[] getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(double[] localizacao) {
        this.localizacao = localizacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getSede() {
        return sede;
    }

    public void setSede(Double sede) {
        this.sede = sede;
    }

    public Double getSanidade() {
        return sanidade;
    }

    public void setSanidade(Double sanidade) {
        this.sanidade = sanidade;
    }

    public Double getVida() {
        return vida;
    }

    public void setVida(Double vida) {
        this.vida = vida;
    }
}
