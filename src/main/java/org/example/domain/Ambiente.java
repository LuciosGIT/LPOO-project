package org.example.domain;

import org.example.interfaces.AmbienteInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Ambiente implements AmbienteInterface {

    //atributos
    private String nome;
    private String descricao;
    private Double dificuldadeExploracao;
    private List<Item> recursosDisponiveis = new ArrayList<>();
    private double probabilidadeEventos;
    private String condicoesClimaticas;
    private Double valorAleatorio;

    public Ambiente(String nome, String descricao, Double dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas){

        this.nome = nome;
        this.descricao = descricao;
        this.dificuldadeExploracao = dificuldadeExploracao;
        this.probabilidadeEventos = probabilidadeEventos;
        this.condicoesClimaticas = condicoesClimaticas;

    }

    //métodos

   /* public void setProbabilidades(){
        Random random = new Random();
        for(Item item : recursosDisponiveis) {
            item.setProbabilidadeDeEncontrar(random.nextDouble(0.6,1.0));
        }
    }
    */


    public String getNome() {
        return this.nome;
    }

    public  String getDescricao() {
        return this.descricao;
    }

    public Double getDificuldadeExploracao() {
        return this.dificuldadeExploracao;
    }

    public  double getProbabilidadeEventos() {
        return this.probabilidadeEventos;
    }

    public double getValorAleatorio(){
        //Usado para gerar probabilidades nas subclasses de `Ambiente`
        Random random = new Random();
        return this.valorAleatorio = random.nextDouble();
    }

    public List<Item> getRecursosDisponiveis() {
        return this.recursosDisponiveis;
    }

}
