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

}
