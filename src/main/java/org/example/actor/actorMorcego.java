package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.example.Ui.Inventory;
import org.example.criatura.Morcego;
import org.example.domain.Item;
import org.example.domain.Personagem;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.enums.TipoArma;
import org.example.itens.Armas;

import java.util.List;

public class actorMorcego extends Actor implements Collidable {

    private double vida;
    private double vidaMaxima; // Para calcular a porcentagem da barra de vida
    private double dano;
    private Morcego morcego;

    private final Texture texturaCriatura;
    private Polygon collider;
    private Personagem player;
    private Inventory inventory;
    private actorPersonagem playerActor;
    private double velocidade = 300; // velocidade do morcego
    private boolean isMorto = false;

    // Componentes da lifebar
    private ShapeRenderer shapeRenderer;
    private static final float LIFEBAR_WIDTH = 60f;
    private static final float LIFEBAR_HEIGHT = 8f;
    private static final float LIFEBAR_OFFSET_Y = 10f; // Distância acima do morcego
    private boolean showLifebar = false;
    private float lifebarTimer = 0f;
    private static final float LIFEBAR_DISPLAY_TIME = 3f; // Tempo para mostrar a barra após dano

    public actorMorcego(Personagem player, actorPersonagem playerActor, Inventory inventory, Morcego morcego) {
        this.player = player;
        this.playerActor = playerActor;
        this.inventory = inventory;
        this.morcego = morcego;

        this.vida = morcego.getVida();
        this.vidaMaxima = morcego.getVida(); // Armazena a vida máxima
        this.dano = morcego.getDanoDeAtaque();

        // Inicializa o ShapeRenderer para desenhar a barra de vida
        this.shapeRenderer = new ShapeRenderer();

        texturaCriatura = new Texture(Gdx.files.internal("imagens/sprites/morcego.png"));

        float x = MathUtils.random(0, Gdx.graphics.getWidth() - 100);
        float y = MathUtils.random(0, Gdx.graphics.getHeight() - 100);

        setBounds(x, y, texturaCriatura.getWidth() * 0.5f, texturaCriatura.getHeight() * 0.5f);
        setPosition(x, y);

        setZIndex(10);
        setSize(0.9f * texturaCriatura.getWidth(), 0.9f * texturaCriatura.getHeight());
        setOrigin(getWidth() / 2, getHeight() / 2);

        float baseWidth = getWidth() * 0.5f;
        float baseHeight = getHeight() * 0.2f;
        float baseX = getWidth() * -0.1f;
        float baseY = getWidth() * 0.2f;

        float[] vertices = new float[]{
                baseX, baseY,
                baseX + baseWidth, baseY,
                baseX + baseWidth, baseY + baseHeight,
                baseX, baseY + baseHeight
        };

        collider = new Polygon(vertices);
        collider.setPosition(getX(), getY());

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (podeAtacar()) {
                    diminuirVida();
                    System.out.println("Morcego atacado! Vida restante: " + vida);
                } else {
                    System.out.println("Você precisa de uma arma à distância para atacar o morcego!");
                }
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Desenha o sprite do morcego
        if (texturaCriatura != null) {
            batch.draw(texturaCriatura,
                    getX(), getY(),
                    getWidth(), getHeight()
            );
        }

        // Desenha a barra de vida se necessário
        if (showLifebar && vida > 0) {
            drawLifebar(batch);
        }
    }

    private void drawLifebar(Batch batch) {
        // Finaliza o batch para usar o ShapeRenderer
        batch.end();

        // Calcula a posição da barra de vida
        float barX = getX() + (getWidth() - LIFEBAR_WIDTH) / 2;
        float barY = getY() + getHeight() + LIFEBAR_OFFSET_Y;

        // Calcula a porcentagem de vida
        float healthPercentage = (float) (vida / vidaMaxima);

        // Configura o ShapeRenderer
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Desenha o fundo da barra (vermelho escuro)
        shapeRenderer.setColor(0.3f, 0.1f, 0.1f, 0.8f);
        shapeRenderer.rect(barX, barY, LIFEBAR_WIDTH, LIFEBAR_HEIGHT);

        // Desenha a barra de vida atual
        if (healthPercentage > 0.6f) {
            shapeRenderer.setColor(0.2f, 0.8f, 0.2f, 0.9f); // Verde
        } else if (healthPercentage > 0.3f) {
            shapeRenderer.setColor(0.9f, 0.9f, 0.2f, 0.9f); // Amarelo
        } else {
            shapeRenderer.setColor(0.9f, 0.2f, 0.2f, 0.9f); // Vermelho
        }

        shapeRenderer.rect(barX, barY, LIFEBAR_WIDTH * healthPercentage, LIFEBAR_HEIGHT);

        // Desenha a borda da barra
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(barX, barY, LIFEBAR_WIDTH, LIFEBAR_HEIGHT);
        shapeRenderer.end();

        // Reinicia o batch
        batch.begin();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (collider != null) {
            collider.setPosition(getX(), getY());
        }

        // Atualiza o timer da lifebar
        if (showLifebar) {
            lifebarTimer -= delta;
            if (lifebarTimer <= 0) {
                showLifebar = false;
            }
        }

        // Mostra a lifebar quando o morcego está próximo do jogador
        if (isNearPlayer()) {
            showLifebar = true;
            lifebarTimer = LIFEBAR_DISPLAY_TIME;
        }
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }

    public void ataque() {
        if (playerActor == null) return;

        double distancia = Math.sqrt(Math.pow(playerActor.getX() - getX(), 2) + Math.pow(playerActor.getY() - getY(), 2));
        float tempo = (float) (distancia / velocidade);

        if (distancia < 50) {
            clearActions();

            // Executa o ataque
            morcego.ataque(player); // lógica do personagem

            // Pequeno rebote - calcula direção oposta ao jogador
            float playerX = playerActor.getX() + playerActor.getWidth() / 2;
            float playerY = playerActor.getY() + playerActor.getHeight() / 2;
            float morcegoX = getX() + getWidth() / 2;
            float morcegoY = getY() + getHeight() / 2;

            // Direção do rebote (oposta ao jogador)
            float dirX = morcegoX - playerX;
            float dirY = morcegoY - playerY;

            // Normaliza a direção
            float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
            if (length > 0) {
                dirX /= length;
                dirY /= length;

                // Pequeno rebote - apenas 40-60 pixels de distância
                float reboteDistancia = MathUtils.random(40f, 60f);
                float novaX = morcegoX + dirX * reboteDistancia - getWidth() / 2;
                float novaY = morcegoY + dirY * reboteDistancia - getHeight() / 2;

                // Garante que não saia da tela
                novaX = MathUtils.clamp(novaX, 0, Gdx.graphics.getWidth() - getWidth());
                novaY = MathUtils.clamp(novaY, 0, Gdx.graphics.getHeight() - getHeight());

                // Movimento rápido de rebote (0.3 segundos)
                addAction(Actions.moveTo(novaX, novaY, 0.3f));
            }
        } else {
            if (getActions().size == 0) {
                addAction(Actions.moveTo(playerActor.getX(), playerActor.getY(), tempo));
            }
        }
    }

    public boolean isNearPlayer() {
        if (playerActor == null) return false;

        double distancia = Math.sqrt(Math.pow(playerActor.getX() - getX(), 2) + Math.pow(playerActor.getY() - getY(), 2));
        return distancia < 60;
    }

    // MÉTODO PRINCIPAL PARA COLISÕES COM OBSTÁCULOS
    // Este método deve ser chamado no game loop para obstáculos que IMPEDEM movimento
    public void checkObstacleCollisions(List<actorArvore> arvores, actorMercador mercador, actorAbrigo abrigo) {
        // Verificar colisões com árvores
        if (arvores != null) {
            checkCollisionWithTrees(arvores);
        }

        // Verificar colisão com mercador
        if (mercador != null) {
            checkCollisionWithMercador(mercador);
        }

        // Verificar colisão com abrigo
        if (abrigo != null) {
            checkCollisionWithAbrigo(abrigo);
        }
    }

    // MÉTODO SEPARADO PARA INTERAÇÃO COM PERSONAGEM
    // Este método deve ser chamado separadamente e NÃO impede movimento
    public boolean isNearPlayer(actorPersonagem playerActor) {
        if (playerActor == null) return false;

        double distancia = Math.sqrt(Math.pow(playerActor.getX()-getX(),2) + Math.pow(playerActor.getY()-getY(),2));
        return distancia < 60; // Range de interação com o personagem
    }

    // Collision detection with trees - RANGE REDUZIDO
    public void checkCollisionWithTrees(List<actorArvore> arvores) {
        float colliderReduction = 0.8f;
        float morcegoColliderWidth = getWidth() * colliderReduction;
        float morcegoColliderHeight = getHeight() * colliderReduction;
        float morcegoColliderX = getX() + (getWidth() - morcegoColliderWidth) / 2;
        float morcegoColliderY = getY() + (getHeight() - morcegoColliderHeight) / 2;

        Rectangle morcegoBounds = new Rectangle(morcegoColliderX, morcegoColliderY, morcegoColliderWidth, morcegoColliderHeight);

        for (actorArvore arvore : arvores) {
            float treeColliderReduction = 0.7f;
            float treeColliderWidth = arvore.getWidth() * treeColliderReduction;
            float treeColliderHeight = arvore.getHeight() * treeColliderReduction;
            float treeColliderX = arvore.getX() + (arvore.getWidth() - treeColliderWidth) / 2;
            float treeColliderY = arvore.getY() + (arvore.getHeight() - treeColliderHeight) / 2;

            Rectangle treeBounds = new Rectangle(treeColliderX, treeColliderY,
                    treeColliderWidth, treeColliderHeight);

            if (morcegoBounds.overlaps(treeBounds)) {
                handleTreeCollision(arvore);
                break; // Sair do loop após primeira colisão
            }
        }
    }

    // Collision detection with mercador - RANGE REDUZIDO
    public void checkCollisionWithMercador(actorMercador mercador) {
        float colliderReduction = 0.8f;
        float morcegoColliderWidth = getWidth() * colliderReduction;
        float morcegoColliderHeight = getHeight() * colliderReduction;
        float morcegoColliderX = getX() + (getWidth() - morcegoColliderWidth) / 2;
        float morcegoColliderY = getY() + (getHeight() - morcegoColliderHeight) / 2;

        Rectangle morcegoBounds = new Rectangle(morcegoColliderX, morcegoColliderY, morcegoColliderWidth, morcegoColliderHeight);

        float mercadorColliderReduction = 0.75f;
        float mercadorColliderWidth = mercador.getWidth() * mercadorColliderReduction;
        float mercadorColliderHeight = mercador.getHeight() * mercadorColliderReduction;
        float mercadorColliderX = mercador.getX() + (mercador.getWidth() - mercadorColliderWidth) / 2;
        float mercadorColliderY = mercador.getY() + (mercador.getHeight() - mercadorColliderHeight) / 2;

        Rectangle mercadorBounds = new Rectangle(mercadorColliderX, mercadorColliderY,
                mercadorColliderWidth, mercadorColliderHeight);

        if (morcegoBounds.overlaps(mercadorBounds)) {
            handleMercadorCollision(mercador);
        }
    }

    // Collision detection with abrigo - RANGE REDUZIDO
    public void checkCollisionWithAbrigo(actorAbrigo abrigo) {
        float colliderReduction = 0.8f;
        float morcegoColliderWidth = getWidth() * colliderReduction;
        float morcegoColliderHeight = getHeight() * colliderReduction;
        float morcegoColliderX = getX() + (getWidth() - morcegoColliderWidth) / 2;
        float morcegoColliderY = getY() + (getHeight() - morcegoColliderHeight) / 2;

        Rectangle morcegoBounds = new Rectangle(morcegoColliderX, morcegoColliderY, morcegoColliderWidth, morcegoColliderHeight);

        float abrigoColliderReduction = 0.7f;
        float abrigoColliderWidth = abrigo.getWidth() * abrigoColliderReduction;
        float abrigoColliderHeight = abrigo.getHeight() * abrigoColliderReduction;
        float abrigoColliderX = abrigo.getX() + (abrigo.getWidth() - abrigoColliderWidth) / 2;
        float abrigoColliderY = abrigo.getY() + (abrigo.getHeight() - abrigoColliderHeight) / 2;

        Rectangle abrigoBounds = new Rectangle(abrigoColliderX, abrigoColliderY,
                abrigoColliderWidth, abrigoColliderHeight);

        if (morcegoBounds.overlaps(abrigoBounds)) {
            handleAbrigoCollision(abrigo);
        }
    }

    // Handle collision with tree
    private void handleTreeCollision(actorArvore arvore) {
        clearActions();

        float treeX = arvore.getX() + arvore.getWidth()/2;
        float treeY = arvore.getY() + arvore.getHeight()/2;
        float morcegoX = getX() + getWidth()/2;
        float morcegoY = getY() + getHeight()/2;

        float dirX = morcegoX - treeX;
        float dirY = morcegoY - treeY;

        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (length > 0) {
            dirX /= length;
            dirY /= length;

            float newX = morcegoX + dirX * 30;
            float newY = morcegoY + dirY * 30;
            setPosition(newX - getWidth()/2, newY - getHeight()/2);
        }
    }

    // Handle collision with mercador
    private void handleMercadorCollision(actorMercador mercador) {
        clearActions();

        float mercadorX = mercador.getX() + mercador.getWidth()/2;
        float mercadorY = mercador.getY() + mercador.getHeight()/2;
        float morcegoX = getX() + getWidth()/2;
        float morcegoY = getY() + getHeight()/2;

        float dirX = morcegoX - mercadorX;
        float dirY = morcegoY - mercadorY;

        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (length > 0) {
            dirX /= length;
            dirY /= length;

            float newX = morcegoX + dirX * 30;
            float newY = morcegoY + dirY * 30;
            setPosition(newX - getWidth()/2, newY - getHeight()/2);
        }
    }

    // Handle collision with abrigo
    private void handleAbrigoCollision(actorAbrigo abrigo) {
        clearActions();

        float abrigoX = abrigo.getX() + abrigo.getWidth()/2;
        float abrigoY = abrigo.getY() + abrigo.getHeight()/2;
        float morcegoX = getX() + getWidth()/2;
        float morcegoY = getY() + getHeight()/2;

        float dirX = morcegoX - abrigoX;
        float dirY = morcegoY - abrigoY;

        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (length > 0) {
            dirX /= length;
            dirY /= length;

            float newX = morcegoX + dirX * 30;
            float newY = morcegoY + dirY * 30;
            setPosition(newX - getWidth()/2, newY - getHeight()/2);
        }
    }

    // MÉTODO ESPECIAL PARA MORCEGO - SÓ PODE SER ATACADO COM ARMAS À DISTÂNCIA
    private boolean podeAtacar() {
        Item itemSelecionado = inventory.getItemSelecionado();

        // OBRIGATÓRIO ter uma arma selecionada
        if (itemSelecionado == null || !(itemSelecionado instanceof Armas)) {
            return false; // Não pode atacar sem arma
        }

        Armas arma = (Armas) itemSelecionado;

        // OBRIGATÓRIO ser arma à distância
        if (arma.getTipoArma() != TipoArma.DISTANCIA) {
            return false; // Não pode atacar com arma corpo a corpo
        }

        // Arma à distância sempre pode atacar (não depende da distância)
        return true;
    }

    public void diminuirVida() {
        Item itemSelecionado = inventory.getItemSelecionado();

        // Só pode atacar com arma à distância, então sempre haverá uma arma
        if (itemSelecionado instanceof Armas arma && arma.getTipoArma() == TipoArma.DISTANCIA) {
            Double dano = arma.getDano();
            vida -= dano;

            // Mostra a lifebar quando o morcego recebe dano
            showLifebar = true;
            lifebarTimer = LIFEBAR_DISPLAY_TIME;

            if (vida <= 0) {
                vida = 0;
                isMorto = true;
                remove(); // Remove o morcego da cena
                System.out.println("Morcego derrotado!");
            }
        }
    }

    public boolean getIsMorto() {
        return isMorto;
    }

    // Método para forçar mostrar a lifebar (útil para debug ou situações específicas)
    public void showLifebar() {
        showLifebar = true;
        lifebarTimer = LIFEBAR_DISPLAY_TIME;
    }

    // Getters para a vida (útil para outras classes)
    public double getVida() {
        return vida;
    }

    public double getVidaMaxima() {
        return vidaMaxima;
    }

    public float getHealthPercentage() {
        return (float) (vida / vidaMaxima);
    }

    public void dispose() {
        if (texturaCriatura != null) {
            texturaCriatura.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}