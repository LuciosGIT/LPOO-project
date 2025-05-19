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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.example.Ui.Inventory;
import org.example.Ui.LifeBar;
import org.example.actor.actorLago;
import org.example.actor.actorPersonagem;
import org.example.actor.actorPilhaDeItem;
import org.example.actor.actorRio;
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
    private actorRio rio; // Variável para o rio
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

    public TelaDeJogoLagoRio(Game game, Personagem player){
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

        criarActorsLagos();  // Cria o rio e os lagos

        stage.addActor(actorPlayer); // Adicione o jogador por último para que ele fique na frente

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

        batch.draw(backgroundLagoRio,
                0, 0,
                worldWidth, worldHeight
        );
        batch.end();
        stage.act(deltaTime);
        stage.draw();

        movement(deltaTime);
        camera();
        inputs.inputListener(actorPlayer, inventory);
        lifeBar.setPosition(actorPlayer);
        lifeBar.setLifeBarValue(player.getVida());
        inventory.setPosition(camera);

        // Verificar colisões
        actorPlayer.checkCollision(listaDeLagos);
        verificarColisaoComRio(deltaTime);

        sairDoCenario();
    }

    // Método para verificar colisão com o rio
    private void verificarColisaoComRio(float deltaTime) {
        if (rio != null && actorPlayer.checkCollision(rio)) {
            // Aplicar efeito de corrente quando o jogador está no rio
            rio.aplicarEfeitoDeCorrente(actorPlayer, deltaTime);
        }
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
        if (rio != null) {
            rio.dispose(); // Liberar recursos do rio
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

    private void criarActorsLagos() {
        float espacoMinimo = 350f; // Espaço mínimo entre lagos
        float espacoDoRio = 350f;  // Espaço mínimo entre lagos e o rio (aumentado para maior distância)
        int maxTentativas = 50;    // Para evitar loops infinitos
        int lagosCriados = 0;
        int maxLagos = 10;         // Quantidade de lagos que deseja criar

        // Crie o rio mais largo horizontalmente
        float rioLargura = 500; // Rio mais largo
        float rioX = worldWidth / 2 - rioLargura / 2; // Centraliza o rio horizontalmente
        float rioY = 0; // Começa na parte inferior do mapa
        float rioAltura = worldHeight; // Altura igual à altura do mundo

        rio = new actorRio(rioX, rioY, rioLargura, rioAltura, player, inventory);
        stage.addActor(rio); // Adicione o rio ao palco

        while(lagosCriados < maxLagos && maxTentativas > 0) {
            float posX = MathUtils.random(100, worldWidth - 100);
            float posY = MathUtils.random(100, worldHeight - 100);

            boolean posicaoValida = true;

            // Verificar distância de outros lagos
            for (actorLago outroLago : listaDeLagos) {
                float distanciaX = Math.abs(posX - outroLago.getX());
                float distanciaY = Math.abs(posY - outroLago.getY());

                if(distanciaX < espacoMinimo && distanciaY < espacoMinimo) {
                    posicaoValida = false;
                    break;
                }
            }

            // Verificar se o lago está longe o suficiente do rio
            if (posicaoValida) {
                // Calcular as bordas do lago e do rio
                float lagoEsquerda = posX;
                float lagoDireita = posX + 100; // Assumindo que o lago tem aproximadamente 100 de largura
                float rioEsquerda = rioX;
                float rioDireita = rioX + rioLargura;

                // Verificar se o lago está muito próximo do rio
                // Consideramos a distância entre as bordas mais próximas
                float distanciaAteRio;

                if (lagoDireita < rioEsquerda) {
                    // Lago está à esquerda do rio
                    distanciaAteRio = rioEsquerda - lagoDireita;
                } else if (lagoEsquerda > rioDireita) {
                    // Lago está à direita do rio
                    distanciaAteRio = lagoEsquerda - rioDireita;
                } else {
                    // Lago sobrepõe o rio (isso não deveria acontecer com a verificação correta)
                    distanciaAteRio = 0;
                }

                // Se o lago estiver muito próximo do rio, não é uma posição válida
                if (distanciaAteRio < espacoDoRio) {
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
}