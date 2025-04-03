package org.example.domain;

import org.example.enums.TipoClimatico;
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
    private volatile Double dificuldadeExploracao;
    private List<Item> recursosDisponiveis = new ArrayList<>();
    private Map<Evento, Double> probabilidadeEventos;
    private List<Evento> eventosPossiveis;
    private  List<TipoClimatico> tiposDeClimasDoAmbiente;
    private  List<Evento> listaDeclimasDoJogo;
    private  List<Criatura> criaturasAmbientes;


    public Ambiente(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas){

        this.nome = nome;
        this.descricao = descricao;
        this.dificuldadeExploracao = dificuldadeExploracao;
        this.probabilidadeEventos = new HashMap<>();
        this.tiposDeClimasDoAmbiente = condicoesClimaticas;

        //colocar crocodilo, morcego, sobrevivente
        this.criaturasAmbientes = ConfiguracaoDoMundo.getCriaturasPadrao();

        //criando uma lista de climas que irão ter no jogo
        this.listaDeclimasDoJogo = ConfiguracaoDoMundo.getEventosClimaticosPadrao();

    }

    public Ambiente() {

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

    public void setDificuldadeExploracao(Double dificuldadeExploracao){
        if(dificuldadeExploracao == null){
            throw new IllegalArgumentException("Você passou um parâmetro inválido");
        }
        this.dificuldadeExploracao = dificuldadeExploracao;
    }

    public Map<Evento, Double> getProbabilidadeEventos() {
        return probabilidadeEventos;
    }

    public List<Item> getRecursosDisponiveis() {
        return this.recursosDisponiveis;
    }

    public List<Criatura> getCriaturasAmbientes(){
        return this.criaturasAmbientes;
    }

    public List<Evento> getEventos() {
        return eventosPossiveis;
    }

    public List<TipoClimatico> getTiposDeClimasDoAmbiente() {
        return this.tiposDeClimasDoAmbiente;
    }

    public List<Evento> getListaDeclimasDoJogo(){
        return this.listaDeclimasDoJogo;
    }


}