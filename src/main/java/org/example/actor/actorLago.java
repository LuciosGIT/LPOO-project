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
import org.example.itens.Agua;

public class actorLago extends Actor implements Collidable {

    private final Texture texturaLago;
    private final Polygon collider;
    private final Personagem player;
    private final Inventory inventory;
    private final Pureza pureza;

    private int interacoes = 0;

    public actorLago(float x, float y, Personagem player, Inventory inventory) {
        this.texturaLago = new Texture(Gdx.files.internal("imagens/itens do cenario/lago.png"));
        this.player = player;
        this.inventory = inventory;
        this.pureza = MathUtils.randomBoolean(0.7f) ? Pureza.POTAVEL : Pureza.CONTAMINADA;

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

        Action animacaoCombinada = Actions.parallel(
                Actions.sequence(
                        Actions.rotateBy(2, 0.1f),
                        Actions.rotateTo(0, 0.1f),
                        Actions.rotateBy(-2, 0.1f),
                        Actions.rotateTo(0, 0.1f),
                        Actions.rotateBy(1.5f, 0.1f),
                        Actions.rotateTo(0, 0.1f)
                ),
                Actions.sequence(
                        Actions.alpha(0.5f, 0.1f),
                        Actions.alpha(1f, 0.1f)
                )
        );
        this.addAction(animacaoCombinada);

        interacoes++;
        System.out.println("Interação com lago: " + interacoes);

        if (interacoes >= 5) {
            try {
                Item agua = new Agua(
                        pureza == Pureza.POTAVEL ? "Água Potável" : "Água Contaminada",
                        player,
                        0.5,
                        1.0,
                        0.5,
                        pureza,
                        25.0
                );

                if (player != null && player.getInventario() != null) {
                    player.getInventario().adicionarItem(agua);
                    inventory.updateInventory();
                    System.out.println("Item adicionado: " + agua.getNomeItem() + " (" + pureza + ")");
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
