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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import org.example.Ui.*;
import org.example.actor.actorArvore;
import org.example.actor.actorMercador;
import org.example.actor.actorPersonagem;
import org.example.actor.actorPilhaDeItem;
import org.example.ambientes.AmbienteFloresta;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.enums.TipoClimatico;
import org.example.utilitarios.Utilitario;
import org.example.utilitariosInterfaceGrafica.InicializarMundo;
import org.example.utilitariosInterfaceGrafica.Inputs;
import org.example.eventos.EventoClimatico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
    private boolean isPilhaDeItemInstanciada;
    private actorMercador mercador;

    private float worldWidth;
    private float worldHeight;
    private float viewportWidth;
    private float viewportHeight;

    private Craft popUp;
    private Sound soundForest;
    private long soundId;
    private Message message;


    private Texture darkOverlay;
    private float currentTime = 12f;
    private float dayDuration = 300f;

    private AmbienteFloresta ambienteFloresta;

    InicializarMundo inicializarMundo;
    Inputs inputs;
    LifeBar lifeBar;
    Inventory inventory;
    HungerBar hungerBar;

    // Variáveis para o efeito de chuva
    private boolean climaChuvosoAtivo = false;
    private Texture rainDropTexture;
    private Array<Rectangle> raindrops;
    private Random random;
    private float timeSinceLastDrop;

    public TelaDeJogoFloresta(Game game, Personagem player) {
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);
        this.ambienteFloresta = new AmbienteFloresta(
                "Floresta",
                "Uma floresta densa e cheia de vida.",
                5.0,
                Arrays.asList(TipoClimatico.TEMPESTADE, TipoClimatico.CALOR),
                true,
                true,
                true,
                player
        );
    }

    @Override
    public void show() {
        inicializarMundo = new InicializarMundo(actorPlayer, "imagens/backgrounds/mapaTelaDeJogoFloresta2.png");

        this.camera = inicializarMundo.getCamera();
        this.stage = inicializarMundo.getStage();
        this.batch = inicializarMundo.getBatch();
        this.backgroundFloresta = inicializarMundo.getBackgroundFloresta();
        this.worldWidth = inicializarMundo.getWorldWidth();
        this.worldHeight = inicializarMundo.getWorldHeight();
        this.viewportWidth = inicializarMundo.getViewportWidth();
        this.viewportHeight = inicializarMundo.getViewportHeight();

        lifeBar = new LifeBar(actorPlayer);
        stage.addActor(lifeBar.getLifeBar());

        hungerBar = new HungerBar(actorPlayer);
        stage.addActor(hungerBar.getHungerBar());

        this.isPilhaDeItemInstanciada = false;

        inventory = new Inventory(stage, 5, actorPlayer);
        stage.addActor(actorPlayer);

        criarActorArvore();
        inventory.updateInventory();

        this.popUp = new Craft(stage, "Criar Item", "craftando", actorPlayer, inventory);

        inputs = new Inputs();

        this.soundForest = Gdx.audio.newSound(Gdx.files.internal("sons/soundForest.wav"));
        soundId = soundForest.loop(0.6f);

        darkOverlay = new Texture(Gdx.files.internal("imagens/pixel.png"));

        ambienteFloresta.explorar(player);
        instanciarPilhaDeItem();

        // Inicializar componentes do efeito de chuva
        rainDropTexture = new Texture(Gdx.files.internal("imagens/assets/particles/chuva.png"));
        raindrops = new Array<Rectangle>();
        random = new Random();
        timeSinceLastDrop = 0;

        Evento evento = ambienteFloresta.gerarEvento(player);

        this.mercador = new actorMercador("Mercador", stage);



        if (evento instanceof EventoClimatico) {
            aplicarClimaNaTela((EventoClimatico) evento);
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.update();
    }

    @Override
    public void render(float delta) {
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f);
        float darkness = time();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundFloresta, 0, 0, worldWidth, worldHeight);
        batch.setColor(0, 0, 0, darkness);
        batch.draw(darkOverlay, 0, 0, worldWidth, worldHeight);
        batch.setColor(1, 1, 1, 1);

        // Renderizar efeito de chuva
        if (climaChuvosoAtivo) {
            // Calcular a área visível da câmera
            float leftEdge = camera.position.x - viewportWidth/2 * camera.zoom;
            float rightEdge = camera.position.x + viewportWidth/2 * camera.zoom;
            float topEdge = camera.position.y + viewportHeight/2 * camera.zoom;
            float bottomEdge = camera.position.y - viewportHeight/2 * camera.zoom;

            // Atualizar o tempo desde a última gota
            timeSinceLastDrop += delta;

            // Adicionar novas gotas
            if (timeSinceLastDrop > 0.005f) { // Ajuste este valor para controlar a intensidade da chuva
                timeSinceLastDrop = 0;

                // Criar várias gotas por frame para uma chuva mais densa
                for (int i = 0; i < 5; i++) {
                    Rectangle raindrop = new Rectangle();
                    raindrop.x = leftEdge + random.nextFloat() * (rightEdge - leftEdge);
                    raindrop.y = topEdge;
                    raindrop.width = 1 + random.nextFloat() * 2; // Largura variável
                    raindrop.height = 10 + random.nextFloat() * 15; // Altura variável
                    raindrops.add(raindrop);
                }
            }

            // Atualizar e desenhar as gotas existentes
            for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
                Rectangle raindrop = iter.next();
                raindrop.y -= (400 + random.nextFloat() * 200) * delta; // Velocidade variável

                // Remover gotas que saíram da tela
                if (raindrop.y < bottomEdge) {
                    iter.remove();
                } else {
                    batch.draw(rainDropTexture, raindrop.x, raindrop.y, raindrop.width, raindrop.height);
                }
            }
        }

        batch.end();

        stage.act(deltaTime);
        stage.draw();

        movement(deltaTime);
        camera();
        lifeBar.setPosition(actorPlayer);
        lifeBar.setLifeBarValue(player.getVida());

        hungerBar.setPosition(actorPlayer);
        hungerBar.setHungerValue(player.getFome());

        inventory.setPosition(camera);

        actorPlayer.checkCollision(listaDeArvores);
        sairDoCenario();

        popUp.setPosition(actorPlayer);
        inputs.inputListener(actorPlayer, inventory, popUp);
    }

    private void aplicarClimaNaTela(EventoClimatico eventoClimatico) {
        if (eventoClimatico.getTipoDeClima() == TipoClimatico.TEMPESTADE) {
            climaChuvosoAtivo = true;
        } else {
            climaChuvosoAtivo = false;
        }
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {

        if(mercador != null) {
            mercador.dispose();
        }

        if (pilhaDeItem != null) {
            pilhaDeItem.dispose();
        }

        if (soundForest != null) {
            soundForest.stop();
            soundForest.dispose();
        }

        inicializarMundo.dispose();
        for (actorArvore arvore : listaDeArvores) {
            arvore.dispose();
        }

        inventory.dispose();
        hungerBar.dispose();

        if (rainDropTexture != null) {
            rainDropTexture.dispose();
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

    private void criarActorArvore() {
        float espacoMinimo = 250f;
        int maxTentativas = 30;
        int arvoresCriadas = 0;

        while (arvoresCriadas < 8 && maxTentativas > 0) {
            float posX = MathUtils.random(100, worldWidth - 100);
            float posY = MathUtils.random(100, worldHeight - 100);

            boolean posicaoValida = true;

            for (actorArvore outraArvore : listaDeArvores) {
                float distanciaX = Math.abs(posX - outraArvore.getX());
                float distanciaY = Math.abs(posY - outraArvore.getY());

                if (distanciaX < espacoMinimo && distanciaY < espacoMinimo) {
                    posicaoValida = false;
                    break;
                }
            }

            if (posicaoValida) {
                actorArvore novaArvore = new actorArvore(posX, posY, player, inventory);
                listaDeArvores.add(novaArvore);
                stage.addActor(novaArvore);
                arvoresCriadas++;
            }

            maxTentativas--;
        }
    }

    private float time() {
        currentTime += Gdx.graphics.getDeltaTime() * (24f / dayDuration);
        if (currentTime >= 24f) {
            currentTime = 0f;
        }

        float darkness;
        if (currentTime >= 18f || currentTime < 6f) {
            darkness = 0.8f;
        } else if (currentTime >= 6f && currentTime < 7f) {
            float t = (currentTime - 6f);
            darkness = 0.8f - (0.6f * t);
        } else if (currentTime >= 17f && currentTime < 18f) {
            float t = (currentTime - 17f);
            darkness = 0.2f + (0.6f * t);
        } else {
            darkness = 0.2f;
        }

        return darkness;
    }

    public void instanciarPilhaDeItem() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isPilhaDeItemInstanciada && Utilitario.getValorAleatorio() < 0.1f) {
                    float posx = MathUtils.random(0, worldWidth - 100);
                    float posy = MathUtils.random(0, worldHeight - 100);

                    pilhaDeItem = new actorPilhaDeItem(posx, posy, player, inventory, ambienteFloresta);
                    stage.addActor(pilhaDeItem);
                    isPilhaDeItemInstanciada = true;
                }
            }
        }, 0, 10);
    }

    private void sairDoCenario() {
        float playerX = actorPlayer.getX();
        float playerY = actorPlayer.getY();
        float playerWidth = actorPlayer.getWidth();
        float playerHeight = actorPlayer.getHeight();
        float margin = 10f;

        boolean naBoradaEsquerda = playerX <= margin;
        boolean naBordaDireita = playerX + playerWidth >= worldWidth - margin;
        boolean naBordaSuperior = playerY + playerHeight >= worldHeight - margin;
        boolean naBordaInferior = playerY <= margin;

        if (naBoradaEsquerda || naBordaSuperior) {
            actorPlayer.addAction(Actions.fadeIn(0.1f));
            game.setScreen(new TelaDeJogoCaverna(game, player));
            dispose();
        }

        if (naBordaDireita) {
            actorPlayer.addAction(Actions.fadeIn(0.1f));
            game.setScreen(new TelaDeJogoRuinas(game, player));
            dispose();
        }

        if (naBordaInferior) {
            actorPlayer.addAction(Actions.fadeIn(0.1f));
            game.setScreen(new TelaDeJogoLagoRio(game, player));
            dispose();
        }
    }
}