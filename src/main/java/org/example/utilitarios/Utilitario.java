package org.example.utilitarios;

import org.example.criatura.Morcego;
import org.example.criatura.Sobrevivente;
import org.example.domain.Criatura;
import org.example.domain.Personagem;

import java.util.Random;

public class Utilitario {

    public static Double getValorAleatorio() {
        Random random = new Random();
        return random.nextDouble();
    }

    public static boolean getBooleanAleatorio(){
        Random random = new Random();
        return random.nextBoolean();
    }

    public static boolean spawnarMorcego(Personagem jogador) {
        boolean foiAtacado = false;
        foiAtacado = Utilitario.getBooleanAleatorio();
        if (foiAtacado) {
            Criatura criatura = new Morcego("Morcego Espantoso", 100.0, 25.0, 25.0);
            criatura.ataque(jogador);
        }
            return foiAtacado;

    }

    public static boolean spawnarSobrevivente(Personagem jogador) {
        boolean foiAtacado = false;
        foiAtacado = Utilitario.getBooleanAleatorio();
        if (foiAtacado) {
            Criatura criatura = new Sobrevivente("Sobrevivente Perdido", 100.0, 15.0, 15.0);
            criatura.ataque(jogador);
        }
        return foiAtacado;

    }

    public static boolean armadilhaInstaurada(Personagem jogador) {
        boolean armadilhaInstaurada = false;
        armadilhaInstaurada = Utilitario.getBooleanAleatorio();
        if (armadilhaInstaurada) {
            jogador.diminuirVida(25.0);
            jogador.diminuirEnergia(15.0);
            jogador.diminuirSanidade(22.0);
        }
        return armadilhaInstaurada;
    }
}
