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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import org.example.Ui.*;
import org.example.actor.actorCaverna;
import org.example.actor.actorCristal;
import org.example.actor.actorPersonagem;
import org.example.actor.actorPilhaDeItem;
import org.example.ambientes.AmbienteMontanha;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.enums.TipoClimatico;
import org.example.enums.TipoDescoberta;
import org.example.eventos.EventoClimatico;
import org.example.eventos.EventoDescoberta;
import org.example.personagens.Sobrevivente;
import org.example.utilitarios.Utilitario;
import org.example.utilitariosInterfaceGrafica.InicializarMundo;
import org.example.utilitariosInterfaceGrafica.Inputs;
import com.badlogic.gdx.audio.Sound;
import org.example.utilitariosInterfaceGrafica.VerificarStatusPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TelaDeJogoMontanha implements Screen {

    private final Game game;
    private final Personagem player;
    private final actorPersonagem actorPlayer;
    private actorCristal cristal;
    private final List<actorCristal> listaDeCristais = new ArrayList<>();
    private actorPilhaDeItem pilhaDeItem;
    private boolean isPilhaDeItemInstanciada;
    private Texture backgroundMontanha;
    private Batch batch;
    private Stage stage;
    private Stage hudStage;
    private OrthographicCamera camera;

    private Craft popUp;

    private float worldWidth; // Largura do mundo
    private float worldHeight; // Altura do mundo
    private float viewportWidth; // Largura visível da câmera
    private float viewportHeight; // Altura visível da câmera

    private Sound soundMountain;
    private long soundId;

    private Texture darkOverlay;
    private float currentTime = 12f;
    private float dayDuration = 300f;

    private final AmbienteMontanha ambienteMontanha;

    InicializarMundo inicializarMundo;
    Inputs inputs;
    LifeBar lifeBar;
    HungerBar hungerBar;
    Inventory inventory;

    // Variáveis para o efeito de neve
    private boolean climaNeveAtivo = false;
    private Texture snowflakeTexture;
    private Array<Rectangle> snowflakes;
    private Random random;
    private float timeSinceLastSnowflake;

    // Variáveis para a caverna
    private actorCaverna caverna;
    private boolean cavernaVisivel = false;
    private EventoDescoberta eventoDescoberta;
    private boolean interacaoCavernaOcorreu = false;

    private VerificarStatusPlayer verificarStatusPlayer;

    private Message message;

    public TelaDeJogoMontanha(Game game, Personagem player){
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);
        this.ambienteMontanha = new AmbienteMontanha("Montanha", "Pico de um monte gelado",0.9, List.of(TipoClimatico.TEMPESTADE, TipoClimatico.NEVASCA), true, false, player);
    }

    @Override
    public void show() {
        inicializarMundo = new InicializarMundo(actorPlayer,"imagens/backgrounds/mapaTelaDeJogoMontanha.png");

        this.camera = inicializarMundo.getCamera();
        this.stage = inicializarMundo.getStage();
        this.batch = inicializarMundo.getBatch();
        this.backgroundMontanha = inicializarMundo.getBackgroundFloresta();
        this.worldWidth = inicializarMundo.getWorldWidth();
        this.worldHeight = inicializarMundo.getWorldHeight();
        this.viewportWidth = inicializarMundo.getViewportWidth();
        this.viewportHeight = inicializarMundo.getViewportHeight();

        inputs = new Inputs();

        // Cria o stage para HUD/interface fixa
        hudStage = new Stage();

        lifeBar = new LifeBar(actorPlayer);
        stage.addActor(lifeBar.getLifeBar());

        isPilhaDeItemInstanciada = false;

        hungerBar = new HungerBar(actorPlayer);
        stage.addActor(hungerBar.getHungerBar());

        inventory = new Inventory(stage, 5, actorPlayer);

        stage.addActor(actorPlayer);

        criarActorCristal();
        inventory.updateInventory();

        this.popUp = new Craft(stage, "Criar Item", "craftando", actorPlayer, inventory);

        soundMountain = Gdx.audio.newSound(Gdx.files.internal("sons/soundMount.wav"));
        soundId = soundMountain.loop(0.5f);

        darkOverlay = new Texture(Gdx.files.internal("imagens/pixel.png"));

        // Inicializar componentes do efeito de neve
        snowflakeTexture = new Texture(Gdx.files.internal("imagens/assets/particles/neve.png"));
        snowflakes = new Array<Rectangle>();
        random = new Random();
        timeSinceLastSnowflake = 0;

        instanciarPilhaDeItem();

        ambienteMontanha.explorar(player);

        // Gerar evento aleatório
        Evento evento = ambienteMontanha.gerarEvento(player);

        String mensagemEvento = "Bem-vindo à montanha gelada!";
        // Verificar o tipo de evento
        if (evento instanceof EventoDescoberta) {
            this.eventoDescoberta = (EventoDescoberta) evento;
            if (eventoDescoberta.getTipoDescoberta() == TipoDescoberta.CAVERNA) {
                if (eventoDescoberta.isCavernaSpawnada()) {
                    posicionarCaverna();
                }
                mensagemEvento = "Você encontrou uma caverna!";
            }
        } else if (evento instanceof EventoClimatico) {
            aplicarClimaNaTela((EventoClimatico) evento);
            mensagemEvento = "O clima mudou: " + ((EventoClimatico) evento).getTipoDeClima().name().toLowerCase();
        }

        // Cria a mensagem e adiciona ao hudStage para ficar fixa na tela
        message = new Message(mensagemEvento, "Evento", hudStage, (viewportWidth - 300) / 2f, viewportHeight - 100);
        message.show();

        verificarStatusPlayer = new VerificarStatusPlayer(player);

    }

    // Método para posicionar a caverna com espaçamento adequado
    private void posicionarCaverna() {
        float espacoMinimoCristais = 300f; // Espaço mínimo entre caverna e cristais
        int maxTentativas = 50;
        boolean posicaoValida = false;
        float cavernaX = 0;
        float cavernaY = 0;

        // Tentar encontrar uma posição válida para a caverna
        while (!posicaoValida && maxTentativas > 0) {
            cavernaX = MathUtils.random(200, worldWidth - 200);
            cavernaY = MathUtils.random(200, worldHeight - 200);
            posicaoValida = true;

            // Verificar distância com cristais
            for (actorCristal cristal : listaDeCristais) {
                float distanciaX = Math.abs(cavernaX - cristal.getX());
                float distanciaY = Math.abs(cavernaY - cristal.getY());

                if (distanciaX < espacoMinimoCristais && distanciaY < espacoMinimoCristais) {
                    posicaoValida = false;
                    break;
                }
            }

            // Verificar distância com a pilha de item, se existir
            if (posicaoValida && isPilhaDeItemInstanciada && pilhaDeItem != null) {
                float distanciaPilhaX = Math.abs(cavernaX - pilhaDeItem.getX());
                float distanciaPilhaY = Math.abs(cavernaY - pilhaDeItem.getY());

                if (distanciaPilhaX < espacoMinimoCristais && distanciaPilhaY < espacoMinimoCristais) {
                    posicaoValida = false;
                }
            }

            maxTentativas--;
        }

        // Se encontrou uma posição válida, criar a caverna
        if (posicaoValida) {
            caverna = new actorCaverna(cavernaX, cavernaY);

            // Definir a ação a ser executada quando a caverna for clicada
            caverna.setOnClickAction(() -> {
                if (eventoDescoberta != null && !interacaoCavernaOcorreu) {
                    // Verificar se o jogador é um Sobrevivente
                    if (player instanceof Sobrevivente) {
                        // Tentar interagir com a caverna
                        boolean podeExplorar = eventoDescoberta.interagirComCaverna(player);

                        if (podeExplorar) {
                            // Marcar que a interação já ocorreu
                            interacaoCavernaOcorreu = true;

                            // Abrir a tela de exploração da caverna
                            game.setScreen(new TelaDeJogoCavernaEvento(game, player, eventoDescoberta.getRecursosEncontrados()));
                        }
                    } else {
                        // Jogador não é um Sobrevivente
                        System.out.println("Você não tem as habilidades necessárias para explorar esta caverna.");
                    }
                }
            });

            stage.addActor(caverna);
            cavernaVisivel = true;

            // Exibir mensagem de descoberta
            System.out.println("Você encontrou uma caverna! Clique nela para explorar.");
        } else {
            // Se não encontrou posição válida após várias tentativas
            System.out.println("Não foi possível posicionar a caverna adequadamente.");
            // Posicionar em um local padrão, se necessário
            caverna = new actorCaverna(worldWidth / 2, worldHeight / 2);

            // Definir a ação a ser executada quando a caverna for clicada
            caverna.setOnClickAction(() -> {
                if (eventoDescoberta != null && !interacaoCavernaOcorreu) {
                    // Verificar se o jogador é um Sobrevivente
                    if (player instanceof Sobrevivente) {
                        // Tentar interagir com a caverna
                        boolean podeExplorar = eventoDescoberta.interagirComCaverna(player);

                        if (podeExplorar) {
                            // Marcar que a interação já ocorreu
                            interacaoCavernaOcorreu = true;

                            // Abrir a tela de exploração da caverna
                            game.setScreen(new TelaDeJogoCavernaEvento(game, player, eventoDescoberta.getRecursosEncontrados()));
                        }
                    } else {
                        // Jogador não é um Sobrevivente
                        System.out.println("Você não tem as habilidades necessárias para explorar esta caverna.");
                    }
                }
            });

            stage.addActor(caverna);
            cavernaVisivel = true;
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
        batch.draw(backgroundMontanha, 0, 0, worldWidth, worldHeight);
        batch.setColor(0, 0, 0, darkness);
        batch.draw(darkOverlay, 0, 0, worldWidth, worldHeight);
        batch.setColor(1, 1, 1, 1);

        // Renderizar efeito de neve lateral
        if (climaNeveAtivo) {
            // Calcular a área visível da câmera
            float leftEdge = camera.position.x - viewportWidth/2 * camera.zoom;
            float rightEdge = camera.position.x + viewportWidth/2 * camera.zoom;
            float topEdge = camera.position.y + viewportHeight/2 * camera.zoom;
            float bottomEdge = camera.position.y - viewportHeight/2 * camera.zoom;

            // Atualizar o tempo desde o último floco
            timeSinceLastSnowflake += delta;

            // Adicionar novos flocos
            if (timeSinceLastSnowflake > 0.01f) {
                timeSinceLastSnowflake = 0;

                // Criar vários flocos por frame para uma nevasca densa
                for (int i = 0; i < 8; i++) {
                    Rectangle snowflake = new Rectangle();

                    // Posicionar os flocos no lado esquerdo da tela
                    snowflake.x = leftEdge - 10; // Começar um pouco fora da tela

                    // Distribuir os flocos verticalmente na parte superior da tela
                    // Usamos 70% da altura da tela para que os flocos pareçam vir do céu
                    float alturaDistribuicao = viewportHeight * 0.7f;
                    snowflake.y = topEdge - random.nextFloat() * alturaDistribuicao;

                    // Tamanho dos flocos
                    snowflake.width = 4 + random.nextFloat() * 18; // Flocos maiores
                    snowflake.height = snowflake.width;

                    snowflakes.add(snowflake);
                }
            }

            // Atualizar e desenhar os flocos existentes
            for (Iterator<Rectangle> iter = snowflakes.iterator(); iter.hasNext(); ) {
                Rectangle snowflake = iter.next();

                // Movimento principal da esquerda para a direita
                snowflake.x += (200 + random.nextFloat() * 150) * delta; // Velocidade horizontal

                // Pequeno movimento vertical para dar mais naturalidade
                snowflake.y -= (20 + random.nextFloat() * 40) * delta; // Queda leve

                // Movimento oscilante vertical para simular flutuação
                snowflake.y += Math.sin(snowflake.x * 0.03) * 1.5 * delta * 60;

                // Remover flocos que saíram da tela
                if (snowflake.x > rightEdge || snowflake.y < bottomEdge) {
                    iter.remove();
                } else {
                    batch.draw(snowflakeTexture, snowflake.x, snowflake.y, snowflake.width, snowflake.height);
                }
            }
        }

        batch.end();
        stage.act(deltaTime);
        stage.draw();

        hudStage.act(deltaTime);
        hudStage.draw();

        // métodos
        movement(deltaTime);
        camera();

        lifeBar.setPosition(actorPlayer);
        lifeBar.setLifeBarValue(player.getVida());

        hungerBar.setPosition(actorPlayer);
        hungerBar.setHungerValue(player.getFome());

        inventory.setPosition(camera);

        actorPlayer.checkCollision(listaDeCristais);
        if (cavernaVisivel && caverna != null) {
            actorPlayer.checkCollision(caverna);
        }
        sairDoCenario();

        inputs.inputListener(actorPlayer, inventory, popUp,hudStage);

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

    private void aplicarClimaNaTela(EventoClimatico eventoClimatico) {
        if (eventoClimatico.getTipoDeClima() == TipoClimatico.NEVASCA) {
            climaNeveAtivo = true;
        } else {
            climaNeveAtivo = false;
        }
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
        if (soundMountain != null) {
            soundMountain.stop();
            soundMountain.dispose();
        }

        inicializarMundo.dispose();
        for(actorCristal cristal : listaDeCristais){
            cristal.dispose();
        }

        if (caverna != null) {
            caverna.dispose();
        }

        inventory.dispose();

        if(hungerBar != null) {
            hungerBar.dispose();
        }

        if (snowflakeTexture != null) {
            snowflakeTexture.dispose();
        }

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

                    // Verificar distância com a caverna, se existir
                    boolean posicaoValida = true;
                    if (cavernaVisivel && caverna != null) {
                        float distanciaCavernaX = Math.abs(posx - caverna.getX());
                        float distanciaCavernaY = Math.abs(posy - caverna.getY());

                        if (distanciaCavernaX < 300f && distanciaCavernaY < 300f) {
                            posicaoValida = false;
                        }
                    }

                    if (posicaoValida) {
                        pilhaDeItem = new actorPilhaDeItem(posx, posy, player, inventory, ambienteMontanha);
                        stage.addActor(pilhaDeItem);
                        isPilhaDeItemInstanciada = true;
                    }
                }
            }
        }, 0, 10);
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

    private void criarActorCristal() {
        float espacoMinimo = 200f; // Espaço mínimo entre cristais
        int maxTentativas = 30;    // Evita loop infinito
        int cristaisCriados = 0;

        // Na montanha, criamos mais cristais (8 em vez de 6)
        while(cristaisCriados < 5 && maxTentativas > 0) {
            // Gera posição aleatória
            float posX = MathUtils.random(100, worldWidth - 100);
            float posY = MathUtils.random(100, worldHeight - 100);

            boolean posicaoValida = true;

            // Verifica distância com outros cristais
            for(actorCristal outroCristal : listaDeCristais) {
                float distanciaX = Math.abs(posX - outroCristal.getX());
                float distanciaY = Math.abs(posY - outroCristal.getY());

                // Se estiver muito perto de outro cristal
                if(distanciaX < espacoMinimo && distanciaY < espacoMinimo) {
                    posicaoValida = false;
                    break;
                }
            }

            // Se a posição for válida, cria o cristal
            if(posicaoValida) {
                actorCristal novoCristal = new actorCristal(posX, posY, player, inventory);
                listaDeCristais.add(novoCristal);
                stage.addActor(novoCristal);
                cristaisCriados++;
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
        boolean naBordaEsquerda = playerX <= margin;
        boolean naBordaDireita = playerX + playerWidth >= worldWidth - margin;
        boolean naBordaSuperior = playerY + playerHeight >= worldHeight - margin;
        boolean naBordaInferior = playerY <= margin;

        // Na montanha, a saída inferior leva para a caverna
        if (naBordaInferior) {
            actorPlayer.addAction(Actions.fadeIn(0.1f));
            game.setScreen(new TelaDeJogoCaverna(game, player));
            dispose();
        }

        // As outras bordas podem levar a outras áreas (se existirem)
        if (naBordaEsquerda || naBordaDireita || naBordaSuperior) {
            // Aqui você pode adicionar transições para outras telas
            // Por enquanto, apenas reposiciona o jogador para evitar que saia da tela
            if (naBordaEsquerda) actorPlayer.setX(margin + 1);
            if (naBordaDireita) actorPlayer.setX(worldWidth - playerWidth - margin - 1);
            if (naBordaSuperior) actorPlayer.setY(worldHeight - playerHeight - margin - 1);
        }
    }
}