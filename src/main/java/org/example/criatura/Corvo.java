package org.example.criatura;

import org.example.domain.Criatura;
import org.example.domain.Personagem;

public class Corvo extends Criatura {

    public Corvo(String nome, Double vida, Double nivelDePerigo, Double danoDeAtaque) {
        super(nome, vida, nivelDePerigo, danoDeAtaque);
    }

    @Override
    public void ataque(Personagem jogador) {
        jogador.diminuirSanidade(getDanoDeAtaque()*0.3);
        jogador.diminuirVida(getDanoDeAtaque());
    }
}
