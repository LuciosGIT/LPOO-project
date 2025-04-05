package org.example.telasDoJogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class TelaDeInicio implements Screen {

    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture buttonTexture;

    @Override
    public void show() {
        // Inicializar o viewport e stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Inicializar o SpriteBatch e a textura como campos da classe
        batch = new SpriteBatch();
        backgroundTexture = new Texture("backgroundTelaDeInicio.png");

        //centralizar



        //criar botões
        buttonTexture = new Texture("imagemBotaoJogar.png");
        ImageButton buttonJogar = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture)));

        buttonJogar.setSize(buttonJogar.getWidth()*0.5f, buttonJogar.getHeight()*0.2f);
        buttonJogar.setPosition((float) Gdx.graphics.getWidth() /2-buttonJogar.getWidth()/2, (float) Gdx.graphics.getHeight() /2-buttonJogar.getHeight()/2);

        buttonJogar.addListener(new ClickListener(){


        });

        stage.addActor(buttonJogar);

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
        buttonTexture.dispose();
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

