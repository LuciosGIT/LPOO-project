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
import org.example.Ui.Craft;
import org.example.Ui.Inventory;
import org.example.Ui.LifeBar;
import org.example.Ui.HungerBar;
import org.example.actor.actorCristal;
import org.example.actor.actorPersonagem;
import org.example.actor.actorPilhaDeItem;
import org.example.ambientes.AmbienteMontanha;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.enums.TipoClimatico;
import org.example.eventos.EventoClimatico;
import org.example.utilitariosInterfaceGrafica.InicializarMundo;
import org.example.utilitariosInterfaceGrafica.Inputs;
import com.badlogic.gdx.audio.Sound;

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
    private Texture backgroundMontanha;
    private Batch batch;
    private Stage stage;
    private OrthographicCamera camera;

    private Craft popUp;

    private float worldWidth; // Largura do mundo
    private float worldHeight; // Altura do mundo
    private float viewportWidth; // Largura visível da câmera
    private float viewportHeight; // Altura visível da câmera

    private Sound soundMountain;
    private long soundId;

    private AmbienteMontanha ambienteMontanha;

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

    public TelaDeJogoMontanha(Game game, Personagem player){
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);
        this.ambienteMontanha = new AmbienteMontanha("Montanha", "Pico de um monte gelado",5.5, List.of(TipoClimatico.TEMPESTADE, TipoClimatico.NEVASCA), true, false, player);
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

        lifeBar = new LifeBar(actorPlayer);
        stage.addActor(lifeBar.getLifeBar());

        hungerBar = new HungerBar(actorPlayer);
        stage.addActor(hungerBar.getHungerBar());

        inventory = new Inventory(stage, 5, actorPlayer);

        stage.addActor(actorPlayer);

        criarActorCristal();
        inventory.updateInventory();

        this.popUp = new Craft(stage, "Criar Item", "craftando", actorPlayer, inventory);

        soundMountain = Gdx.audio.newSound(Gdx.files.internal("sons/soundMount.wav"));
        soundId = soundMountain.loop(0.5f);

        // Inicializar componentes do efeito de neve
        snowflakeTexture = new Texture(Gdx.files.internal("imagens/assets/particles/neve.png"));
        snowflakes = new Array<Rectangle>();
        random = new Random();
        timeSinceLastSnowflake = 0;

        ambienteMontanha.explorar(player);

        // Verificar se há evento climático e aplicar
        Evento evento = ambienteMontanha.gerarEvento(player);
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

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        stage.getViewport().apply();

        batch.draw(backgroundMontanha,
                0, 0,
                worldWidth, worldHeight
        );

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

        // métodos
        movement(deltaTime);
        camera();

        lifeBar.setPosition(actorPlayer);
        lifeBar.setLifeBarValue(player.getVida());

        hungerBar.setPosition(actorPlayer);
        hungerBar.setHungerValue(player.getFome());

        inventory.setPosition(camera);

        actorPlayer.checkCollision(listaDeCristais);
        sairDoCenario();

        inputs.inputListener(actorPlayer, inventory, popUp);

        popUp.setPosition(actorPlayer);
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
        inventory.dispose();

        if(hungerBar != null) {
            hungerBar.dispose();
        }

        if (snowflakeTexture != null) {
            snowflakeTexture.dispose();
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
            // Por exemplo:
            // game.setScreen(new TelaDeJogoOutraArea(game, player));
            // dispose();

            // Por enquanto, apenas reposiciona o jogador para evitar que saia da tela
            if (naBordaEsquerda) actorPlayer.setX(margin + 1);
            if (naBordaDireita) actorPlayer.setX(worldWidth - playerWidth - margin - 1);
            if (naBordaSuperior) actorPlayer.setY(worldHeight - playerHeight - margin - 1);
        }
    }
}