package org.example.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import org.example.telasDoJogo.TelaDeEscolhaPersonagem;
import org.example.telasDoJogo.TelaDeInicio;

public class MyGame extends Game {

    @Override
    public void create() {
        // Inicialização do seu jogo

        setScreen(new TelaDeInicio(this));

    }

    @Override
    public void resize(int width, int height) {
        // Redimensionamento da janela
    }

    @Override
    public void pause() {
        // Quando o jogo for pausado
    }

    @Override
    public void resume() {
        // Quando o jogo for retomado
    }

    @Override
    public void dispose() {
        // Limpeza quando o jogo é encerrado
    }
}
