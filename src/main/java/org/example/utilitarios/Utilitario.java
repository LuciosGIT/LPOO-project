package org.example.utilitarios;

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
