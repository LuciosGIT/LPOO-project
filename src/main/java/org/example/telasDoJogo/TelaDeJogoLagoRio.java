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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import org.example.Ui.HungerBar;
import org.example.Ui.Inventory;
import org.example.Ui.LifeBar;
import org.example.actor.actorLago;
import org.example.actor.actorPersonagem;
import org.example.actor.actorPilhaDeItem;
import org.example.actor.actorRio;
import org.example.actor.actorPonte;
import org.example.ambientes.AmbienteFloresta;
import org.example.ambientes.AmbienteLagoRio;
import org.example.domain.Personagem;
import org.example.enums.TipoClimatico;
import org.example.utilitarios.Utilitario;
import org.example.utilitariosInterfaceGrafica.InicializarMundo;
import org.example.utilitariosInterfaceGrafica.Inputs;
import org.example.Ui.Craft;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelaDeJogoLagoRio implements Screen {

    private final Game game;
    private final Personagem player;
    private final actorPersonagem actorPlayer;
    private final List<actorLago> listaDeLagos = new ArrayList<>();
    private actorPilhaDeItem pilhaDeItem;
    private actorRio rioSuperior;
    private actorRio rioInferior;
    private actorRio rioVisual;
    private actorPonte ponte;
    private Texture backgroundLagoRio;
    private Batch batch;
    private Stage stage;
    private OrthographicCamera camera;
    private boolean isPilhaDeItemInstanciada;

    private float worldWidth;
    private float worldHeight;
    private float viewportWidth;
    private float viewportHeight;

    private Craft popUp;

    private Sound soundRiver;
    private long soundId;

    private AmbienteLagoRio ambienteLagoRio;

    InicializarMundo inicializarMundo;
    Inputs inputs;
    LifeBar lifeBar;
    HungerBar hungerBar; // <- NOVO
    Inventory inventory;

    public TelaDeJogoLagoRio(Game game, Personagem player) {
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);
        this.ambienteLagoRio = new AmbienteLagoRio("Lago e Rio", "Um lago e um rio", 5.0, Arrays.asList(TipoClimatico.TEMPESTADE, TipoClimatico.CALOR), true, player);
    }

    @Override
    public void show() {
        inicializarMundo = new InicializarMundo(actorPlayer, "imagens/backgrounds/mapaTelaDeJogoLagoRio.png");

        this.camera = inicializarMundo.getCamera();
        this.stage = inicializarMundo.getStage();
        this.batch = inicializarMundo.getBatch();
        this.backgroundLagoRio = inicializarMundo.getBackgroundFloresta();
        this.worldWidth = inicializarMundo.getWorldWidth();
        this.worldHeight = inicializarMundo.getWorldHeight();
        this.viewportWidth = inicializarMundo.getViewportWidth();
        this.viewportHeight = inicializarMundo.getViewportHeight();

        this.isPilhaDeItemInstanciada = false;

        inventory = new Inventory(stage, 5, actorPlayer);

        criarActorsLagosRioEPonte();

        stage.addActor(actorPlayer);
        inventory.updateInventory();

        lifeBar = new LifeBar(actorPlayer);
        stage.addActor(lifeBar.getLifeBar());

        hungerBar = new HungerBar(actorPlayer); // <- NOVO
        stage.addActor(hungerBar.getHungerBar()); // <- NOVO

        instanciarPilhaDeItem();

        this.popUp = new Craft(stage, "Cria Item", "Pressione 'C' para abrir o inventário", actorPlayer, inventory);

        inputs = new Inputs();

        soundRiver = Gdx.audio.newSound(Gdx.files.internal("sons/soundLake.wav"));
        soundId = soundRiver.loop(0.5f);

        ambienteLagoRio.explorar(player);
    }

    public void instanciarPilhaDeItem() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isPilhaDeItemInstanciada && Utilitario.getValorAleatorio() < 0.1f) {
                    pilhaDeItem = new actorPilhaDeItem(100, 100, player, inventory, ambienteLagoRio);
                    stage.addActor(pilhaDeItem);
                    isPilhaDeItemInstanciada = true;
                }
            }
        }, 0, 2);
    }

    @Override
    public void render(float delta) {
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        stage.getViewport().apply();
        batch.draw(backgroundLagoRio, 0, 0, worldWidth, worldHeight);
        batch.end();

        stage.act(deltaTime);
        stage.draw();

        movement(deltaTime);
        camera();

        lifeBar.setPosition(actorPlayer);
        lifeBar.setLifeBarValue(player.getVida());

        hungerBar.setPosition(actorPlayer); // <- NOVO
        hungerBar.setHungerValue(player.getFome()); // <- NOVO

        inventory.setPosition(camera);

        actorPlayer.checkCollision(listaDeLagos);
        verificarColisaoComRio(deltaTime);

        sairDoCenario();

        popUp.setPosition(actorPlayer);
        inputs.inputListener(actorPlayer, inventory, popUp);
        popUp.setPosition(actorPlayer);
    }

    private void verificarColisaoComRio(float deltaTime) {
        if (rioSuperior != null && actorPlayer.checkCollision(rioSuperior)) {
            rioSuperior.aplicarEfeitoDeCorrente(actorPlayer, deltaTime);
        }

        if (rioInferior != null && actorPlayer.checkCollision(rioInferior)) {
            rioInferior.aplicarEfeitoDeCorrente(actorPlayer, deltaTime);
        }
    }

    private void movement(float deltaTime) {
        float dx = 0, dy = 0;
        float moveSpeed = 135f;

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

        float adjustedViewportWidth = viewportWidth * camera.zoom;
        float adjustedViewportHeight = viewportHeight * camera.zoom;

        float minX = adjustedViewportWidth / 2;
        float maxX = worldWidth - adjustedViewportWidth / 2;
        float minY = adjustedViewportHeight / 2;
        float maxY = worldHeight - adjustedViewportHeight / 2;

        camera.position.x = MathUtils.clamp(camera.position.x + (posX - camera.position.x) * lerp, minX, maxX);
        camera.position.y = MathUtils.clamp(camera.position.y + (posY - camera.position.y) * lerp, minY, maxY);
        camera.update();
    }

    private void sairDoCenario() {
        float playerX = actorPlayer.getX();
        float playerY = actorPlayer.getY();
        float playerWidth = actorPlayer.getWidth();
        float playerHeight = actorPlayer.getHeight();
        float margin = 10f;

        boolean naBordaEsquerda = playerX <= margin;
        boolean naBordaDireita = playerX + playerWidth >= worldWidth - margin;
        boolean naBordaSuperior = playerY + playerHeight >= worldHeight - margin;
        boolean naBordaInferior = playerY <= margin;

        if (naBordaEsquerda || naBordaDireita || naBordaSuperior) {
            actorPlayer.addAction(Actions.fadeIn(0.1f));
            game.setScreen(new TelaDeJogoFloresta(game, player));
            dispose();
        }
    }

    private void criarActorsLagosRioEPonte() {
        float espacoMinimo = 350f;
        int maxLagos = 10;
        int maxTentativas = 50;
        int lagosCriados = 0;

        float rioLargura = 500;
        float rioX = (worldWidth - rioLargura) / 2;
        float alturaPonte = 250;
        float deslocamentoEsquerda = 2f;
        float ponteY = (worldHeight - alturaPonte) / 2;
        float ponteX = rioX - deslocamentoEsquerda;

        rioVisual = new actorRio(rioX, 0, rioLargura, worldHeight, player, inventory);
        ponte = new actorPonte(ponteX, ponteY, rioLargura, alturaPonte);

        stage.addActor(rioVisual);
        stage.addActor(ponte);

        rioSuperior = new actorRio(rioX, 0, rioLargura, ponteY, player, inventory);
        float yRioInferior = ponteY + alturaPonte;
        float alturaRioInferior = worldHeight - yRioInferior;
        rioInferior = new actorRio(rioX, yRioInferior, rioLargura, alturaRioInferior, player, inventory);

        while (lagosCriados < maxLagos && maxTentativas > 0) {
            float posX = MathUtils.random(100, worldWidth - 100);
            float posY = MathUtils.random(100, worldHeight - 100);

            boolean posicaoValida = true;
            for (actorLago lago : listaDeLagos) {
                float dx = Math.abs(posX - lago.getX());
                float dy = Math.abs(posY - lago.getY());
                if (dx < espacoMinimo && dy < espacoMinimo) {
                    posicaoValida = false;
                    break;
                }
            }

            if (posicaoValida) {
                if (posX + 100 > rioX - 200 && posX < rioX + rioLargura + 200) {
                    posicaoValida = false;
                }
            }

            if (posicaoValida) {
                actorLago novoLago = new actorLago(posX, posY, player, inventory);
                listaDeLagos.add(novoLago);
                stage.addActor(novoLago);
                lagosCriados++;
            }

            maxTentativas--;
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.update();
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (soundRiver != null) {
            soundRiver.stop();
            soundRiver.dispose();
        }

        inicializarMundo.dispose();
        inventory.dispose();
        for (actorLago lago : listaDeLagos) {
            lago.dispose();
        }
        if (rioSuperior != null) rioSuperior.dispose();
        if (rioInferior != null) rioInferior.dispose();
        if (rioVisual != null) rioVisual.dispose();
        if (hungerBar != null) hungerBar.dispose(); // <- NOVO
    }
}
