package org.example.domain;

import org.example.enums.TipoItem;

public abstract class Item {

    protected Personagem personagem;

    protected TipoItem tipoItem;

    protected Double peso;

    protected Double durabilidade;

    protected  Double probabilidadeDeEncontrar;

    public Item(TipoItem tipoItem, Personagem personagem, Double peso, Double durabilidade, Double probabilidadeDeEncontrar) {
        this.tipoItem = tipoItem;
        this.personagem = personagem;
        this.peso = peso;
        this.durabilidade = durabilidade;
        this.probabilidadeDeEncontrar = probabilidadeDeEncontrar;

    }
    public Item() {

    }
    public abstract void usar();

    public TipoItem getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(TipoItem tipoItem) {
        this.tipoItem = tipoItem;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Personagem getPersonagem() {
        return personagem;
    }

    public void setPersonagem(Personagem personagem) {
        this.personagem = personagem;
    }

    public Double getDurabilidade() {
        return durabilidade;
    }

    public void setDurabilidade(Double durabilidade) {
        this.durabilidade = durabilidade;
    }

    public double getProbabilidadeDeEncontrar(){
        return this.probabilidadeDeEncontrar;
    }

    public void setProbabilidadeDeEncontrar(Double probabilidadeDeEncontrar){
        this.probabilidadeDeEncontrar = probabilidadeDeEncontrar;
    }



}
