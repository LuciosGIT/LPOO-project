package org.example.domain;

import java.util.List;

public abstract class Ambiente {

    //atributos
    protected String nome;
    protected  String descricao;
    protected  int dificuldadeExploracao;
    protected  double probabilidadeEventos;
    protected  String condicoesClimaticas;
    protected List<Item> recursosDisponiveis; //crie uma classe de probabilidades antes de implementar isso

    public Ambiente(String nome, String descricao, int dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas, List<Item> recursosDisponiveis){

        this.nome = nome;
        this.descricao = descricao;
        this.dificuldadeExploracao = dificuldadeExploracao;
        this.probabilidadeEventos = probabilidadeEventos;
        this.condicoesClimaticas = condicoesClimaticas;
        this.recursosDisponiveis = recursosDisponiveis;

    }

    //métodos

    public abstract String getNome();
    public abstract String getDescricao();
    public abstract  int getDificuldadeExploracao();
    public abstract double getProbabilidadeEventos();

    public abstract void explorar(Personagem jogador);

    public abstract void gerarEvento();

    public abstract void modificarClima();

}
