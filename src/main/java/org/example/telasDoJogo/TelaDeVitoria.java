package org.example.telasDoJogo;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;

public class TelaDeVitoria implements Screen {

    private Game game;
    private Texture gameOverTexture;
    private SpriteBatch batch;
    private Stage stage;

    public TelaDeVitoria(Game game) {
        this.game = game;
    }

    @Override
    public void show() {

        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        gameOverTexture = new Texture("imagens/backgrounds/telaDeVitoria.png");

        sairDaTelaDeGameOver();

    }

    @Override
    public void resize(int width, int height) {
        // Implement resize logic here
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(gameOverTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Renderizar o stage se necessário
        stage.act(delta);
        stage.draw();
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
        if(gameOverTexture != null) {
            gameOverTexture.dispose();
        }
    }

    public void sairDaTelaDeGameOver() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {

                game.setScreen(new TelaDeInicio(game));
                dispose();
            }
        },5);
    }

}