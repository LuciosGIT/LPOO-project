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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import org.example.Ui.*;
import org.example.actor.*;
import org.example.ambientes.AmbienteFloresta;
import org.example.criatura.Corvo;
import org.example.criatura.Lobo;
import org.example.criatura.Morcego;
import org.example.criatura.Urso;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.enums.TipoClimatico;
import org.example.enums.TipoDescoberta;
import org.example.eventos.EventoClimatico;
import org.example.eventos.EventoCriatura;
import org.example.eventos.EventoDescoberta;
import org.example.utilitarios.Utilitario;
import org.example.utilitariosInterfaceGrafica.InicializarMundo;
import org.example.utilitariosInterfaceGrafica.Inputs;
import org.example.utilitariosInterfaceGrafica.VerificarStatusPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TelaDeJogoFloresta implements Screen {

    private final Game game;
    private final Personagem player;
    private final actorPersonagem actorPlayer;
    private actorArvore arvore;
    private List<actorArvore> listaDeArvores = new ArrayList<>();
    private actorPilhaDeItem pilhaDeItem;
    private Texture backgroundFloresta;
    private Batch batch;
    private Stage stage;
    private OrthographicCamera camera;
    private boolean isPilhaDeItemInstanciada;
    private actorMercador mercador;
    private List<Actor> listaDeCriaturas = new ArrayList<>();


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

    private final AmbienteFloresta ambienteFloresta;

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

    // Variáveis para o abrigo
    private actorAbrigo abrigo;
    private boolean abrigoVisivel = false;
    private EventoDescoberta eventoDescoberta;
    private boolean interacaoAbrigoOcorreu = false;

    private EventoCriatura eventoCriatura;

    private VerificarStatusPlayer verificarStatusPlayer;

    public TelaDeJogoFloresta(Game game, Personagem player) {
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);
        this.ambienteFloresta = new AmbienteFloresta(
                "Floresta",
                "Uma floresta densa e cheia de vida.",
                0.8,
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

        // Posicionar o mercador antes de criar as árvores
        float mercadorX = MathUtils.random(200, worldWidth - 200);
        float mercadorY = MathUtils.random(200, worldHeight - 200);
        this.mercador = new actorMercador("Mercador", stage);
        mercador.setPosition(mercadorX, mercadorY);

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

        // Gerar evento aleatório
        Evento evento = ambienteFloresta.gerarEvento(player);

        //Evento evento = new EventoCriatura(true,"Corvo","",0.5, new Lobo("1",10.0,0.2,0.1), 1.0);

        // Verificar o tipo de evento
        if (evento instanceof EventoDescoberta) {
            this.eventoDescoberta = (EventoDescoberta) evento;
            if (eventoDescoberta.getTipoDescoberta() == TipoDescoberta.ABRIGO) {
                // Se o abrigo foi spawnado, criar o actor
                if (eventoDescoberta.isAbrigoSpawnado()) {
                    // Posicionar o abrigo com espaçamento adequado
                    posicionarAbrigo();
                }
            }
        }else if (evento instanceof EventoClimatico) {
            aplicarClimaNaTela((EventoClimatico) evento);
        }else if (evento instanceof EventoCriatura) {

            this.eventoCriatura = (EventoCriatura) evento;

            if(eventoCriatura.getCriatura() instanceof Corvo){
                actorCorvo corvo = new actorCorvo( player, inventory, (Corvo) eventoCriatura.getCriatura());
                listaDeCriaturas.add(corvo);

            }else if(eventoCriatura.getCriatura() instanceof Lobo) {
                actorLobo lobo = new actorLobo(player, actorPlayer, inventory, (Lobo) eventoCriatura.getCriatura());
                listaDeCriaturas.add(lobo);

            }
            else if(eventoCriatura.getCriatura() instanceof Urso) {
                actorUrso urso = new actorUrso(player, actorPlayer, inventory, (Urso) eventoCriatura.getCriatura());
                listaDeCriaturas.add(urso);
            }
        }

        verificarStatusPlayer = new VerificarStatusPlayer(player);
    }

    // Método para posicionar o abrigo com espaçamento adequado
    private void posicionarAbrigo() {
        float espacoMinimoArvores = 300f; // Espaço mínimo entre abrigo e árvores
        float espacoMinimoMercador = 400f; // Espaço mínimo entre abrigo e mercador
        int maxTentativas = 50;
        boolean posicaoValida = false;
        float abrigoX = 0;
        float abrigoY = 0;

        // Tentar encontrar uma posição válida para o abrigo
        while (!posicaoValida && maxTentativas > 0) {
            abrigoX = MathUtils.random(200, worldWidth - 200);
            abrigoY = MathUtils.random(200, worldHeight - 200);
            posicaoValida = true;

            // Verificar distância com árvores
            for (actorArvore arvore : listaDeArvores) {
                float distanciaX = Math.abs(abrigoX - arvore.getX());
                float distanciaY = Math.abs(abrigoY - arvore.getY());

                if (distanciaX < espacoMinimoArvores && distanciaY < espacoMinimoArvores) {
                    posicaoValida = false;
                    break;
                }
            }

            // Verificar distância com o mercador
            if (posicaoValida) {
                float distanciaMercadorX = Math.abs(abrigoX - mercador.getX());
                float distanciaMercadorY = Math.abs(abrigoY - mercador.getY());

                if (distanciaMercadorX < espacoMinimoMercador && distanciaMercadorY < espacoMinimoMercador) {
                    posicaoValida = false;
                }
            }

            maxTentativas--;
        }

        // Se encontrou uma posição válida, criar o abrigo
        if (posicaoValida) {
            abrigo = new actorAbrigo(abrigoX, abrigoY);

            // Definir a ação a ser executada quando o abrigo for clicado
            abrigo.setOnClickAction(() -> {
                if (eventoDescoberta != null && !interacaoAbrigoOcorreu) {
                    // Jogador interage com o abrigo
                    eventoDescoberta.interagirComAbrigo(player);

                    // Marcar que a interação já ocorreu
                    interacaoAbrigoOcorreu = true;

                    // Remover abrigo após interação, se necessário
                    if (!eventoDescoberta.isAbrigoSpawnado()) {
                        abrigo.remove();
                        abrigo.dispose();
                        abrigo = null;
                        abrigoVisivel = false;
                    }

                    // Atualizar inventário após coletar itens
                    inventory.updateInventory();

                    // Exibir mensagem de interação
                    System.out.println("Você interagiu com o abrigo!");
                }
            });

            stage.addActor(abrigo);
            abrigoVisivel = true;

            // Exibir mensagem de descoberta
            System.out.println("Você encontrou um abrigo! Clique nele para interagir.");
        } else {
            // Se não encontrou posição válida após várias tentativas
            System.out.println("Não foi possível posicionar o abrigo adequadamente.");
            // Posicionar em um local padrão, se necessário
            abrigo = new actorAbrigo(worldWidth / 2, worldHeight / 2);

            // Definir a ação a ser executada quando o abrigo for clicado
            abrigo.setOnClickAction(() -> {
                if (eventoDescoberta != null && !interacaoAbrigoOcorreu) {
                    // Jogador interage com o abrigo
                    eventoDescoberta.interagirComAbrigo(player);

                    // Marcar que a interação já ocorreu
                    interacaoAbrigoOcorreu = true;

                    // Remover abrigo após interação, se necessário
                    if (!eventoDescoberta.isAbrigoSpawnado()) {
                        abrigo.remove();
                        abrigo.dispose();
                        abrigo = null;
                        abrigoVisivel = false;
                    }

                    // Atualizar inventário após coletar itens
                    inventory.updateInventory();

                    // Exibir mensagem de interação
                    System.out.println("Você interagiu com o abrigo!");
                }
            });

            stage.addActor(abrigo);
            abrigoVisivel = true;
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
        actorPlayer.checkCollision(mercador);
        if (abrigoVisivel && abrigo != null) {
            actorPlayer.checkCollision(abrigo);
        }
        sairDoCenario();

        popUp.setPosition(actorPlayer);
        inputs.inputListener(actorPlayer, inventory, popUp);

        for(Actor criatura : listaDeCriaturas){
            if(criatura instanceof actorCorvo) {
                stage.addActor(criatura);
                ((actorCorvo) criatura).ataque(actorPlayer);
            }
            else if(criatura instanceof actorLobo) {
                stage.addActor(criatura);

                // Add collision detection for the wolf with trees
                actorLobo lobo = (actorLobo) criatura;

                // Check collision with trees (assuming your actorLobo has a checkCollision method)
                lobo.checkCollisionWithTrees(listaDeArvores);

                // Also check collision with other obstacles
                lobo.checkCollisionWithMercador(mercador);
                if (abrigoVisivel && abrigo != null) {
                    lobo.checkCollisionWithAbrigo(abrigo);
                }

                if(lobo.getIsMorto()){
                    lobo.dispose();
                    System.out.print("oii");
                    lobo.remove();
                    listaDeCriaturas.remove(criatura);
                    return;
                }

                // Then perform the attack
                lobo.ataque();
                inventory.updateInventory();


            }

            else if (criatura instanceof actorUrso) {
                stage.addActor(criatura);

                actorUrso urso = (actorUrso) criatura;

                // Colisão com obstáculos
                urso.checkCollisionWithTrees(listaDeArvores);
                urso.checkCollisionWithMercador(mercador);
                if (abrigoVisivel && abrigo != null) {
                    urso.checkCollisionWithAbrigo(abrigo);
                }

                if(urso.getIsMorto()){
                    urso.dispose();
                    System.out.print("oii");
                    urso.remove();
                    listaDeCriaturas.remove(criatura);
                    return;
                }


                urso.ataque();
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

        popUp.dispose();

        if(soundForest != null) {
            soundForest.stop();
            soundForest.dispose();
        }

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

        if (abrigo != null) {
            abrigo.dispose();
        }

        if(listaDeCriaturas != null) {
            for (Actor criatura : listaDeCriaturas) {
                if (criatura instanceof actorCorvo) {
                    ((actorCorvo) criatura).dispose();
                } else if (criatura instanceof actorLobo) {
                    ((actorLobo) criatura).dispose();
                } else if (criatura instanceof actorUrso) {
                    ((actorUrso) criatura).dispose();
                }
            }
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
        float espacoMinimo = 300f; // Espaço mínimo entre árvores
        float espacoMinimoMercador = 400f; // Espaço mínimo entre árvores e o mercador
        float espacoMinimoAbrigo = 300f; // Espaço mínimo entre árvores e abrigo
        int maxTentativas = 30;
        int arvoresCriadas = 0;

        while (arvoresCriadas < 8 && maxTentativas > 0) {
            float posX = MathUtils.random(100, worldWidth - 100);
            float posY = MathUtils.random(100, worldHeight - 100);

            boolean posicaoValida = true;

            // Verificar distância com outras árvores
            for (actorArvore outraArvore : listaDeArvores) {
                float distanciaX = Math.abs(posX - outraArvore.getX());
                float distanciaY = Math.abs(posY - outraArvore.getY());

                if (distanciaX < espacoMinimo && distanciaY < espacoMinimo) {
                    posicaoValida = false;
                    break;
                }
            }

            // Verificar distância com o mercador
            if (posicaoValida) {
                float distanciaMercadorX = Math.abs(posX - mercador.getX());
                float distanciaMercadorY = Math.abs(posY - mercador.getY());

                if (distanciaMercadorX < espacoMinimoMercador && distanciaMercadorY < espacoMinimoMercador) {
                    posicaoValida = false;
                }
            }

            // Verificar distância com o abrigo, se existir
            if (posicaoValida && abrigoVisivel && abrigo != null) {
                float distanciaAbrigoX = Math.abs(posX - abrigo.getX());
                float distanciaAbrigoY = Math.abs(posY - abrigo.getY());

                if (distanciaAbrigoX < espacoMinimoAbrigo && distanciaAbrigoY < espacoMinimoAbrigo) {
                    posicaoValida = false;
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

                    // Verificar distância com o abrigo, se existir
                    boolean posicaoValida = true;
                    if (abrigoVisivel && abrigo != null) {
                        float distanciaAbrigoX = Math.abs(posx - abrigo.getX());
                        float distanciaAbrigoY = Math.abs(posy - abrigo.getY());

                        if (distanciaAbrigoX < 300f && distanciaAbrigoY < 300f) {
                            posicaoValida = false;
                        }
                    }

                    if (posicaoValida) {
                        pilhaDeItem = new actorPilhaDeItem(posx, posy, player, inventory, ambienteFloresta);
                        stage.addActor(pilhaDeItem);
                        isPilhaDeItemInstanciada = true;
                    }
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