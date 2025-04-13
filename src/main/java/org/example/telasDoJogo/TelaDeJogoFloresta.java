package org.example.telasDoJogo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
    private OrthographicCamera camera;
    private float tamanhoTelaX;
    private float tamanhoTelaY;

    private float worldWidth; // Largura do mundo
    private float worldHeight; // Altura do mundo
    private float viewportWidth; // Largura visível da câmera
    private float viewportHeight; // Altura visível da câmera


    public TelaDeJogoFloresta(Game game, Personagem player){
        // Inicializa a tela de jogo com o personagem
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);
    }

    @Override
    public void show() {

        inicializar();


    }

    @Override
    public void resize(int width, int height) {
        // Implement resize logic here
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        stage.getViewport().apply();

        batch.draw(backgroundFloresta,
                0, 0,
                worldWidth, worldHeight
        );
        batch.end();
        stage.act(delta);
        stage.draw();

        movement(delta);
        camera();

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

        //camera
        camera = (OrthographicCamera) stage.getCamera();
        tamanhoTelaX = Gdx.graphics.getWidth();
        tamanhoTelaY = Gdx.graphics.getHeight();

        worldWidth = backgroundFloresta.getWidth(); // Largura do mundo
        worldHeight = backgroundFloresta.getHeight(); // Altura do mundo
        viewportWidth = camera.viewportWidth * camera.zoom; // Largura visível da câmera
        viewportHeight = camera.viewportHeight * camera.zoom;

        //actor
        actorPlayer.setTexture("parado");
        actorPlayer.setSize(64, 64);
        actorPlayer.setPosition(
                worldWidth / 2 - actorPlayer.getWidth() / 2,
                worldHeight / 2 - actorPlayer.getHeight() / 2
        );

        stage.addActor(actorPlayer);



    }

    private void movement(float delta) {

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();

            Vector2 cordenadasPersonagem = new Vector2(actorPlayer.getX(), actorPlayer.getY());
            float distancia = (float) Math.pow(Math.pow(cordenadasPersonagem.x - x,2) + Math.pow(cordenadasPersonagem.y - y,2),0.5f);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Vector2 actorCoordenadas = stage.screenToStageCoordinates(new Vector2(x, y));
                    actorPlayer.addAction(Actions.moveTo(actorCoordenadas.x, actorCoordenadas.y, 4f));
                }
            },0.1f);



        }

    }

    private void camera() {
        // Calcula a posição desejada da câmera (centro do personagem)
        float posX = actorPlayer.getX() + actorPlayer.getWidth() / 2;
        float posY = actorPlayer.getY() + actorPlayer.getHeight() / 2;

        // Suavização do movimento da câmera
        float lerp = 0.1f;

        // Calcula os limites da câmera
        float minX = viewportWidth / 2;
        float maxX = worldWidth - viewportWidth / 2;
        float minY = viewportHeight / 2;
        float maxY = worldHeight - viewportHeight / 2;

        // Aplica interpolação com limites
        camera.position.x = MathUtils.clamp(
                camera.position.x + (posX - camera.position.x) * lerp,
                minX,
                maxX
        );

        camera.position.y = MathUtils.clamp(
                camera.position.y + (posY - camera.position.y) * lerp,
                minY,
                maxY
        );

        camera.update();
    }

}


