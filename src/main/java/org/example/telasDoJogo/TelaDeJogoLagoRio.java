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

import org.example.Ui.Inventory;
import org.example.Ui.LifeBar;
import org.example.actor.actorLago;
import org.example.actor.actorPersonagem;
import org.example.actor.actorPilhaDeItem;
import org.example.actor.actorRio;
import org.example.actor.actorPonte;
import org.example.domain.Personagem;
import org.example.utilitariosInterfaceGrafica.InicializarMundo;
import org.example.utilitariosInterfaceGrafica.Inputs;

import java.util.ArrayList;
import java.util.List;

public class TelaDeJogoLagoRio implements Screen {

    private final Game game;
    private final Personagem player;
    private final actorPersonagem actorPlayer;
    private final List<actorLago> listaDeLagos = new ArrayList<>();
    private actorPilhaDeItem pilhaDeItem;
    private actorRio rioSuperior;
    private actorRio rioInferior;
    private actorRio rioVisual; // Rio apenas visual, sem colisão
    private actorPonte ponte;
    private Texture backgroundLagoRio;
    private Batch batch;
    private Stage stage;
    private OrthographicCamera camera;

    private float worldWidth;
    private float worldHeight;
    private float viewportWidth;
    private float viewportHeight;

    InicializarMundo inicializarMundo;
    Inputs inputs;
    LifeBar lifeBar;
    Inventory inventory;

    public TelaDeJogoLagoRio(Game game, Personagem player) {
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);
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

        inputs = new Inputs();

        lifeBar = new LifeBar(actorPlayer);
        stage.addActor(lifeBar.getLifeBar());

        inventory = new Inventory(stage, 5, actorPlayer);

        pilhaDeItem = new actorPilhaDeItem(100, 100, player, inventory);
        stage.addActor(pilhaDeItem);

        criarActorsLagosRioEPonte();

        stage.addActor(actorPlayer);

        inventory.updateInventory();
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
        inputs.inputListener(actorPlayer, inventory);
        lifeBar.setPosition(actorPlayer);
        lifeBar.setLifeBarValue(player.getVida());
        inventory.setPosition(camera);

        actorPlayer.checkCollision(listaDeLagos);
        verificarColisaoComRio(deltaTime);

        sairDoCenario();
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

        // Dados do rio
        float rioLargura = 500;

        // Posição X do rio (centralizado horizontalmente)
        float rioX = (worldWidth - rioLargura) / 2;

        // Definir dimensões fixas para cada componente
        float alturaPonte = 250;

        // Deslocamento para a esquerda (ajuste este valor conforme necessário)
        float deslocamentoEsquerda = 20f; // 50 pixels para a esquerda

        // Posicionar a ponte exatamente no centro vertical da tela, mas deslocada para a esquerda
        float ponteY = (worldHeight - alturaPonte) / 2;
        float ponteX = rioX - deslocamentoEsquerda; // Deslocamento para a esquerda

        // Criar um rio visual contínuo para a aparência perfeita
        rioVisual = new actorRio(rioX, 0, rioLargura, worldHeight, player, inventory);

        // Criar a ponte sobre o rio, deslocada para a esquerda
        ponte = new actorPonte(ponteX, ponteY, rioLargura, alturaPonte);

        // Adicionar os atores visuais na ordem correta
        stage.addActor(rioVisual);
        stage.addActor(ponte);

        // Agora criar os rios com colisão, mas não os adicionar ao stage (serão invisíveis)
        // Rio superior - do topo da tela até o início da ponte
        rioSuperior = new actorRio(rioX, 0, rioLargura, ponteY, player, inventory);

        // Rio inferior - do fim da ponte até o fim da tela
        float yRioInferior = ponteY + alturaPonte;
        float alturaRioInferior = worldHeight - yRioInferior;
        rioInferior = new actorRio(rioX, yRioInferior, rioLargura, alturaRioInferior, player, inventory);

        // Adicionar logs para verificar as posições e dimensões
        System.out.println("Rio Visual: y=" + rioVisual.getY() + ", altura=" + rioVisual.getHeight());
        System.out.println("Ponte: x=" + ponte.getX() + ", y=" + ponte.getY() + ", altura=" + ponte.getHeight());
        System.out.println("Rio Superior (colisão): y=" + rioSuperior.getY() + ", altura=" + rioSuperior.getHeight());
        System.out.println("Rio Inferior (colisão): y=" + rioInferior.getY() + ", altura=" + rioInferior.getHeight());

        // Criar lagos longe do rio
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

            // Verificar distância mínima do rio
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
        inicializarMundo.dispose();
        inventory.dispose();
        for (actorLago lago : listaDeLagos) {
            lago.dispose();
        }
        if (rioSuperior != null) rioSuperior.dispose();
        if (rioInferior != null) rioInferior.dispose();
        if (rioVisual != null) rioVisual.dispose();
    }
}