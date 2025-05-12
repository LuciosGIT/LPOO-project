package org.example.criatura;

import org.example.domain.Criatura;
import org.example.domain.Personagem;

public class Crocodilo extends Criatura {

    public Crocodilo(String nome, Double vida, Double nivelDePerigo, Double danoDeAtaque) {
        super(nome, vida, nivelDePerigo, danoDeAtaque);
    }
    @Override
    public void ataque(Personagem jogador) {

    }
}
