package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.example.Ui.Inventory;
import org.example.criatura.Crocodilo;
import org.example.criatura.Urso;
import org.example.domain.Personagem;

import java.util.List;

public class actorCrocodilo extends Actor implements Collidable {

    private double vida;
    private double dano;
    private Crocodilo crocodilo;

    private final Texture texturaCriatura;
    private Polygon collider;
    private Personagem player;
    private Inventory inventory;
    private double velocidade = 180; // velocidade menor que o lobo


    public actorCrocodilo(Personagem player, Inventory inventory, Crocodilo crocodilo) {
        this.crocodilo = crocodilo;
        this.player = player;
        this.inventory = inventory;
        vida = crocodilo.getVida();
        dano = crocodilo.getDanoDeAtaque();

        texturaCriatura = new Texture(Gdx.files.internal("imagens/sprites/crocodilo.png"));

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
            crocodilo.ataque(player.getPlayer());
        } else {
            if (getActions().size == 0) {
                addAction(Actions.moveTo(player.getX(), player.getY(), tempo));
            }
        }
    }

    public void dispose() {
        if (texturaCriatura != null) {
            texturaCriatura.dispose();
        }
    }

}
