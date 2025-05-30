package org.example.telasDoJogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Game;

public class TelaDeInicio implements Screen {

    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture buttonTextureJogar;
    private Texture buttonTextureSair;
    private Texture buttonTextureJogarPressionado;
    private Texture buttonTextureSairPressionado;
    private Sound somAmbiente;
    private Sound buttonClikedSound;
    private final Game game;

    public TelaDeInicio(Game game) {
        this.game = game;
    }

    @Override
    public void show() {

        inicializarElementos();

        criarBotao();

        criarSom();


    }

    @Override
    public void hide() {
        System.out.println("TelaDeInicio - Hide method called");
        somAmbiente.pause();
        buttonClikedSound.pause();
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
        buttonTextureJogar.dispose();
        buttonTextureJogar.dispose();
        buttonTextureSair.dispose();
        buttonTextureSairPressionado.dispose();
        buttonTextureJogarPressionado.dispose();
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();
        somAmbiente.dispose();
        buttonClikedSound.dispose();


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

    private void inicializarElementos(){

        // Inicializar o viewport e stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Inicializar o SpriteBatch e a textura como campos da classe
        batch = new SpriteBatch();
        backgroundTexture = new Texture("imagens/backgrounds/backgroundTelaDeInicio.png");
        somAmbiente = Gdx.audio.newSound(Gdx.files.internal("sons/somAmbiente.mp3"));
        buttonClikedSound = Gdx.audio.newSound(Gdx.files.internal("sons/buttonCliked.wav"));

    }

    private void criarBotao(){
        //botãos normais
        buttonTextureJogar = new Texture("imagens/assets/TelaDeInicio/imagemBotaoJogar.png");
        buttonTextureSair = new Texture("imagens/assets/TelaDeInicio/imagemBotaoSair.png");
        //botãos pressionados
        buttonTextureJogarPressionado = new Texture("imagens/assets/TelaDeInicio/imagemBotaoJogarPressionado.png");
        buttonTextureSairPressionado = new Texture("imagens/assets/TelaDeInicio/imagemBotaoSairPressionado.png");

        //drawables dos botões
        TextureRegionDrawable buttonDrawableJogar = new TextureRegionDrawable(new TextureRegion(buttonTextureJogar));
        TextureRegionDrawable buttonDrawableJogarPressionado = new TextureRegionDrawable(new TextureRegion(buttonTextureJogarPressionado));
        TextureRegionDrawable buttonDrawableSair = new TextureRegionDrawable(new TextureRegion(buttonTextureSair));
        TextureRegionDrawable buttonDrawableSairPressionado = new TextureRegionDrawable(new TextureRegion(buttonTextureSairPressionado));

        //construtores dos botões
        ImageButton buttonJogar = new ImageButton(buttonDrawableJogar,buttonDrawableJogarPressionado);
        ImageButton buttonSair = new ImageButton(buttonDrawableSair,buttonDrawableSairPressionado);


        // Centralização do botão Jogar
        buttonJogar.setSize(buttonJogar.getWidth()*0.5f, buttonJogar.getHeight()*0.2f);
        buttonJogar.setPosition(
                (float) Gdx.graphics.getWidth()/2 - buttonJogar.getWidth()/2,
                (float) Gdx.graphics.getHeight()/2 - 100    // Um pouco acima do centro
        );

        // Centralização do botão Sair
        buttonSair.setSize(buttonSair.getWidth()*0.3f, buttonSair.getHeight()*0.15f);
        buttonSair.setPosition(
                (float) Gdx.graphics.getWidth()/2 - buttonSair.getWidth()/2,
                (float) Gdx.graphics.getHeight()/2 - 200   // Um pouco abaixo do centro
        );


        buttonJogar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                buttonClikedSound.play(1f);

                super.clicked(event, x, y);

                game.setScreen(new TelaDeEscolhaPersonagem(game));


            }
        });

        buttonSair.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                buttonClikedSound.play(1f);

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        Gdx.app.exit();
                    }
                },0.1f);

                super.clicked(event, x, y);
            }
        });


        stage.addActor(buttonSair);
        stage.addActor(buttonJogar);

    }

    private void criarSom(){

        long id = somAmbiente.play(2f);
        somAmbiente.setLooping(id,true);



    }

}

