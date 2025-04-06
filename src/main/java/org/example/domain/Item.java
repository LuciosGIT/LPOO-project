package org.example.domain;

import org.example.enums.TipoItem;
import org.example.interfaces.ItemInterface;

public abstract class Item implements ItemInterface {

    private Personagem personagem;

    private String nomeItem;

    private Double peso;

    private Double durabilidade;

    private  Double probabilidadeDeEncontrar;

    protected Item(String nomeItem, Personagem personagem, Double peso, Double durabilidade, Double probabilidadeDeEncontrar) {
        this.nomeItem = nomeItem;
        this.personagem = personagem;
        this.peso = peso;
        this.durabilidade = durabilidade;
        this.probabilidadeDeEncontrar = probabilidadeDeEncontrar;

    }
    public Item() {

    }

    public String getNomeItem() {
        return nomeItem;
    }

    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }

    public Double getPeso() {
        return peso;
    }

    public void aumentarPeso(Double peso) {
        if (peso <= 0) {
            throw new IllegalArgumentException("O peso deve ser aumentado, portanto só pode admitir valores maiores que 0!");
        }
            this.peso += peso;
    }

    public void diminuirPeso(Double peso) {
        if (peso <= 0) {
            throw new IllegalArgumentException("O peso a ser diminuído só pode admitir valores maiores que 0!");
        }
        this.peso -= peso;
    }

    public Personagem getPersonagem() {
        return personagem;
    }

    public void alterarPersonagem(Personagem personagem) {

        this.personagem = personagem;

    }

    public Double getDurabilidade() {
        return durabilidade;
    }

    public void aumentarDurabilidade(Double durabilidade) {

        if (durabilidade <= 0) {
            throw new IllegalArgumentException("A durabilidade só admite valores maiores que 0!");
        }
        this.durabilidade += durabilidade;
    }

    public void diminuirDurabilidade(Double durabilidade) {

        if (durabilidade <= 0) {
            throw new IllegalArgumentException("A durabilidade só admite valores maiores que 0!");
        }
        this.durabilidade -= durabilidade;
    }

    public double getProbabilidadeDeEncontrar(){
        return this.probabilidadeDeEncontrar;
    }

    public void setProbabilidadeDeEncontrar(Double probabilidadeDeEncontrar){
        if (probabilidadeDeEncontrar <= 0) {
            throw new IllegalArgumentException("A probabilidade de se encontrar um item só admite valores maiores que 0!");
        }
        this.probabilidadeDeEncontrar = probabilidadeDeEncontrar;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public void setPersonagem(Personagem personagem) {
        this.personagem = personagem;
    }

    public void setDurabilidade(Double durabilidade) {
        this.durabilidade = durabilidade;
    }
}
