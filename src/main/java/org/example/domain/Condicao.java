package org.example.domain;

import org.example.interfaces.CondicaoInterface;

public abstract class Condicao implements CondicaoInterface {

    private String descricao;

    private Personagem jogadorAfetado;

    public Condicao() {

    }

    public Condicao(String descricao, Personagem jogadorAfetado) {
        this.descricao = descricao;
        this.jogadorAfetado = jogadorAfetado;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {}

}