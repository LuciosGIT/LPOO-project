package org.example.domain;

import org.example.interfaces.EventoInterface;

import java.util.List;

public abstract class Evento implements EventoInterface {

    private String nome;

    private String descricao;

    private Double probabilidadeOcorrencia;

    private String impacto;

    private boolean ativavel;

    public Evento(boolean ativavel, String descricao, String impacto, String nome, Double probabilidadeOcorrencia) {
        this.ativavel = ativavel;
        this.descricao = descricao;
        this.impacto = impacto;
        this.nome = nome;
        this.probabilidadeOcorrencia = probabilidadeOcorrencia;
    }

    public Evento() {

    }

    public boolean isAtivavel() {
        return ativavel;
    }

    public void setAtivavel(boolean ativavel) {
        this.ativavel = ativavel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImpacto() {
        return impacto;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getProbabilidadeOcorrencia() {
        return probabilidadeOcorrencia;
    }

    public void setProbabilidadeOcorrencia(Double probabilidadeOcorrencia) {
        this.probabilidadeOcorrencia = probabilidadeOcorrencia;
    }
}