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
import com.badlogic.gdx.utils.viewport.FitViewport;
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

        movement(deltaTime);
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
        // 1. Configuração da câmera
        camera = new OrthographicCamera(1920f, 1080f);

        // 2. Define dimensões do mundo
        worldWidth = 1920f;
        worldHeight = 1080f;

        // 3. Configuração do viewport e stage
        stage = new Stage(new FitViewport(worldWidth, worldHeight, camera));
        Gdx.input.setInputProcessor(stage);

        // 4. Batch e texturas
        batch = new SpriteBatch();
        backgroundFloresta = new Texture("imagens/backgrounds/mapaTelaDeJogoFloresta.png");

        // 5. Atualiza viewport
        viewportWidth = worldWidth;
        viewportHeight = worldHeight;

        // 6. Configuração do player
        actorPlayer.setTexture("parado");
        actorPlayer.setSize(64, 64);
        actorPlayer.setPosition(
                worldWidth / 2 - actorPlayer.getWidth() / 2,
                worldHeight / 2 - actorPlayer.getHeight() / 2
        );
        stage.addActor(actorPlayer);

        // 7. Aplica zoom por último
        camera.zoom = 0.5f; // Teste com 0.5 para ver se está funcionando
        camera.update();
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