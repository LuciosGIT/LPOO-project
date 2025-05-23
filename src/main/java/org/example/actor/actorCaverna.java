package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class actorCaverna extends Actor implements Collidable {

    private final Texture textureCaverna;
    private Polygon collider;
    private Rectangle hitBox;
    private boolean interagido = false;
    private Runnable onClickAction;

    public actorCaverna(float x, float y) {
        textureCaverna = new Texture(Gdx.files.internal("imagens/itens do cenario/caverna.png"));
        setBounds(x, y, textureCaverna.getWidth() * 0.6f, textureCaverna.getHeight() * 0.6f);
        setPosition(x, y);

        setZIndex(10);
        setOrigin(getWidth() / 2, getHeight() / 2);

        // Criar um collider retangular para a caverna
        float baseWidth = getWidth() * 0.8f;    // 80% da largura
        float baseHeight = getHeight() * 0.5f;   // 50% da altura
        float baseX = getWidth() * 0.1f;         // 10% da esquerda
        float baseY = 0;                         // Base da caverna

        float[] vertices = new float[] {
                baseX, baseY,
                baseX + baseWidth, baseY,
                baseX + baseWidth, baseY + baseHeight,
                baseX, baseY + baseHeight
        };

        collider = new Polygon(vertices);
        collider.setPosition(x, y);

        // Manter o hitBox para compatibilidade
        this.hitBox = new Rectangle(x + baseX, y + baseY, baseWidth, baseHeight);

        // Adicionar ClickListener para interação ao clicar
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onClickAction != null && !interagido) {
                    onClickAction.run();
                }
            }
        });
    }

    // Método para definir a ação a ser executada quando a caverna for clicada
    public void setOnClickAction(Runnable action) {
        this.onClickAction = action;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Atualizar a posição do collider e hitBox se a caverna se mover
        collider.setPosition(getX(), getY());
        hitBox.setPosition(getX() + getWidth() * 0.1f, getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureCaverna,
                getX(), getY(),             // posição
                getOriginX(), getOriginY(), // origem da rotação
                getWidth(), getHeight(),    // dimensões
                1, 1,                       // escala
                getRotation(),              // rotação
                0, 0,                       // região da textura
                textureCaverna.getWidth(), textureCaverna.getHeight(),
                false, false);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public boolean isInteragido() {
        return interagido;
    }

    public void setInteragido(boolean interagido) {
        this.interagido = interagido;
    }

    public void dispose() {
        if (textureCaverna != null) {
            textureCaverna.dispose();
        }
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }
}