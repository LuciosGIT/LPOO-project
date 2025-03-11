package com.ultimafronteira.ambientes;

abstract class Ambiente {

    //atributos
    String nome;
    String descricao;
    int dificuldadeExploracao;
    //List<Item> recursosDisponiveis;
    double probabilidadeEventos;
    String condicoesClimaticas;

    //métodos
    public abstract void explorar();

    public abstract void gerarEvento();

    public abstract void modificarClima();

}
