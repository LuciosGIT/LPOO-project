package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.example.Ui.Inventory;
import org.example.criatura.Urso;
import org.example.domain.Personagem;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.List;

public class actorUrso extends Actor implements Collidable {

    private double vida;
    private double dano;
    private Urso urso;

    private final Texture texturaCriatura;
    private Polygon collider;
    private Personagem player;
    private Inventory inventory;
    private double velocidade = 180; // velocidade menor que o lobo

    public actorUrso(Personagem player, Inventory inventory, Urso urso) {
        this.urso = urso;
        this.player = player;
        this.inventory = inventory;
        vida = urso.getVida();
        dano = urso.getDanoDeAtaque();

        texturaCriatura = new Texture(Gdx.files.internal("imagens/sprites/urso.png"));

        float x = MathUtils.random(0, Gdx.graphics.getWidth()-100);
        float y = MathUtils.random(0, Gdx.graphics.getHeight()-100);

        setBounds(x, y, texturaCriatura.getWidth()*0.5f, texturaCriatura.getHeight()*0.5f);
        setPosition(x, y);

        setZIndex(10);
        setSize(0.35f * texturaCriatura.getWidth(), 0.35f * texturaCriatura.getHeight()); // urso maior
        setOrigin(getWidth()/2, getHeight()/2);

        float baseWidth = getWidth() * 0.5f;
        float baseHeight = getHeight() * 0.2f;
        float baseX = getWidth() * -0.1f;
        float baseY = getWidth() * 0.2f;

        float[] vertices = new float[] {
                baseX, baseY,
                baseX + baseWidth, baseY,
                baseX + baseWidth, baseY + baseHeight,
                baseX, baseY + baseHeight
        };

        collider = new Polygon(vertices);
        collider.setPosition(getX(), getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texturaCriatura != null) {
            batch.draw(texturaCriatura, getX(), getY(), getWidth(), getHeight());
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (collider != null) {
            collider.setPosition(getX(), getY());
        }
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }

    public void ataque(actorPersonagem player) {
        double distancia = Math.sqrt(Math.pow(player.getX()-getX(),2) + Math.pow(player.getY()-getY(),2));
        float tempo = (float) (distancia / velocidade);

        if(distancia < 60) {
            clearActions();
            urso.ataque(player.getPlayer());
        } else {
            if (getActions().size == 0) {
                addAction(Actions.moveTo(player.getX(), player.getY(), tempo));
            }
        }
    }

    public void checkObstacleCollisions(List<actorArvore> arvores, actorMercador mercador, actorAbrigo abrigo) {
        if (arvores != null) checkCollisionWithTrees(arvores);
        if (mercador != null) checkCollisionWithMercador(mercador);
        if (abrigo != null) checkCollisionWithAbrigo(abrigo);
    }

    public boolean isNearPlayer(actorPersonagem playerActor) {
        if (playerActor == null) return false;
        double distancia = Math.sqrt(Math.pow(playerActor.getX()-getX(),2) + Math.pow(playerActor.getY()-getY(),2));
        return distancia < 70;
    }

    public void checkCollisionWithTrees(List<actorArvore> arvores) {
        float colliderReduction = 0.8f;
        float width = getWidth() * colliderReduction;
        float height = getHeight() * colliderReduction;
        float x = getX() + (getWidth() - width) / 2;
        float y = getY() + (getHeight() - height) / 2;

        Rectangle bounds = new Rectangle(x, y, width, height);

        for (actorArvore arvore : arvores) {
            float treeWidth = arvore.getWidth() * 0.7f;
            float treeHeight = arvore.getHeight() * 0.7f;
            float treeX = arvore.getX() + (arvore.getWidth() - treeWidth) / 2;
            float treeY = arvore.getY() + (arvore.getHeight() - treeHeight) / 2;

            Rectangle treeBounds = new Rectangle(treeX, treeY, treeWidth, treeHeight);

            if (bounds.overlaps(treeBounds)) {
                handleCollision(treeX + treeWidth / 2, treeY + treeHeight / 2);
                break;
            }
        }
    }

    public void checkCollisionWithMercador(actorMercador mercador) {
        checkGenericCollision(mercador.getX(), mercador.getY(), mercador.getWidth(), mercador.getHeight(), 0.75f);
    }

    public void checkCollisionWithAbrigo(actorAbrigo abrigo) {
        checkGenericCollision(abrigo.getX(), abrigo.getY(), abrigo.getWidth(), abrigo.getHeight(), 0.7f);
    }

    private void checkGenericCollision(float objX, float objY, float objW, float objH, float reduction) {
        float colliderReduction = 0.8f;
        float width = getWidth() * colliderReduction;
        float height = getHeight() * colliderReduction;
        float x = getX() + (getWidth() - width) / 2;
        float y = getY() + (getHeight() - height) / 2;

        Rectangle ursoBounds = new Rectangle(x, y, width, height);

        float rW = objW * reduction;
        float rH = objH * reduction;
        float rX = objX + (objW - rW) / 2;
        float rY = objY + (objH - rH) / 2;

        Rectangle otherBounds = new Rectangle(rX, rY, rW, rH);

        if (ursoBounds.overlaps(otherBounds)) {
            handleCollision(rX + rW / 2, rY + rH / 2);
        }
    }

    private void handleCollision(float objCenterX, float objCenterY) {
        clearActions();

        float ursoCenterX = getX() + getWidth()/2;
        float ursoCenterY = getY() + getHeight()/2;

        float dirX = ursoCenterX - objCenterX;
        float dirY = ursoCenterY - objCenterY;

        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (length > 0) {
            dirX /= length;
            dirY /= length;

            float newX = ursoCenterX + dirX * 40;
            float newY = ursoCenterY + dirY * 40;
            setPosition(newX - getWidth()/2, newY - getHeight()/2);
        }
    }

    public void dispose() {
        if (texturaCriatura != null) {
            texturaCriatura.dispose();
        }
    }
}
