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
import org.example.enums.TipoMaterial;
import org.example.itens.Materiais;

public class actorPilar extends Actor implements Collidable {

    private final Texture texturaPilar;
    private Polygon collider;
    private int batidas = 0;
    private Personagem player;
    private Inventory inventory;

    public actorPilar(float x, float y, Personagem player, Inventory inventory) {
        texturaPilar = new Texture(Gdx.files.internal("imagens/itens do cenario/pilarRuinas.png"));
        setBounds(x, y, texturaPilar.getWidth()*0.5f, texturaPilar.getHeight()*0.5f);
        setPosition(x, y);

        setZIndex(10);

        setOrigin(getWidth()/2, getHeight()/2);

        // Cria um collider retangular na base do pilar
        float baseWidth = getWidth() * 0.7f;    // 70% da largura
        float baseHeight = getHeight() * 0.3f;   // 30% da altura
        float baseX = getWidth() * 0.15f;        // 15% da esquerda
        float baseY = 0;                         // Base do pilar

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
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texturaPilar,
                getX(), getY(),             // posição
                getOriginX(), getOriginY(), // origem da rotação
                getWidth(), getHeight(),    // dimensões
                1, 1,                       // escala
                getRotation(),              // rotação
                0, 0,                       // região da textura
                texturaPilar.getWidth(), texturaPilar.getHeight(),
                false, false);
    }

    public void dispose() {
        texturaPilar.dispose();
    }

    private void interagir() {
        this.clearActions();

        // Efeito de tremor mais sutil para o pilar
        Action tremer = Actions.sequence(
                Actions.moveBy(2, 0, 0.05f),
                Actions.moveBy(-4, 0, 0.05f),
                Actions.moveBy(4, 0, 0.05f),
                Actions.moveBy(-2, 0, 0.05f)
        );

        if(batidas < 5) { // Pilares são mais resistentes que árvores
            batidas++;
            System.out.println("Batida no pilar: " + batidas);
            this.addAction(tremer);
        } else {
            System.out.println("Pilar quebrado");
            try {
                // Chance de dropar pedras antigas
                if (Math.random() < 0.7) {
                    Item pedra = new Materiais("Pedra Antiga",
                            player,
                            3.0,
                            90.0,
                            0.2,
                            20.0,
                            TipoMaterial.PEDRA);

                    if (player != null && player.getInventario() != null) {
                        player.getInventario().adicionarItem(pedra);
                        inventory.updateInventory();
                    }
                }

                // Chance menor de dropar um fragmento de ruína
                if (Math.random() < 0.3) {
                    Item fragmento = new Materiais("Madeira",
                            player,
                            2.5,
                            70.0,
                            0.3,
                            15.0,
                            TipoMaterial.PEDRA);

                    if (player != null && player.getInventario() != null) {
                        player.getInventario().adicionarItem(fragmento);
                        inventory.updateInventory();
                    }
                }

                // Remove o collider e o ator
                collider = null;
                dispose();
                this.remove();

            } catch (Exception e) {
                System.err.println("Error adding item to inventory: " + e.getMessage());
            }
        }
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }
}