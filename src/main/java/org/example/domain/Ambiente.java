package org.example.domain;

import org.example.enums.TipoClimatico;
import org.example.eventos.EventoClimatico;
import org.example.interfaces.AmbienteInterface;
import org.example.utilitarios.ConfiguracaoDoMundo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Ambiente implements AmbienteInterface {

    //atributos
    private String nome;
    private String descricao;
    private Double dificuldadeExploracao;
    private List<Item> recursosDisponiveis = new ArrayList<>();
    private List<Evento> eventosPossiveis = new ArrayList<>();
    private  List<TipoClimatico> tiposDeClimasDoAmbiente;


    public Ambiente(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas){

        this.nome = nome;
        this.descricao = descricao;
        this.dificuldadeExploracao = dificuldadeExploracao;
        tiposDeClimasDoAmbiente = condicoesClimaticas;

        //colocar crocodilo, morcego, sobrevivente

        //criando uma lista de climas que irão ter no jogo


    }

    public Ambiente() {

    }

    public String getNome() {
        return this.nome;
    }

    public  String getDescricao() {
        return this.descricao;
    }

    public Double getDificuldadeExploracao() {
        return this.dificuldadeExploracao;
    }

    public void setDificuldadeExploracao(Double dificuldadeExploracao){
        if(dificuldadeExploracao == null){
            throw new IllegalArgumentException("Você passou um parâmetro inválido");
        }
        this.dificuldadeExploracao = dificuldadeExploracao;
    }

    protected void adicionarRecurso(Item item) {
        if (item == null) throw new IllegalArgumentException("Recurso não pode ser nulo");
        this.recursosDisponiveis.add(item);
    }

    protected void adicionarEvento(Evento evento) {
        if (evento == null) throw new IllegalArgumentException("Evento não pode ser nulo");
        this.eventosPossiveis.add(evento);
    }


    public List<Item> getRecursosDisponiveis() {
        return this.recursosDisponiveis;
    }


    public List<Evento> getEventos() {
        return eventosPossiveis;
    }

    public List<TipoClimatico> getTiposDeClimasDoAmbiente() {
        return this.tiposDeClimasDoAmbiente;
    }



}