package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import org.example.itens.Agua;

public class actorLago extends Actor implements Collidable {

    private final Texture texturaLago;
    private Polygon collider;
    private int interacoes = 0;
    private final Personagem player;
    private final Inventory inventory;

    public actorLago(float x, float y, Personagem player, Inventory inventory) {
        this.texturaLago = new Texture(Gdx.files.internal("imagens/itens do cenario/lago.png"));
        this.player = player;
        this.inventory = inventory;

        setBounds(x, y, texturaLago.getWidth() * 0.5f, texturaLago.getHeight() * 0.5f);
        setPosition(x, y);
        setZIndex(10);
        setOrigin(getWidth() / 2, getHeight() / 2);

        float[] vertices = new float[]{
                0, 0,
                getWidth(), 0,
                getWidth(), getHeight(),
                0, getHeight()
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
        batch.draw(texturaLago,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                1, 1,
                getRotation(),
                0, 0,
                texturaLago.getWidth(), texturaLago.getHeight(),
                false, false);
    }



    public void dispose() {
        texturaLago.dispose();
    }

    private void interagir() {

        this.clearActions();

        Action animacao = Actions.sequence(
                Actions.alpha(0.5f, 0.1f),
                Actions.alpha(1f, 0.1f)
        );
        this.addAction(animacao);

        interacoes++;
        System.out.println("Interação com lago: " + interacoes);

        if (interacoes >= 5) {
            try {
                Item agua = new Agua(
                        "Água Potável",
                        player,
                        0.5,
                        1.0,
                        0.5,
                        Pureza.POTAVEL,
                        25.0
                );

                if (player != null && player.getInventario() != null) {
                    player.getInventario().adicionarItem(agua);
                    inventory.updateInventory();
                    // dispose();
                    // this.remove();
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
}
