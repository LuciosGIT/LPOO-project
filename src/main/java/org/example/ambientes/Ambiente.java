package org.example.ambientes;

abstract class Ambiente {

    //atributos
    private   String nome;
    private  String descricao;
    private  int dificuldadeExploracao;
    //private List<Item> recursosDisponiveis crie uma classe de probabilidades antes de implementar isso
    private  double probabilidadeEventos;
    private  String condicoesClimaticas;

    public Ambiente(String nome, String descricao, int dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas){

        this.nome = nome;
        this.descricao = descricao;
        this.dificuldadeExploracao = dificuldadeExploracao;
        this.probabilidadeEventos = probabilidadeEventos;
        this.condicoesClimaticas = condicoesClimaticas;

    }

    //métodos
    public abstract void explorar();

    public abstract void gerarEvento();

    public abstract void modificarClima();

}
