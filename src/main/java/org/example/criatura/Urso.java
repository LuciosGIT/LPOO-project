package org.example.criatura;

import org.example.domain.Criatura;
import org.example.domain.Personagem;

public class Urso extends Criatura {

    public Urso(String nome, Double vida, int nivelDePerigo, Double danoDeAtaque) {
        super(nome, vida, nivelDePerigo, danoDeAtaque);
    }

    @Override
    public void ataque(Personagem jogador) {
        jogador.diminuirVida(this.getDanoDeAtaque());
    }
}
