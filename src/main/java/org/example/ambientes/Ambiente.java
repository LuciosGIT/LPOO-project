package org.example.ambientes;

import org.example.domain.Personagem;

abstract class Ambiente {

    //atributos
    protected String nome;
    protected  String descricao;
    protected  int dificuldadeExploracao;
    //private List<Item> recursosDisponiveis crie uma classe de probabilidades antes de implementar isso
    protected  double probabilidadeEventos;
    protected  String condicoesClimaticas;

    public Ambiente(String nome, String descricao, int dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas){

        this.nome = nome;
        this.descricao = descricao;
        this.dificuldadeExploracao = dificuldadeExploracao;
        this.probabilidadeEventos = probabilidadeEventos;
        this.condicoesClimaticas = condicoesClimaticas;

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
