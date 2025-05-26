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
import org.example.Ui.*;
import org.example.actor.actorPersonagem;
import org.example.actor.actorPilhaDeItem;
import org.example.actor.actorPilar;
import org.example.actor.actorEstatua;
import org.example.ambientes.AmbienteRuinas;
import org.example.domain.Personagem;
import org.example.enums.TipoClimatico;
import org.example.utilitarios.Utilitario;
import org.example.utilitariosInterfaceGrafica.InicializarMundo;
import org.example.utilitariosInterfaceGrafica.Inputs;
import com.badlogic.gdx.audio.Sound;
import org.example.utilitariosInterfaceGrafica.VerificarStatusPlayer;

import java.util.ArrayList;
import java.util.List;

public class TelaDeJogoRuinas implements Screen {

    private final Game game;
    private final Personagem player;
    private final actorPersonagem actorPlayer;
    private actorPilar pilar;
    private actorEstatua estatua;
    private final List<actorPilar> listaDePilares = new ArrayList<>();
    private final List<actorEstatua> listaDeEstatuas = new ArrayList<>();
    private actorPilhaDeItem pilhaDeItem;
    private Texture backgroundRuinas;
    private Batch batch;
    private Stage stage;
    private Stage hudStage;  // Stage separado para HUD/interface fixa
    private OrthographicCamera camera;
    private boolean isPilhaDeItemInstanciada;

    private float worldWidth; // Largura do mundo
    private float worldHeight; // Altura do mundo
    private float viewportWidth; // Largura visível da câmera
    private float viewportHeight; // Altura visível da câmera

    private Craft popUp;

    private Sound soundRuins;
    private long soundId;

    private Texture darkOverlay;
    private float currentTime = 12f;
    private float dayDuration = 300f;

    private AmbienteRuinas ambienteRuinas;

    InicializarMundo inicializarMundo;
    Inputs inputs;
    LifeBar lifeBar;
    HungerBar hungerBar;
    Inventory inventory;

    // Constantes para limitar o número de objetos
    private static final int MAX_PILARES = 5;
    private static final int MIN_PILARES = 3; // Garantia de pelo menos 3 pilares
    private static final int MIN_ESTATUAS = 2;
    private static final int MAX_ESTATUAS = 2;

    // Distância mínima entre objetos
    private static final float DISTANCIA_MINIMA_PILARES = 450f;
    private static final float DISTANCIA_MINIMA_ESTATUAS = 550f;
    private static final float DISTANCIA_MINIMA_PILAR_ESTATUA = 500f;
    private static final float DISTANCIA_MINIMA_JOGADOR = 400f;

    private VerificarStatusPlayer verificarStatusPlayer;

    // Distâncias reduzidas para posicionamento de emergência
    private static final float DISTANCIA_EMERGENCIA = 300f;

    private Message message;  // Mensagem do evento

    public TelaDeJogoRuinas(Game game, Personagem player){
        this.game = game;
        this.player = player;
        this.actorPlayer = new actorPersonagem(player);
        try {
            this.ambienteRuinas = new AmbienteRuinas("Ruinas", "Ruinas abandonada e esquecida", 0.9, List.of(TipoClimatico.TEMPESTADE, TipoClimatico.CALOR), true, player);
        }
        catch (Exception e) {
            Gdx.app.error("TelaDeJogoRuinas", "Erro ao inicializar: " + e.getMessage());
        }
    }

    @Override
    public void show() {
        // Inicializa o mundo com o background de ruínas
        inicializarMundo = new InicializarMundo(actorPlayer,"imagens/backgrounds/mapaTelaDeJogoRuinas.png");

        this.camera = inicializarMundo.getCamera();
        this.stage = inicializarMundo.getStage();
        this.batch = inicializarMundo.getBatch();
        this.backgroundRuinas = inicializarMundo.getBackgroundFloresta(); // Reutiliza o método, mas com textura de ruínas
        this.worldWidth = inicializarMundo.getWorldWidth();
        this.worldHeight = inicializarMundo.getWorldHeight();
        this.viewportWidth = inicializarMundo.getViewportWidth();
        this.viewportHeight = inicializarMundo.getViewportHeight();

        hudStage = new Stage();

        lifeBar = new LifeBar(actorPlayer);
        stage.addActor(lifeBar.getLifeBar());

        isPilhaDeItemInstanciada = false;

        hungerBar = new HungerBar(actorPlayer);
        stage.addActor(hungerBar.getHungerBar());

        inventory = new Inventory(stage, 5, actorPlayer);

        stage.addActor(actorPlayer);

        criarPilaresEEstatuas();
        inventory.updateInventory();

        this.popUp = new Craft(stage, "Criar Item", "craftando", actorPlayer, inventory);

        inputs = new Inputs();

        // Adiciona um efeito de fade-in para a transição
        stage.getRoot().getColor().a = 0f;
        stage.getRoot().addAction(Actions.fadeIn(1.0f));

        soundRuins = Gdx.audio.newSound(Gdx.files.internal("sons/soundRuin.wav"));
        soundId = soundRuins.loop(0.5f);

        darkOverlay = new Texture(Gdx.files.internal("imagens/pixel.png"));

        instanciarPilhaDeItem();

        ambienteRuinas.explorar(player);

        // Mensagem padrão para o evento
        String mensagemEvento = "Você entrou nas ruínas antigas.";

        // Cria a mensagem e adiciona ao hudStage para ficar fixa na tela
        message = new Message(mensagemEvento, "Evento", hudStage, (viewportWidth - 300) / 2f, viewportHeight - 100);
        message.show();

        verificarStatusPlayer = new VerificarStatusPlayer(player);

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
        batch.draw(backgroundRuinas, 0, 0, worldWidth, worldHeight);
        batch.setColor(0, 0, 0, darkness);
        batch.draw(darkOverlay, 0, 0, worldWidth, worldHeight);
        batch.setColor(1, 1, 1, 1);
        batch.end();

        stage.act(deltaTime);
        stage.draw();

        // Desenha o HUD (interface fixa) após o stage do jogo
        hudStage.act(deltaTime);
        hudStage.draw();

        //métodos
        movement(deltaTime);
        camera();
        lifeBar.setPosition(actorPlayer);
        lifeBar.setLifeBarValue(player.getVida());
        hungerBar.setPosition(actorPlayer);
        hungerBar.setHungerValue(player.getFome());
        inventory.setPosition(camera);

        // Verifica colisão com pilares e estátuas
        actorPlayer.checkCollision(listaDePilares);
        actorPlayer.checkCollision(listaDeEstatuas);

        sairDoCenario();

        popUp.setPosition(actorPlayer);

        inputs.inputListener(actorPlayer, inventory, popUp);

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
        inicializarMundo.dispose();
        for(actorPilar pilar : listaDePilares){
            pilar.dispose();
        }
        for(actorEstatua estatua : listaDeEstatuas){
            estatua.dispose();
        }
        // Libera as texturas estáticas do actorEstatua
        actorEstatua.disposeTextures();
        inventory.dispose();
        hungerBar.dispose();

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

                    pilhaDeItem = new actorPilhaDeItem(posx, posy, player, inventory, ambienteRuinas);
                    stage.addActor(pilhaDeItem);
                    isPilhaDeItemInstanciada = true;
                }
            }
        }, 0, 10);
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

    private void criarPilaresEEstatuas() {
        // Primeiro cria as estátuas para garantir que elas tenham as melhores posições
        criarEstatuas();
        // Depois cria os pilares nas posições restantes
        criarPilares();
    }

    private void criarPilares() {
        int maxTentativas = 70; // Aumentado para ter mais chances de sucesso com distâncias maiores
        int pilaresCriados = 0;

        // Posições pré-definidas para pilares em caso de emergência
        float[][] posicoesPilaresEmergencia = {
                {worldWidth * 0.15f, worldHeight * 0.15f},
                {worldWidth * 0.85f, worldHeight * 0.85f},
                {worldWidth * 0.15f, worldHeight * 0.85f},
                {worldWidth * 0.85f, worldHeight * 0.15f},
                {worldWidth * 0.5f, worldHeight * 0.25f}
        };

        // Tenta criar pilares com distância normal
        while(pilaresCriados < MAX_PILARES && maxTentativas > 0) {
            float posX = MathUtils.random(200, worldWidth - 200);
            float posY = MathUtils.random(200, worldHeight - 200);

            boolean posicaoValida = true;

            // Verifica distância com outros pilares
            for(actorPilar outroPilar : listaDePilares) {
                float distancia = calcularDistancia(posX, posY, outroPilar.getX(), outroPilar.getY());
                if(distancia < DISTANCIA_MINIMA_PILARES) {
                    posicaoValida = false;
                    break;
                }
            }

            // Verifica distância com estátuas (usando distância específica maior)
            for(actorEstatua outraEstatua : listaDeEstatuas) {
                float distancia = calcularDistancia(posX, posY, outraEstatua.getX(), outraEstatua.getY());
                if(distancia < DISTANCIA_MINIMA_PILAR_ESTATUA) {
                    posicaoValida = false;
                    break;
                }
            }

            // Verifica distância com o jogador
            float distanciaJogador = calcularDistancia(posX, posY, actorPlayer.getX(), actorPlayer.getY());
            if (distanciaJogador < DISTANCIA_MINIMA_JOGADOR) {
                posicaoValida = false;
            }

            if(posicaoValida) {
                actorPilar novoPilar = new actorPilar(posX, posY, player, inventory);
                listaDePilares.add(novoPilar);
                stage.addActor(novoPilar);
                pilaresCriados++;
                Gdx.app.log("TelaDeJogoRuinas", "Pilar criado em: " + posX + ", " + posY);
            }

            maxTentativas--;
        }

        Gdx.app.log("TelaDeJogoRuinas", "Criados " + pilaresCriados + " pilares com distância normal");

        // Se não conseguiu criar o mínimo de pilares, tenta com distância reduzida
        if (pilaresCriados < MIN_PILARES) {
            Gdx.app.log("TelaDeJogoRuinas", "Tentando criar pilares com distância reduzida...");

            maxTentativas = 50;
            while(pilaresCriados < MIN_PILARES && maxTentativas > 0) {
                float posX = MathUtils.random(200, worldWidth - 200);
                float posY = MathUtils.random(200, worldHeight - 200);

                boolean posicaoValida = true;

                // Verifica distância com outros pilares (reduzida)
                for(actorPilar outroPilar : listaDePilares) {
                    float distancia = calcularDistancia(posX, posY, outroPilar.getX(), outroPilar.getY());
                    if(distancia < DISTANCIA_EMERGENCIA) {
                        posicaoValida = false;
                        break;
                    }
                }

                // Verifica distância com estátuas (reduzida)
                for(actorEstatua outraEstatua : listaDeEstatuas) {
                    float distancia = calcularDistancia(posX, posY, outraEstatua.getX(), outraEstatua.getY());
                    if(distancia < DISTANCIA_EMERGENCIA) {
                        posicaoValida = false;
                        break;
                    }
                }

                // Verifica distância com o jogador (reduzida)
                float distanciaJogador = calcularDistancia(posX, posY, actorPlayer.getX(), actorPlayer.getY());
                if (distanciaJogador < DISTANCIA_EMERGENCIA) {
                    posicaoValida = false;
                }

                if(posicaoValida) {
                    actorPilar novoPilar = new actorPilar(posX, posY, player, inventory);
                    listaDePilares.add(novoPilar);
                    stage.addActor(novoPilar);
                    pilaresCriados++;
                    Gdx.app.log("TelaDeJogoRuinas", "Pilar criado com distância reduzida em: " + posX + ", " + posY);
                }

                maxTentativas--;
            }
        }

        // Se ainda não conseguiu criar o mínimo, usa posições pré-definidas
        if (pilaresCriados < MIN_PILARES) {
            Gdx.app.log("TelaDeJogoRuinas", "Usando posições de emergência para pilares...");

            for (int i = 0; i < posicoesPilaresEmergencia.length && pilaresCriados < MIN_PILARES; i++) {
                float posX = posicoesPilaresEmergencia[i][0];
                float posY = posicoesPilaresEmergencia[i][1];

                actorPilar novoPilar = new actorPilar(posX, posY, player, inventory);
                listaDePilares.add(novoPilar);
                stage.addActor(novoPilar);
                pilaresCriados++;

                Gdx.app.log("TelaDeJogoRuinas", "Pilar de emergência criado em: " + posX + ", " + posY);
            }
        }

        Gdx.app.log("TelaDeJogoRuinas", "Total de pilares criados: " + pilaresCriados);
    }

    /**
     * Cria exatamente 2 estátuas no mapa, garantindo que pelo menos uma delas seja criada
     */
    private void criarEstatuas() {
        // Primeiro, tenta criar uma estátua do Rei (25% de chance)
        boolean tentarCriarRei = MathUtils.randomBoolean(0.25f);

        // Posições pré-definidas para garantir uma boa distribuição
        // Ajustadas para ficarem mais afastadas das bordas
        float[][] posicoesEstatuas = {
                {worldWidth * 0.25f, worldHeight * 0.75f},  // Quadrante superior esquerdo
                {worldWidth * 0.75f, worldHeight * 0.75f},  // Quadrante superior direito
                {worldWidth * 0.25f, worldHeight * 0.25f},  // Quadrante inferior esquerdo
                {worldWidth * 0.75f, worldHeight * 0.25f},  // Quadrante inferior direito
                {worldWidth * 0.5f, worldHeight * 0.5f}     // Centro (para o Rei)
        };

        // Embaralha as posições para aleatoriedade
        embaralharPosicoes(posicoesEstatuas);

        // Tenta criar a estátua do Rei primeiro, se aplicável
        if (tentarCriarRei) {
            boolean reiCriado = false;

            // Usa a posição central para o Rei
            float posX = posicoesEstatuas[4][0] + MathUtils.random(-50, 50);
            float posY = posicoesEstatuas[4][1] + MathUtils.random(-50, 50);

            if (posicaoValidaParaEstatua(posX, posY)) {
                for (int i = 0; i < 10 && !reiCriado; i++) {
                    actorEstatua novaEstatua = new actorEstatua(posX, posY, player, inventory);
                    if ("Rei".equals(novaEstatua.getTipoEstatua())) {
                        listaDeEstatuas.add(novaEstatua);
                        stage.addActor(novaEstatua);
                        reiCriado = true;

                        // Adiciona efeito visual especial
                        novaEstatua.addAction(Actions.sequence(
                                Actions.alpha(0),
                                Actions.delay(0.5f),
                                Actions.fadeIn(1.5f),
                                Actions.forever(Actions.sequence(
                                        Actions.scaleBy(0.05f, 0.05f, 1.0f),
                                        Actions.scaleBy(-0.05f, -0.05f, 1.0f)
                                ))
                        ));

                        Gdx.app.log("TelaDeJogoRuinas", "Estátua do Rei criada em: " + posX + ", " + posY);
                    } else {
                        novaEstatua.dispose();
                    }
                }
            }
        }

        // Cria estátuas adicionais até atingir o mínimo
        int indicePos = 0;
        int maxTentativasPorPosicao = 5;

        while (listaDeEstatuas.size() < MIN_ESTATUAS && indicePos < posicoesEstatuas.length) {
            float posX = posicoesEstatuas[indicePos][0] + MathUtils.random(-50, 50);
            float posY = posicoesEstatuas[indicePos][1] + MathUtils.random(-50, 50);

            if (posicaoValidaParaEstatua(posX, posY)) {
                int tentativas = 0;
                boolean estatuaCriada = false;

                while (!estatuaCriada && tentativas < maxTentativasPorPosicao) {
                    actorEstatua novaEstatua = new actorEstatua(posX, posY, player, inventory);

                    // Se já temos uma estátua do Rei e esta também é do Rei, tenta novamente
                    boolean jaTemRei = false;
                    for (actorEstatua e : listaDeEstatuas) {
                        if ("Rei".equals(e.getTipoEstatua())) {
                            jaTemRei = true;
                            break;
                        }
                    }

                    if (jaTemRei && "Rei".equals(novaEstatua.getTipoEstatua())) {
                        novaEstatua.dispose();
                        tentativas++;
                        continue;
                    }

                    // Adiciona a estátua
                    listaDeEstatuas.add(novaEstatua);
                    stage.addActor(novaEstatua);
                    estatuaCriada = true;

                    Gdx.app.log("TelaDeJogoRuinas", "Estátua de " + novaEstatua.getTipoEstatua() +
                            " criada em: " + posX + ", " + posY);

                    // Adiciona efeito visual
                    if ("Rei".equals(novaEstatua.getTipoEstatua())) {
                        novaEstatua.addAction(Actions.sequence(
                                Actions.alpha(0),
                                Actions.delay(0.5f),
                                Actions.fadeIn(1.5f),
                                Actions.forever(Actions.sequence(
                                        Actions.scaleBy(0.05f, 0.05f, 1.0f),
                                        Actions.scaleBy(-0.05f, -0.05f, 1.0f)
                                ))
                        ));
                    } else {
                        novaEstatua.addAction(Actions.sequence(
                                Actions.alpha(0),
                                Actions.fadeIn(1.0f)
                        ));
                    }
                }
            }

            indicePos++;
        }

        Gdx.app.log("TelaDeJogoRuinas", "Criadas " + listaDeEstatuas.size() + " estátuas");

        // Verifica se conseguimos criar o mínimo de estátuas
        if (listaDeEstatuas.size() < MIN_ESTATUAS) {
            Gdx.app.error("TelaDeJogoRuinas", "AVISO: Não foi possível criar o mínimo de " +
                    MIN_ESTATUAS + " estátuas. Criadas apenas " + listaDeEstatuas.size());

            // Força a criação de estátuas em posições fixas como último recurso
            float[][] posicoesEmergencia = {
                    {worldWidth * 0.2f, worldHeight * 0.8f},
                    {worldWidth * 0.8f, worldHeight * 0.2f}
            };

            int indiceEmergencia = 0;
            while (listaDeEstatuas.size() < MIN_ESTATUAS && indiceEmergencia < posicoesEmergencia.length) {
                float posX = posicoesEmergencia[indiceEmergencia][0];
                float posY = posicoesEmergencia[indiceEmergencia][1];

                actorEstatua novaEstatua = new actorEstatua(posX, posY, player, inventory);
                listaDeEstatuas.add(novaEstatua);
                stage.addActor(novaEstatua);

                Gdx.app.log("TelaDeJogoRuinas", "Estátua de emergência de " +
                        novaEstatua.getTipoEstatua() + " criada em: " + posX + ", " + posY);

                indiceEmergencia++;
            }
        }
    }

    /**
     * Verifica se uma posição é válida para criar uma estátua
     */
    private boolean posicaoValidaParaEstatua(float posX, float posY) {
        // Verifica distância com pilares
        for(actorPilar pilar : listaDePilares) {
            float distancia = calcularDistancia(posX, posY, pilar.getX(), pilar.getY());
            if(distancia < DISTANCIA_MINIMA_PILAR_ESTATUA) {
                return false;
            }
        }

        // Verifica distância com outras estátuas
        for(actorEstatua outraEstatua : listaDeEstatuas) {
            float distancia = calcularDistancia(posX, posY, outraEstatua.getX(), outraEstatua.getY());
            if(distancia < DISTANCIA_MINIMA_ESTATUAS) {
                return false;
            }
        }

        // Verifica distância com o jogador
        float distanciaJogador = calcularDistancia(posX, posY, actorPlayer.getX(), actorPlayer.getY());
        if (distanciaJogador < DISTANCIA_MINIMA_JOGADOR) {
            return false;
        }

        // Verifica se está muito próximo das bordas
        if (posX < 200 || posX > worldWidth - 200 || posY < 200 || posY > worldHeight - 200) {
            return false;
        }

        return true;
    }

    /**
     * Calcula a distância entre dois pontos
     */
    private float calcularDistancia(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Embaralha um array de posições
     */
    private void embaralharPosicoes(float[][] posicoes) {
        for (int i = posicoes.length - 1; i > 0; i--) {
            int index = MathUtils.random(i);
            // Troca as posições
            float[] temp = posicoes[i];
            posicoes[i] = posicoes[index];
            posicoes[index] = temp;
        }
    }

    private void sairDoCenario() {
        float playerX = actorPlayer.getX();
        float playerY = actorPlayer.getY();
        float playerWidth = actorPlayer.getWidth();
        float playerHeight = actorPlayer.getHeight();
        float margin = 10f; // margem de tolerância

        // Verifica cada borda
        boolean naBoradaEsquerda = playerX <= margin;
        boolean naBordaDireita = playerX + playerWidth >= worldWidth - margin;
        boolean naBordaSuperior = playerY + playerHeight >= worldHeight - margin;
        boolean naBordaInferior = playerY <= margin;

        if (naBoradaEsquerda) {
            actorPlayer.addAction(Actions.fadeIn(0.1f));
            game.setScreen(new TelaDeJogoFloresta(game, player));
            dispose();
        }

        if (naBordaDireita) {
            actorPlayer.addAction(Actions.fadeIn(0.1f));
            game.setScreen(new TelaDeJogoCaverna(game, player));
            dispose();
        }

        if (naBordaSuperior) {
            actorPlayer.addAction(Actions.fadeIn(0.1f));
            game.setScreen(new TelaDeJogoLagoRio(game, player));
            dispose();
        }

        if (naBordaInferior) {
            actorPlayer.addAction(Actions.fadeIn(0.1f));
            game.setScreen(new TelaDeJogoMontanha(game, player));
            dispose();
        }
    }
}