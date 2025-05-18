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
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import org.example.Ui.Inventory;
import org.example.Ui.LifeBar;
import org.example.actor.actorArvore;
import org.example.actor.actorPersonagem;
import org.example.actor.actorPilhaDeItem;
import org.example.domain.Personagem;
import org.example.utilitariosInterfaceGrafica.InicializarMundo;
import org.example.utilitariosInterfaceGrafica.Inputs;

import java.util.ArrayList;
import java.util.List;

public class TelaDeJogoFloresta implements Screen {

    private final Game game;
    private final Personagem player;
    private final actorPersonagem actorPlayer;
    actorArvore arvore;
    private final List<actorArvore> listaDeArvores = new ArrayList<>();
    private actorPilhaDeItem pilhaDeItem;
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
    Inventory inventory;

    public TelaDeJogoFloresta(Game game, Personagem player){
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);

    }

    @Override
    public void show() {

        inicializarMundo = new InicializarMundo(actorPlayer,"imagens/backgrounds/mapaTelaDeJogoFloresta2.png");

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

        inventory = new Inventory(stage, 5, actorPlayer);

        pilhaDeItem = new actorPilhaDeItem(100, 100, player, inventory);
        stage.addActor(pilhaDeItem);

        stage.addActor(actorPlayer);

        criarActorArvore();
        inventory.updateInventory();


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
        inputs.inputListener(actorPlayer,inventory);
        lifeBar.setPosition(actorPlayer);
        lifeBar.setLifeBarValue(player.getVida());
        inventory.setPosition(camera);

        actorPlayer.checkCollision(listaDeArvores);
        sairDoCenario();


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
        for(actorArvore arvore : listaDeArvores){
            arvore.dispose();
        }

        inventory.dispose();

    }

    private void movement(float deltaTime) {

        float dx = 0, dy = 0;
        float moveSpeed = 200f;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += moveSpeed * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= moveSpeed * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= moveSpeed * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += moveSpeed * deltaTime;

        actorPlayer.moveBy(dx, dy);

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

    private void criarActorArvore() {

        float espacoMinimo = 250f; // Espaço mínimo entre árvores
        int maxTentativas = 30;    // Evita loop infinito
        int arvoresCriadas = 0;

        while(arvoresCriadas < 8 && maxTentativas > 0) {
            // Gera posição aleatória
            float posX = MathUtils.random(100, worldWidth - 100);
            float posY = MathUtils.random(100, worldHeight - 100);

            boolean posicaoValida = true;

            // Verifica distância com outras árvores
            for(actorArvore outraArvore : listaDeArvores) {
                float distanciaX = Math.abs(posX - outraArvore.getX());
                float distanciaY = Math.abs(posY - outraArvore.getY());

                // Se estiver muito perto de outra árvore
                if(distanciaX < espacoMinimo && distanciaY < espacoMinimo) {
                    posicaoValida = false;
                    break;
                }
            }

            // Se a posição for válida, cria a árvore
            if(posicaoValida) {
                actorArvore novaArvore = new actorArvore(posX, posY, player, inventory);
                listaDeArvores.add(novaArvore);
                stage.addActor(novaArvore);
                arvoresCriadas++;
            }

            maxTentativas--;

        }

    }

    private void sairDoCenario() {
        float playerX = actorPlayer.getX();
        float playerY = actorPlayer.getY();
        float playerWidth = actorPlayer.getWidth();
        float playerHeight = actorPlayer.getHeight();
        float margin = 10f; // margem de tolerância

        // Verifica cada borda
        boolean naBoradaEsquerda = playerX <= margin;
        boolean naBordaDireita = playerX + playerWidth >= worldWidth - margin;
        boolean naBordaSuperior = playerY + playerHeight >= worldHeight - margin;
        boolean naBordaInferior = playerY <= margin;

        if (naBoradaEsquerda || naBordaDireita || naBordaSuperior) {

            actorPlayer.addAction(Actions.fadeIn(0.1f));

            game.setScreen(new TelaDeJogoCaverna(game, player));
            dispose();

        }

        if (naBordaInferior) {
            actorPlayer.addAction(Actions.fadeIn(0.1f));

            game.setScreen(new TelaDeJogoLagoRio(game, player));
            dispose();
        }

    }




}


