package org.example.telasDoJogo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.example.Ui.Craft;
import org.example.Ui.Inventory;
import org.example.Ui.LifeBar;
import org.example.Ui.HungerBar;
import org.example.actor.actorPersonagem;
import org.example.actor.actorPilhaDeRecursos;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.utilitariosInterfaceGrafica.InicializarMundo;
import org.example.utilitariosInterfaceGrafica.Inputs;
import org.example.utilitariosInterfaceGrafica.VerificarStatusPlayer;

import java.util.List;

public class TelaDeJogoCavernaEvento implements Screen {

    private final Game game;
    private final Personagem player;
    private final actorPersonagem actorPlayer;
    private final List<Item> recursosDisponiveis;

    private Texture backgroundCaverna;
    private Batch batch;
    private Stage stage;
    private OrthographicCamera camera;

    private float worldWidth;
    private float worldHeight;
    private float viewportWidth;
    private float viewportHeight;

    private Sound soundCaverna;
    private long soundId;

    private actorPilhaDeRecursos pilhaDeRecursos;

    InicializarMundo inicializarMundo;
    Inputs inputs;
    LifeBar lifeBar;
    HungerBar hungerBar;
    Inventory inventory;
    private Craft popUp;
    private Stage hudStage;

    private VerificarStatusPlayer verificarStatusPlayer;

    public TelaDeJogoCavernaEvento(Game game, Personagem player, List<Item> recursosDisponiveis) {
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);
        this.recursosDisponiveis = recursosDisponiveis;
    }

    @Override
    public void show() {
        // Inicializar o mundo com o background da caverna
        inicializarMundo = new InicializarMundo(actorPlayer, "imagens/backgrounds/mapaTelaDeJogoCavernaEvento.png");

        this.camera = inicializarMundo.getCamera();
        this.stage = inicializarMundo.getStage();
        this.batch = inicializarMundo.getBatch();
        this.backgroundCaverna = inicializarMundo.getBackgroundFloresta();
        this.worldWidth = inicializarMundo.getWorldWidth();
        this.worldHeight = inicializarMundo.getWorldHeight();
        this.viewportWidth = inicializarMundo.getViewportWidth();
        this.viewportHeight = inicializarMundo.getViewportHeight();

        hudStage = new Stage();

        // Inicializar inputs
        inputs = new Inputs();

        // Inicializar barras de vida e fome
        lifeBar = new LifeBar(actorPlayer);
        stage.addActor(lifeBar.getLifeBar());

        hungerBar = new HungerBar(actorPlayer);
        stage.addActor(hungerBar.getHungerBar());

        // Inicializar inventário
        inventory = new Inventory(stage, 5, actorPlayer);

        // Adicionar o personagem à cena
        stage.addActor(actorPlayer);

        // Posicionar o personagem no centro da tela
        actorPlayer.setPosition(worldWidth / 2 - actorPlayer.getWidth() / 2, worldHeight / 2 - actorPlayer.getHeight() / 2);

        // Atualizar o inventário
        inventory.updateInventory();

        // Inicializar o popup de craft
        this.popUp = new Craft(stage, "Criar Item", "craftando", actorPlayer, inventory);

        // Criar a pilha de recursos
        criarPilhaDeRecursos();

        // Iniciar som ambiente da caverna
        try {
            soundCaverna = Gdx.audio.newSound(Gdx.files.internal("sons/soundCaverna.wav"));
            soundId = soundCaverna.loop(0.4f);
        } catch (Exception e) {
            System.out.println("Não foi possível carregar o som da caverna");
        }


        verificarStatusPlayer = new VerificarStatusPlayer(player);
    }

    private void criarPilhaDeRecursos() {
        // Posicionar a pilha de recursos em um local visível
        float posX = worldWidth / 2;
        float posY = worldHeight / 2 + 100;

        // Criar a pilha de recursos com os itens disponíveis
        pilhaDeRecursos = new actorPilhaDeRecursos(posX, posY, player, inventory, recursosDisponiveis);
        stage.addActor(pilhaDeRecursos);
    }

    @Override
    public void render(float delta) {
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundCaverna, 0, 0, worldWidth, worldHeight);
        batch.end();

        stage.act(deltaTime);
        stage.draw();

        // Métodos
        movement(deltaTime);
        camera();

        lifeBar.setPosition(actorPlayer);
        lifeBar.setLifeBarValue(player.getVida());

        hungerBar.setPosition(actorPlayer);
        hungerBar.setHungerValue(player.getFome());

        inventory.setPosition(camera);

        // Verificar se o jogador quer sair da caverna
        verificarSaida();

        inputs.inputListener(actorPlayer, inventory, popUp, hudStage);

        popUp.setPosition(actorPlayer);

        try {

            if (verificarStatusPlayer.getIsMorto()) {
                actorPlayer.addAction(Actions.fadeIn(0.1f));
                game.setScreen(new TelaDeGameOver(game));
                dispose();
            }else{
                verificarStatusPlayer.verificandoStatus();
            }

        }catch (Exception e) {
            System.out.println("Erro ao verificar status do player: " + e.getMessage());
        }

    }

    private void movement(float deltaTime) {
        float dx = 0, dy = 0;
        float moveSpeed = 150f;

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

    private void verificarSaida() {
        // Verificar se o jogador pressionou ESC para sair
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            voltarParaMontanha();
        }

        // Verificar se o jogador está na borda da tela
        float playerX = actorPlayer.getX();
        float playerY = actorPlayer.getY();
        float playerWidth = actorPlayer.getWidth();
        float playerHeight = actorPlayer.getHeight();
        float margin = 10f;

        boolean naBordaEsquerda = playerX <= margin;
        boolean naBordaDireita = playerX + playerWidth >= worldWidth - margin;
        boolean naBordaSuperior = playerY + playerHeight >= worldHeight - margin;
        boolean naBordaInferior = playerY <= margin;

        if (naBordaEsquerda || naBordaDireita || naBordaSuperior || naBordaInferior) {
            voltarParaMontanha();
        }
    }

    private void voltarParaMontanha() {
        actorPlayer.addAction(Actions.fadeOut(0.3f));
        game.setScreen(new TelaDeJogoMontanha(game, player));
        dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (soundCaverna != null) {
            soundCaverna.stop();
            soundCaverna.dispose();
        }

        inicializarMundo.dispose();

        if (pilhaDeRecursos != null) {
            pilhaDeRecursos.dispose();
        }

        inventory.dispose();

        if (lifeBar != null) {
            lifeBar.dispose();
        }

        if (hungerBar != null) {
            hungerBar.dispose();
        }
    }
}