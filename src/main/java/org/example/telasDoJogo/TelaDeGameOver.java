package org.example.telasDoJogo;

import org.example.interfaces.ScreenInterface;

public class TelaDeGameOver implements ScreenInterface {

    @Override
    public void mostrar() {
        System.out.println("Tela de Game Over exibida");
    }

    @Override
    public void esconder() {
        System.out.println("Tela de Game Over escondida");
    }

    @Override
    public boolean estaVisivel() {
        return false; // Retorna false por padrão
    }
}
