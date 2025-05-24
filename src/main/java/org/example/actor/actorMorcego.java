package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.example.Ui.Inventory;
import org.example.criatura.Morcego;
import org.example.domain.Personagem;
import org.lwjgl.system.MathUtil;

public class actorMorcego extends Actor implements Collidable {

    private double vida;
    private double dano;
    private Morcego morcego;

    private final Texture texturaCriatura;
    private Polygon collider;
    private Personagem player;
    private Inventory inventory;
    private double velocidade = 100; // velocidade do morcego


    public actorMorcego(Personagem player, Inventory inventory, Morcego morcego) {

        this.morcego = morcego;
        vida = morcego.getVida();
        dano = morcego.getDanoDeAtaque();

        texturaCriatura = new Texture(Gdx.files.internal("imagens/sprites/morcego.png"));

        float x = MathUtils.random(0, Gdx.graphics.getWidth()-100);
        float y = MathUtils.random(0, Gdx.graphics.getHeight()-100);

        setBounds(x, y, texturaCriatura.getWidth()*0.5f, texturaCriatura.getHeight()*0.5f);
        setPosition(x, y);

        setZIndex(10);

        setSize(0.9f * texturaCriatura.getWidth(), 0.9f * texturaCriatura.getHeight());

        setOrigin(getWidth()/2, getHeight()/2);

        float baseWidth = getWidth() * 0.5f;    // 70% da largura
        float baseHeight = getHeight() * 0.2f;   // 30% da altura
        float baseX = getWidth() * -0.1f;        // 15% da esquerda
        float baseY = getHeight() * 0.2f;

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
            batch.draw(texturaCriatura,
                    getX(), getY(),
                    getWidth(), getHeight()
            );
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }

    public void ataque(actorPersonagem player) {
        //seguir o jogador

        double distancia =  Math.sqrt(Math.pow(player.getX()-getX(),2) + Math.pow(player.getY()-getY(),2));

        //tempo = distancia / velocidade

        float tempo = (float) (distancia / velocidade);

        if(distancia < 50) {
            clearActions();
            float posX = MathUtils.random(0, Gdx.graphics.getWidth()-100);
            float posY = MathUtils.random(0, Gdx.graphics.getHeight()-100);

            addAction(Actions.moveTo(posX, posY, tempo*1.5f));

            morcego.ataque(player.getPlayer());

        }else{
            addAction(Actions.moveTo(player.getX(), player.getY(), tempo));
        }

    }

}
