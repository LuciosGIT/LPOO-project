package org.example.personagens;

import org.example.domain.Ambiente;
import org.example.domain.Personagem;
import org.example.itens.Ferramentas;

public class Sobrevivente extends Personagem {



    public Sobrevivente(String nome) {
        this.alterarNomePersonagem(nome);
    }


    @Override
    public void habilidade() {
        this.modificadorFome = 0.7;
        this.modificadorSede = 0.7;
    }
}
