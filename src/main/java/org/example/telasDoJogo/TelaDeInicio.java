package org.example.telasDoJogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;



public class TelaDeInicio implements Screen {

    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;


    @Override
    public void show() {
        // Inicializar o viewport e stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Inicializar o SpriteBatch e a textura como campos da classe
        batch = new SpriteBatch();
        backgroundTexture = new Texture("backgroundTelaDeInicio2.jpg");

    }


    @Override
    public void hide() {
        System.out.println("TelaDeInicio - Hide method called");
    }

    @Override
    public void resize(int width, int height) {
        // Atualizar o viewport quando o tamanho da tela mudar
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void pause() {
        System.out.println("TelaDeInicio - Pause method called");
    }

    @Override
    public void resume() {
        System.out.println("TelaDeInicio - Resume method called");
    }

    @Override
    public void dispose() {
        // Liberar recursos
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();
    }


    @Override
    public void render(float delta) {
        // Limpar a tela
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Desenhar o fundo
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Atualizar e desenhar o stage
        stage.act(delta);
        stage.draw();
    }

}

