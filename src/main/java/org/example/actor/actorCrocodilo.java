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
import org.example.criatura.Crocodilo;
import org.example.domain.Item;
import org.example.domain.Personagem;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.enums.TipoArma;
import org.example.itens.Armas;

public class actorCrocodilo extends Actor implements Collidable {

    private double vida;
    private double vidaMaxima; // Para calcular a porcentagem da barra de vida
    private double dano;
    private Crocodilo crocodilo;

    private final Texture texturaCriatura;
    private Polygon collider;
    private Personagem player;
    private Inventory inventory;
    private actorPersonagem playerActor;
    private double velocidade = 180; // velocidade menor que o lobo
    private boolean isMorto = false;

    // Componentes da lifebar
    private ShapeRenderer shapeRenderer;
    private static final float LIFEBAR_WIDTH = 70f; // Ligeiramente maior que o lobo
    private static final float LIFEBAR_HEIGHT = 8f;
    private static final float LIFEBAR_OFFSET_Y = 10f; // Distância acima do crocodilo
    private boolean showLifebar = false;
    private float lifebarTimer = 0f;
    private static final float LIFEBAR_DISPLAY_TIME = 3f; // Tempo para mostrar a barra após dano

    public actorCrocodilo(Personagem player, actorPersonagem playerActor, Inventory inventory, Crocodilo crocodilo) {
        this.player = player;
        this.playerActor = playerActor;
        this.inventory = inventory;
        this.crocodilo = crocodilo;

        this.vida = crocodilo.getVida();
        this.vidaMaxima = crocodilo.getVida(); // Armazena a vida máxima
        this.dano = crocodilo.getDanoDeAtaque();

        // Inicializa o ShapeRenderer para desenhar a barra de vida
        this.shapeRenderer = new ShapeRenderer();

        texturaCriatura = new Texture(Gdx.files.internal("imagens/sprites/crocodilo.png"));

        float x = MathUtils.random(0, Gdx.graphics.getWidth() - 100);
        float y = MathUtils.random(0, Gdx.graphics.getHeight() - 100);

        setBounds(x, y, texturaCriatura.getWidth() * 0.5f, texturaCriatura.getHeight() * 0.5f);
        setPosition(x, y);

        setZIndex(10);
        setSize(0.35f * texturaCriatura.getWidth(), 0.35f * texturaCriatura.getHeight()); // crocodilo maior
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
                    System.out.println("Crocodilo atacado! Vida restante: " + vida);
                } else {
                    System.out.println("Você não pode atacar o crocodilo agora.");
                }
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Desenha o sprite do crocodilo
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

        // Desenha a barra de vida atual com cores diferentes baseadas na vida
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

        // Mostra a lifebar quando o crocodilo está próximo do jogador
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

        if (distancia < 60) { // Crocodilo tem range de ataque ligeiramente maior
            clearActions();
            crocodilo.ataque(player); // lógica do personagem
        } else {
            if (getActions().size == 0) {
                addAction(Actions.moveTo(playerActor.getX(), playerActor.getY(), tempo));
            }
        }
    }

    public boolean isNearPlayer() {
        if (playerActor == null) return false;

        double distancia = Math.sqrt(Math.pow(playerActor.getX() - getX(), 2) + Math.pow(playerActor.getY() - getY(), 2));
        return distancia < 70; // Range ligeiramente maior que o lobo
    }

    // MÉTODO SEPARADO PARA INTERAÇÃO COM PERSONAGEM
    public boolean isNearPlayer(actorPersonagem playerActor) {
        if (playerActor == null) return false;

        double distancia = Math.sqrt(Math.pow(playerActor.getX()-getX(),2) + Math.pow(playerActor.getY()-getY(),2));
        return distancia < 70; // Range de interação com o personagem
    }

    private boolean podeAtacar() {
        Item itemSelecionado = inventory.getItemSelecionado();

        float dx = playerActor.getX() - getX();
        float dy = playerActor.getY() - getY();
        float distancia = (float) Math.sqrt(dx * dx + dy * dy);

        // Permite ataque se estiver perto, mesmo sem item ou com item que não é arma
        if (itemSelecionado == null || !(itemSelecionado instanceof Armas)) {
            return distancia <= 60f; // Range ligeiramente maior para o crocodilo
        }

        Armas arma = (Armas) itemSelecionado;

        if (arma.getTipoArma() == TipoArma.DISTANCIA) {
            return true; // ataque à distância não depende da distância do personagem
        }

        return distancia <= 60f; // arma corpo a corpo depende da distância
    }

    public void diminuirVida() {
        Double dano = 5.0; // Dano padrão caso não tenha arma selecionada

        Item itemSelecionado = inventory.getItemSelecionado();

        if (itemSelecionado instanceof Armas arma) {
            dano = arma.getDano();
        }

        vida -= dano;

        // Mostra a lifebar quando o crocodilo recebe dano
        showLifebar = true;
        lifebarTimer = LIFEBAR_DISPLAY_TIME;

        if (vida <= 0) {
            vida = 0;
            isMorto = true;
            remove(); // Remove o crocodilo da cena
            System.out.println("Crocodilo derrotado!");
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