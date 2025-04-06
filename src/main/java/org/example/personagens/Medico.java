package org.example.personagens;

import org.example.domain.Ambiente;
import org.example.domain.Personagem;
import org.example.itens.Ferramentas;

public class Medico extends Personagem {

    public Medico(String nome) {
        this.alterarNomePersonagem(nome);
    }

    @Override
    public void habilidade() {
    // to do
    }
}
