package org.example.probabilidades;

public class ProbabilidadeRecursos {

    protected double probabilidadeMadeira;
    protected double probabilidadePedra;
    protected double probabilidadeMetal;

    //construtor
    public ProbabilidadeRecursos(double probabilidadeMadeira, double probabilidadePedra, double probabilidadeMetal){
        this.probabilidadeMadeira = probabilidadeMadeira;
        this.probabilidadePedra = probabilidadePedra;
        this.probabilidadeMetal = probabilidadeMetal;
    }

    //métodos getters
    public double getProbabilidadeMadeira(){
        return probabilidadeMadeira;
    }

    public double getProbabilidadePedra(){
        return probabilidadePedra;
    }

    public double getProbabilidadeMetal(){
        return probabilidadeMetal;
    }

    //métodos setters

    public void setProbabilidadeMadeira(double probabilidadeMadeira){
        this.probabilidadeMadeira = probabilidadeMadeira;
    }

    public void setProbabilidadePedra(double probabilidadePedra){
        this.probabilidadePedra = probabilidadePedra;
    }

    public void setProbabilidadeMetal(double probabilidadeMetal){
        this.probabilidadeMetal = probabilidadeMetal;
    }

}
