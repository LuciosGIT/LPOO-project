package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.math.Polygon;
import org.example.domain.Personagem;

public class actorArvore extends Actor{

    private final Texture textureArvore;
    private Polygon collider;

    public actorArvore(float x, float y) {
        textureArvore = new Texture(Gdx.files.internal("imagens/itens do cenario/arvoreFloresta.png"));
        setBounds(x,y, textureArvore.getWidth()*0.5f, textureArvore.getHeight()*0.5f);
        setPosition(x, y);

        setOrigin(getWidth()/2, getHeight()/2);

        float baseWidth = getWidth() * 0.5f;    // 60% da largura
        float baseHeight = getHeight() * 0.2f;   // 20% da altura
        float baseX = getWidth() * 0.1f;         // 20% da esquerda
        float baseY = 0;                         // Base da árvore

        float[] vertices = new float[] {
                baseX, baseY,
                baseX + baseWidth, baseY,
                baseX + baseWidth, baseY + baseHeight,
                baseX, baseY + baseHeight
        };

        collider = new Polygon(vertices);
        collider.setPosition(x, y);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                interagir();
            }
        });



    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureArvore,
                getX(), getY(),             // posição
                getOriginX(), getOriginY(), // origem da rotação
                getWidth(), getHeight(),    // dimensões
                1, 1,                       // escala
                getRotation(),              // rotação
                0, 0,                       // região da textura
                textureArvore.getWidth(), textureArvore.getHeight(),
                false, false);
    }


    public void dispose() {
        textureArvore.dispose();
    }

    private void interagir() {

        this.clearActions();

        Action tremer = Actions.sequence(
                Actions.rotateBy(5, 0.1f),
                Actions.rotateTo(0, 0.1f),
                Actions.rotateBy(-5, 0.1f),
                Actions.rotateTo(0, 0.1f),
                Actions.rotateBy(5, 0.1f),
                Actions.rotateTo(0, 0.1f),
                Actions.rotateBy(-5, 0.1f),
                Actions.rotateTo(0, 0.1f),
                Actions.rotateBy(5, 0.1f),
                Actions.rotateTo(0, 0.1f)  // Retorna à rotação original (0 graus)
        );


        this.addAction(tremer);

    }

    public boolean checkCollision(Polygon other) {
        return collider.getBoundingRectangle().overlaps(other.getBoundingRectangle());
    }

    public void setCollider(Polygon collider) {
        this.collider = collider;
    }

    public Polygon getCollider() {
        return collider;
    }



}
