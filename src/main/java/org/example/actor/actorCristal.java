package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.example.Ui.Inventory;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.Pureza;
import org.example.enums.TipoMaterial;
import org.example.itens.Agua;
import org.example.itens.Materiais;

public class actorCristal extends Actor implements Collidable {

    private final Texture texturaCristal;
    private Polygon collider;
    private int interacoes = 0;
    private Personagem player;
    private Inventory inventory;
    private boolean coletado;

    public actorCristal(float x, float y, Personagem player, Inventory inventory) {
        texturaCristal = new Texture(Gdx.files.internal("imagens/itens do cenario/cristal-montanha.png"));
        setBounds(x, y, texturaCristal.getWidth() * 0.5f, texturaCristal.getHeight() * 0.5f);
        setPosition(x, y);

        setZIndex(10);
        setOrigin(getWidth() / 2, getHeight() / 2);

        // Criar um collider simples para o cristal (similar ao da árvore)
        float baseWidth = getWidth() * 0.8f;
        float baseHeight = getHeight() * 0.8f;
        float baseX = getWidth() * 0.1f;
        float baseY = 0;

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

        this.player = player;
        this.inventory = inventory;
        this.coletado = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!coletado) {
            batch.draw(texturaCristal,
                    getX(), getY(),
                    getOriginX(), getOriginY(),
                    getWidth(), getHeight(),
                    1, 1,
                    getRotation(),
                    0, 0,
                    texturaCristal.getWidth(), texturaCristal.getHeight(),
                    false, false);
        }
    }

    public void dispose() {
        texturaCristal.dispose();
    }

    private void interagir() {
        if (coletado) return;

        this.clearActions();

        Action brilhar = Actions.sequence(
                Actions.scaleTo(1.2f, 1.2f, 0.1f),
                Actions.scaleTo(1.0f, 1.0f, 0.1f),
                Actions.scaleTo(1.2f, 1.2f, 0.1f),
                Actions.scaleTo(1.0f, 1.0f, 0.1f)
        );

        if (interacoes < 3) {
            interacoes++;
            System.out.println("Interação com o cristal: " + interacoes);
            this.addAction(brilhar);
        } else {
            System.out.println("Cristal coletado - água obtida");
            try {
                Item agua = new Agua("Água de Degelo",
                        player,
                        0.5,
                        1.0,
                        0.5,
                        Pureza.CONTAMINADA,
                        25.0
                );

                if (player != null && player.getInventario() != null) {
                    player.getInventario().adicionarItem(agua);
                    inventory.updateInventory();
                    coletado = true;
                    collider = null;

                    // Efeito de desaparecimento
                    this.addAction(Actions.sequence(
                            Actions.fadeOut(0.5f),
                            Actions.removeActor()
                    ));
                }
            } catch (Exception e) {
                System.err.println("Erro ao adicionar água ao inventário: " + e.getMessage());
            }
        }
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }

    public boolean isColetado() {
        return coletado;
    }
}