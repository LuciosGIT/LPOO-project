package org.example.criatura;

import org.example.domain.Criatura;
import org.example.domain.Personagem;
import java.util.Optional;

public class Cobra extends Criatura {

    public Cobra(String nome, Double vida, Double nivelDePerigo, Double danoDeAtaque) {
        super(nome, vida, nivelDePerigo, danoDeAtaque);
    }

    @Override
    public void ataque(Personagem jogador) {
        jogador.diminuirVida(this.getDanoDeAtaque());
        //jogador.envenenar();

    }





}
