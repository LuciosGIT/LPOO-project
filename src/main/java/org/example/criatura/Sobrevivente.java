package org.example.criatura;

import org.example.domain.Criatura;
import org.example.domain.Personagem;

public class Sobrevivente extends Criatura {

    public Sobrevivente(String nome, Double vida, Double nivelDePerigo, Double danoDeAtaque) {
        super(nome, vida, nivelDePerigo, danoDeAtaque);
    }

    @Override
    public void ataque(Personagem jogador) {
        jogador.diminuirVida(this.getDanoDeAtaque());
    }
}
