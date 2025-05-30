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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.example.Ui.*;
import org.example.actor.*;

import com.badlogic.gdx.utils.Timer;
import org.example.ambientes.AmbienteCaverna;
import org.example.criatura.Morcego;
import org.example.domain.Personagem;
import org.example.enums.TipoClimatico;
import org.example.utilitarios.Utilitario;
import org.example.utilitariosInterfaceGrafica.InicializarMundo;
import org.example.utilitariosInterfaceGrafica.Inputs;
import org.example.domain.Evento;
import org.example.eventos.EventoCriatura;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.criatura.Corvo;
import org.example.utilitariosInterfaceGrafica.VerificarStatusPlayer;

import java.util.List;
import java.util.ArrayList;

import java.util.Arrays;

public class TelaDeJogoCaverna implements Screen {

    private Game game;
    private Personagem player;
    private actorPersonagem actorPlayer;
    private actorRocha actorRocha;
    private List<actorRocha> listaDeRochas = new ArrayList<>();

    private Texture backgroundFloresta;
    private actorPilhaDeItem pilhaDeItem;
    private Batch batch;
    private Stage stage;
    private Stage hudStage;
    private OrthographicCamera camera;
    private boolean isPilhaDeItemInstanciada;

    private float worldWidth;
    private float worldHeight;
    private float viewportWidth;
    private float viewportHeight;

    private Craft popUp;
    private Sound soundCavern;
    private long soundId;
    private AmbienteCaverna ambienteCaverna;
    private Texture darkOverlay;
    private Texture lightTexture;

    private InicializarMundo inicializarMundo;
    private Inputs inputs;
    private LifeBar lifeBar;
    private HungerBar hungerBar;
    private Inventory inventory;
    private List<Actor> listaDeCriaturas = new ArrayList<>();

    private boolean isEventoRuinasAtivo = false;
    private float tempoEventoRuinas = 0;
    private final float DURACAO_EVENTO_RUINAS = 10f;
    private float tempoEntreEventos = 20f;
    private float tempoDesdeUltimoEvento = 0f;
    private float intensidadeTremor = 3.0f;
    private float offsetX = 0;
    private float offsetY = 0;
    private Sound somTremor;
    private long somTremorId;

    private VerificarStatusPlayer verificarStatusPlayer;

    private Message message;

    public TelaDeJogoCaverna(Game game, Personagem player) {
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);
        this.ambienteCaverna = new AmbienteCaverna("Caverna", "Um lugar escuro e misterioso", 0.8, Arrays.asList(TipoClimatico.CALOR), true, player);
    }

    @Override
    public void show() {
        inicializarMundo = new InicializarMundo(actorPlayer, "imagens/backgrounds/mapaTelaDeJogoCaverna.png");

        this.camera = inicializarMundo.getCamera();
        this.stage = inicializarMundo.getStage();
        this.batch = inicializarMundo.getBatch();
        this.backgroundFloresta = inicializarMundo.getBackgroundFloresta();
        this.worldWidth = inicializarMundo.getWorldWidth();
        this.worldHeight = inicializarMundo.getWorldHeight();
        this.viewportWidth = inicializarMundo.getViewportWidth();
        this.viewportHeight = inicializarMundo.getViewportHeight();

        hudStage = new Stage();

        inputs = new Inputs();

        lifeBar = new LifeBar(actorPlayer);
        stage.addActor(lifeBar.getLifeBar());

        isPilhaDeItemInstanciada = false;

        hungerBar = new HungerBar(actorPlayer);
        stage.addActor(hungerBar.getHungerBar());

        inventory = new Inventory(stage, 5, actorPlayer);
        stage.addActor(inventory.getInventoryTable());
        inventory.updateInventory();

        stage.addActor(actorPlayer);

        this.popUp = new Craft(stage, "Criar Item", "craftando", actorPlayer, inventory);

        this.soundCavern = Gdx.audio.newSound(Gdx.files.internal("sons/soudCavern.wav"));
        this.somTremor = Gdx.audio.newSound(Gdx.files.internal("sons/tremor.wav"));
        soundId = soundCavern.loop(0.6f);

        darkOverlay = new Texture(Gdx.files.internal("imagens/pixel.png"));
        lightTexture = new Texture(Gdx.files.internal("imagens/luz.png"));

        instanciarPilhaDeItem();
        instanciarRocha();
        iniciarEventosAleatorios();
        ambienteCaverna.explorar(player);

        Evento evento = ambienteCaverna.gerarEvento(player);

        String mensagemEvento = "Bem-vindo à caverna misteriosa...";
        if (evento instanceof EventoCriatura eventoCriatura) {
            if (eventoCriatura.getCriatura() instanceof Morcego morcegoCriatura) {
                actorMorcego morcego = new actorMorcego(player, actorPlayer, inventory, morcegoCriatura);
                listaDeCriaturas.add(morcego);
                stage.addActor(morcego);
                mensagemEvento = "Um morcego apareceu!";
            } else if (eventoCriatura.getCriatura() instanceof Corvo corvoCriatura) {
                actorCorvo corvo = new actorCorvo(player, actorPlayer, inventory, corvoCriatura);
                listaDeCriaturas.add(corvo);
                stage.addActor(corvo);
                mensagemEvento = "Um corvo apareceu!";
            }
        }

        // Cria a mensagem e adiciona ao hudStage para ficar fixa na tela
        message = new Message(mensagemEvento, "Evento", hudStage, (viewportWidth - 300) / 2f, viewportHeight - 100);
        message.show();

        verificarStatusPlayer = new VerificarStatusPlayer(player);

    }

    private void iniciarEventosAleatorios() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isEventoRuinasAtivo &&
                        tempoDesdeUltimoEvento >= tempoEntreEventos &&
                        MathUtils.random() < 0.8f) {
                    Gdx.app.log("TelaDeJogoCaverna", "Tentando iniciar evento de ruínas...");
                    iniciarEventoRuinas();
                }
            }
        }, 10, 10);
    }

    private void iniciarEventoRuinas() {
        isEventoRuinasAtivo = true;
        tempoEventoRuinas = 0;
        tempoDesdeUltimoEvento = 0;

        Gdx.app.log("TelaDeJogoCaverna", "Evento de ruínas iniciado! Duração: " + DURACAO_EVENTO_RUINAS + " segundos");

        if (somTremor != null) {
            somTremorId = somTremor.loop(0.7f);
            somTremor.setPitch(somTremorId, 0.8f);
        }
    }

    private void atualizarEventoRuinas(float deltaTime) {
        tempoDesdeUltimoEvento += deltaTime;

        if (!isEventoRuinasAtivo) {
            offsetX = 0;
            offsetY = 0;
            return;
        }

        tempoEventoRuinas += deltaTime;

        // Tremor com padrão senoidal
        float progress = tempoEventoRuinas / DURACAO_EVENTO_RUINAS;
        float intensidadeAtual = intensidadeTremor * (1 - progress);

        offsetX = MathUtils.sin(tempoEventoRuinas * 30) * intensidadeAtual;
        offsetY = MathUtils.cos(tempoEventoRuinas * 25) * intensidadeAtual;

        // Ajustar volume do som
        if (somTremor != null) {
            somTremor.setPan(somTremorId, offsetX / intensidadeTremor, 1 - progress);
        }

        if (tempoEventoRuinas >= DURACAO_EVENTO_RUINAS) {
            finalizarEventoRuinas();
        }
    }

    private void finalizarEventoRuinas() {
        isEventoRuinasAtivo = false;
        Gdx.app.log("TelaDeJogoCaverna", "Evento de ruínas finalizado!");

        if (somTremor != null) {
            somTremor.stop(somTremorId);
        }
        offsetX = 0;
        offsetY = 0;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.update();
    }

    @Override
    public void render(float delta) {
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f);

        atualizarEventoRuinas(deltaTime);

        if (isEventoRuinasAtivo) {
            camera.translate(offsetX, offsetY);
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundFloresta, 0, 0, worldWidth, worldHeight);
        batch.setColor(0, 0, 0, 0.0f);
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

        hungerBar.setPosition(actorPlayer);
        hungerBar.setHungerValue(player.getFome());

        sairDoCenario();

        inventory.setPosition(camera);
        inputs.inputListener(actorPlayer, inventory, popUp,hudStage);
        popUp.setPosition(actorPlayer);

        instanciarRocha();
        actorPlayer.checkCollision(listaDeRochas);

        if (isEventoRuinasAtivo) {
            camera.translate(-offsetX, -offsetY);
        }

        for (int i = listaDeCriaturas.size() - 1; i >= 0; i--) {
            Actor criatura = listaDeCriaturas.get(i);

            if (criatura instanceof actorMorcego morcego) {
                if (morcego.getIsMorto()) {
                    morcego.dispose();
                    morcego.remove();
                    listaDeCriaturas.remove(i);
                    continue;
                }
                morcego.ataque();
            } else if (criatura instanceof actorCorvo corvo) {
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

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        if (soundCavern != null) {
            soundCavern.stop(soundId);
        }
        if (somTremor != null) {
            somTremor.stop(somTremorId);
        }
    }

    @Override
    public void dispose() {
        if (darkOverlay != null) darkOverlay.dispose();
        if (lightTexture != null) lightTexture.dispose();
        if (soundCavern != null) {
            soundCavern.stop();
            soundCavern.dispose();
        }
        if (somTremor != null) {
            somTremor.stop();
            somTremor.dispose();
        }
        if (hungerBar != null) hungerBar.dispose();
        if (lifeBar != null) lifeBar.dispose();
        inicializarMundo.dispose();
        inventory.dispose();

        if (hudStage != null) {
            hudStage.dispose();
        }
    }

    public void instanciarPilhaDeItem() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {

                if (!isPilhaDeItemInstanciada && Utilitario.getValorAleatorio() < 0.1f) {
                    float posx = MathUtils.random(0, worldWidth - 100);
                    float posy = MathUtils.random(0, worldHeight - 100);

                    pilhaDeItem = new actorPilhaDeItem(posx, posy, player, inventory, ambienteCaverna);
                    stage.addActor(pilhaDeItem);
                    isPilhaDeItemInstanciada = true;
                }
            }
        }, 0, 10);
    }

    public void instanciarRocha(){

        int limiteRocha = 5;
        while (listaDeRochas.size() < limiteRocha) {
            float posx = MathUtils.random(0, worldWidth - 100);
            float posy = MathUtils.random(0, worldHeight - 100);

            actorRocha novaRocha = new actorRocha(posx, posy, player, inventory);

            if (!novaRocha.checkCollision(listaDeRochas)) {
                stage.addActor(novaRocha);
                listaDeRochas.add(novaRocha);
            }
        }

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

        float adjustedViewportWidth = viewportWidth * camera.zoom;
        float adjustedViewportHeight = viewportHeight * camera.zoom;

        float minX = adjustedViewportWidth / 2;
        float maxX = worldWidth - adjustedViewportWidth / 2;
        float minY = adjustedViewportHeight / 2;
        float maxY = worldHeight - adjustedViewportHeight / 2;

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

    private void sairDoCenario() {
        // Se evento estiver ativo, bloqueia saída
        if (isEventoRuinasAtivo) return;

        float playerX = actorPlayer.getX();
        float playerY = actorPlayer.getY();
        float playerWidth = actorPlayer.getWidth();
        float playerHeight = actorPlayer.getHeight();
        float margin = 10f;

        boolean naBoradaEsquerda = playerX <= margin;
        boolean naBordaDireita = playerX + playerWidth >= worldWidth - margin;
        boolean naBordaSuperior = playerY + playerHeight >= worldHeight - margin;
        boolean naBordaInferior = playerY <= margin;

        if (naBoradaEsquerda || naBordaDireita || naBordaInferior) {
            game.setScreen(new TelaDeJogoFloresta(game, player));
        }
        if (naBordaSuperior) {
            game.setScreen(new TelaDeJogoMontanha(game, player));
        }
    }
}
