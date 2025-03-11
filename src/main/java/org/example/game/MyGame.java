package org.example.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL20;

public class MyGame extends ApplicationAdapter {

    @Override
    public void create() {
        // Inicialização do seu jogo
    }

    @Override
    public void render() {
        // Lógica do jogo, chamada a cada quadro
        GL20 gl = com.badlogic.gdx.Gdx.gl;
        gl.glClearColor(0, 0, 0, 1);  // Cor de fundo
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Limpa a tela
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
