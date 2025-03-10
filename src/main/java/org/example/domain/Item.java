package org.example.domain;

import org.example.enums.TipoItem;

public abstract class Item {

    protected Personagem personagem;

    protected TipoItem tipoItem;

    protected Double peso;

    protected Double durabilidade;

    public Item(TipoItem tipoItem, Personagem personagem, Double peso, Double durabilidade) {
        this.tipoItem = tipoItem;
        this.personagem = personagem;
        this.peso = peso;
        this.durabilidade = durabilidade;

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
}
