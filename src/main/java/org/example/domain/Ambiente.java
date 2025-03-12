package org.example.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Ambiente {

    //atributos
    protected String nome;
    protected  String descricao;
    protected  int dificuldadeExploracao;
    protected List<Item> recursosDisponiveis = new ArrayList<>(); //crie uma classe de probabilidades antes de implementar isso
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

    public void setProbabilidades(){
        Random random = new Random();
        for(Item item : recursosDisponiveis) {
            item.setProbabilidadeDeEncontrar(random.nextDouble(0.6,1.0));
        }
    }

    public abstract String getNome();
    public abstract String getDescricao();
    public abstract  int getDificuldadeExploracao();
    public abstract double getProbabilidadeEventos();

    public abstract void explorar(Personagem jogador);

    public abstract void gerarEvento();

    public abstract void modificarClima();

}
