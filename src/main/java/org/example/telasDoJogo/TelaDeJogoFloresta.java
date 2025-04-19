package org.example.telasDoJogo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import org.example.Ui.LifeBar;
import org.example.actor.actorPersonagem;
import org.example.domain.Personagem;
import org.example.utilitarios.telas.InicializarMundo;
import org.example.utilitarios.telas.Inputs;

public class TelaDeJogoFloresta implements Screen {

    private Game game;
    private Personagem player;
    private actorPersonagem actorPlayer;
    private Texture backgroundFloresta;
    private Batch batch;
    private Stage stage;
    private OrthographicCamera camera;

    private float worldWidth; // Largura do mundo
    private float worldHeight; // Altura do mundo
    private float viewportWidth; // Largura visível da câmera
    private float viewportHeight; // Altura visível da câmera

    InicializarMundo inicializarMundo;
    Inputs inputs;
    LifeBar lifeBar;

    public TelaDeJogoFloresta(Game game, Personagem player){
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);

    }

    @Override
    public void show() {
        inicializarMundo = new InicializarMundo(actorPlayer,"imagens/backgrounds/mapaTelaDeJogoFloresta.png");

        this.camera = inicializarMundo.getCamera();
        this.stage = inicializarMundo.getStage();
        this.batch = inicializarMundo.getBatch();
        this.backgroundFloresta = inicializarMundo.getBackgroundFloresta();
        this.worldWidth = inicializarMundo.getWorldWidth();
        this.worldHeight = inicializarMundo.getWorldHeight();
        this.viewportWidth = inicializarMundo.getViewportWidth();
        this.viewportHeight = inicializarMundo.getViewportHeight();

        inputs = new Inputs();

        lifeBar = new LifeBar(actorPlayer);
        stage.addActor(lifeBar.getLifeBar());

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.update();
    }

    @Override
    public void render(float delta) {

        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f);

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
        stage.act(deltaTime);
        stage.draw();

        //métodos
        movement(deltaTime);
        camera();
        inputs.inputListener();
        lifeBar.setPosition(actorPlayer);

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
        inicializarMundo.dispose();
        // Dispose of other resources if needed
    }

    private void movement(float deltaTime) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    // Limpa ações anteriores
                    actorPlayer.clearActions();

                    // Obtém coordenadas do clique e converte para coordenadas do mundo
                    Vector2 clickPos = stage.screenToStageCoordinates(
                            new Vector2(Gdx.input.getX(), Gdx.input.getY())
                    );

                    // Posição atual do personagem
                    Vector2 playerPos = new Vector2(actorPlayer.getX(), actorPlayer.getY());

                    // Velocidade constante em pixels por segundo
                    float velocidadeConstante = 150f;

                    // Calcula o tempo baseado em uma velocidade fixa
                    float distancia = playerPos.dst(clickPos);
                    float tempo = distancia / velocidadeConstante;

                    // Move o personagem
                    actorPlayer.addAction(Actions.moveTo(clickPos.x, clickPos.y, tempo));
                }
            }, 0.35f);
        }
    }

    private void camera() {
        float posX = actorPlayer.getX() + actorPlayer.getWidth() / 2;
        float posY = actorPlayer.getY() + actorPlayer.getHeight() / 2;

        float lerp = 0.1f;

        // Ajusta viewport pelo zoom
        float adjustedViewportWidth = viewportWidth * camera.zoom;
        float adjustedViewportHeight = viewportHeight * camera.zoom;

        float minX = adjustedViewportWidth / 2;
        float maxX = worldWidth - adjustedViewportWidth / 2;
        float minY = adjustedViewportHeight / 2;
        float maxY = worldHeight - adjustedViewportHeight / 2;


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