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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import org.example.Ui.*;
import org.example.actor.actorLago;
import org.example.actor.actorPersonagem;
import org.example.actor.actorPilhaDeItem;
import org.example.actor.actorRio;
import org.example.actor.actorPonte;
import org.example.ambientes.AmbienteFloresta;
import org.example.ambientes.AmbienteLagoRio;
import org.example.criatura.Crocodilo;
import org.example.criatura.Morcego;
import org.example.domain.Personagem;
import org.example.enums.TipoClimatico;
import org.example.eventos.EventoCriatura;
import org.example.utilitarios.Utilitario;
import org.example.utilitariosInterfaceGrafica.InicializarMundo;
import org.example.utilitariosInterfaceGrafica.Inputs;
import com.badlogic.gdx.audio.Sound;
import org.example.actor.actorCrocodilo;
import org.example.domain.Evento;
import org.example.eventos.EventoCriatura;
import org.example.criatura.Corvo;
import org.example.actor.actorCorvo;
import org.example.utilitariosInterfaceGrafica.VerificarStatusPlayer;

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
    private Stage hudStage;
    private OrthographicCamera camera;
    private boolean isPilhaDeItemInstanciada;
    private List<Actor> listaDeCriaturas = new ArrayList<>();

    private float worldWidth;
    private float worldHeight;
    private float viewportWidth;
    private float viewportHeight;

    private Craft popUp;

    private Sound soundRiver;
    private long soundId;

    private Texture darkOverlay;
    private float currentTime = 12f;
    private float dayDuration = 300f;

    private AmbienteLagoRio ambienteLagoRio;

    InicializarMundo inicializarMundo;
    Inputs inputs;
    LifeBar lifeBar;
    HungerBar hungerBar; // <- NOVO
    Inventory inventory;

    private VerificarStatusPlayer verificarStatusPlayer;

    private Message message;

    public TelaDeJogoLagoRio(Game game, Personagem player) {
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);
        this.ambienteLagoRio = new AmbienteLagoRio("Lago e Rio", "Um lago e um rio", 0.8, Arrays.asList(TipoClimatico.TEMPESTADE, TipoClimatico.CALOR), true, player);
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

        // Cria o stage para HUD/interface fixa
        hudStage = new Stage();

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

        darkOverlay = new Texture(Gdx.files.internal("imagens/pixel.png"));

        ambienteLagoRio.explorar(player);

        //to do: adicionar evento do crocodilo

        Evento evento = ambienteLagoRio.gerarEvento(player);

        String mensagemEvento = "Bem-vindo ao lago e rio!";
        if (evento instanceof EventoCriatura eventoCriatura) {
            if (eventoCriatura.getCriatura() instanceof Crocodilo) {
                actorCrocodilo crocodilo = new actorCrocodilo(player, actorPlayer, inventory, (Crocodilo) eventoCriatura.getCriatura());
                stage.addActor(crocodilo);
                listaDeCriaturas.add(crocodilo);
                mensagemEvento = "Um crocodilo apareceu!";
            } else if (eventoCriatura.getCriatura() instanceof Corvo) {
                actorCorvo corvo = new actorCorvo(player, actorPlayer, inventory, (Corvo) eventoCriatura.getCriatura());
                stage.addActor(corvo);
                listaDeCriaturas.add(corvo);
                mensagemEvento = "Um corvo apareceu!";
            }
        }

        // Cria a mensagem e adiciona ao hudStage para ficar fixa na tela
        message = new Message(mensagemEvento, "Evento", hudStage, (viewportWidth - 300) / 2f, viewportHeight - 100);
        message.show();

        verificarStatusPlayer = new VerificarStatusPlayer(player);

    }

    public void instanciarPilhaDeItem() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isPilhaDeItemInstanciada && Utilitario.getValorAleatorio() < 0.1f) {
                    float posx = MathUtils.random(0, worldWidth - 100);
                    float posy = MathUtils.random(0, worldHeight - 100);

                    pilhaDeItem = new actorPilhaDeItem(posx, posy, player, inventory, ambienteLagoRio);
                    stage.addActor(pilhaDeItem);
                    isPilhaDeItemInstanciada = true;
                }
            }
        }, 0, 10);
    }

    @Override
    public void render(float delta) {
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f);
        float darkness = time();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundLagoRio, 0, 0, worldWidth, worldHeight);
        batch.setColor(0, 0, 0, darkness);
        batch.draw(darkOverlay, 0, 0, worldWidth, worldHeight);
        batch.setColor(1, 1, 1, 1);
        batch.end();

        stage.act(deltaTime);
        stage.draw();

        hudStage.act(deltaTime);
        hudStage.draw();

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

        for (int i = listaDeCriaturas.size() - 1; i >= 0; i--) {
            Actor actor = listaDeCriaturas.get(i);

            if (actor instanceof actorCrocodilo crocodilo) {
                if (crocodilo.getIsMorto()) {
                    crocodilo.dispose();
                    crocodilo.remove();
                    listaDeCriaturas.remove(i);
                    continue;
                }
                crocodilo.ataque();
            } else if (actor instanceof actorCorvo corvo) {
                if (corvo.getIsMorto()) {
                    corvo.dispose();
                    corvo.remove();
                    listaDeCriaturas.remove(i);
                    continue;
                }
                corvo.ataque();
            }
        }


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

    private float time() {
        currentTime += Gdx.graphics.getDeltaTime() * (24f / dayDuration);
        if (currentTime >= 24f) {
            currentTime = 0f;
        }

        float darkness;

        if (currentTime >= 18f || currentTime < 6f) {
            darkness = 0.5f;  // Maximum darkness changed to 0.5
        } else if (currentTime >= 6f && currentTime < 7f) {
            float t = (currentTime - 6f);
            darkness = 0.5f - (0.3f * t);  // Changed from 0.8f and 0.6f
        } else if (currentTime >= 17f && currentTime < 18f) {
            float t = (currentTime - 17f);
            darkness = 0.2f + (0.3f * t);  // Changed multiplier to 0.3f
        } else {
            darkness = 0.2f;  // Minimum darkness stays the same
        }

        return darkness;
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

        if (hudStage != null) {
            hudStage.dispose();
        }
    }
}
