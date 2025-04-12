package org.example.telasDoJogo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.actor.actorPersonagem;
import org.example.domain.Personagem;

public class TelaDeJogoFloresta implements Screen {

    private Game game;
    private Personagem player;
    private actorPersonagem actorPlayer;
    private Texture backgroundFloresta;
    private Batch batch;
    private Stage stage;

    public TelaDeJogoFloresta(Game game, Personagem player){
        // Inicializa a tela de jogo com o personagem
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);
    }

    @Override
    public void show() {

        inicializar();
        actorPlayer.setTexture("parado");

        actorPlayer.setSize(64, 64);
        actorPlayer.setPosition( (float) Gdx.graphics.getWidth() /2-actorPlayer.getWidth()/2, (float) Gdx.graphics.getHeight() /2-actorPlayer.getHeight()/2);
        stage.addActor(actorPlayer);

    }

    @Override
    public void resize(int width, int height) {
        // Implement resize logic here
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        stage.getViewport().apply();

        batch.draw(backgroundFloresta, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.act(delta);
        stage.draw();

        movement();

    }

    @Override
    public void pause() {
        // Implement pause logic here
    }

    @Override
    public void resume() {
        // Implement resume logic here
    }

    @Override
    public void hide() {
        // Implement hide logic here
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundFloresta.dispose();
        stage.dispose();
        // Dispose of other resources if needed
    }

    private void inicializar() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();

        backgroundFloresta = new Texture("imagens/backgrounds/mapaTelaDeJogoFloresta.png");


    }

    private void movement() {

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Vector2 actorCoordenadas = stage.screenToStageCoordinates(new Vector2(x, y));
                    actorPlayer.addAction(Actions.moveTo(actorCoordenadas.x, actorCoordenadas.y, 4f));
                }
            },0.1f);



        }

    }

}


