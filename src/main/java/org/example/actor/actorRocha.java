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
import org.example.enums.TipoMaterial;
import org.example.itens.Materiais;
import java.util.List;

public class actorRocha extends Actor implements Collidable {

        private final Texture texturaRocha;
        private Polygon collider;
        private int batidas = 0;
        private Personagem player;
        private Inventory inventory;
        private boolean isRocha;
        private boolean isRochaComFerro;

        public actorRocha(float x, float y, Personagem player, Inventory inventory) {
            if(MathUtils.random() < 0.7f) {
                isRocha = true;
                isRochaComFerro = false;
                texturaRocha = new Texture(Gdx.files.internal("imagens/itens do cenario/rocha.png"));
            } else {
                isRocha = false;
                isRochaComFerro = true;
                texturaRocha = new Texture(Gdx.files.internal("imagens/itens do cenario/rochaFerro.png"));
            }

            setBounds(x, y, texturaRocha.getWidth()*0.5f, texturaRocha.getHeight()*0.5f);
            setPosition(x, y);

            setZIndex(10);

            setSize(0.1f * texturaRocha.getWidth(), 0.1f * texturaRocha.getHeight());

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
            batch.draw(texturaRocha,
                    getX(), getY(),             // posição
                    getOriginX(), getOriginY(), // origem da rotação
                    getWidth(), getHeight(),    // dimensões
                    1, 1,                       // escala
                    getRotation(),              // rotação
                    0, 0,                       // região da textura
                    texturaRocha.getWidth(), texturaRocha.getHeight(),
                    false, false);
        }

        public void dispose() {
            texturaRocha.dispose();
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
                System.out.println("Pedra quebrada");
                try {

                    if (Math.random() < 0.85 && isRocha) {
                        Item pedra = new Materiais("Pedra",
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
                    }else{
                        Item ferro = new Materiais("Ferro",
                                player,
                                3.0,
                                90.0,
                                0.2,
                                20.0,
                                TipoMaterial.METAL);

                        if (player != null && player.getInventario() != null) {
                            player.getInventario().adicionarItem(ferro);
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

        public Boolean checkCollision(List<actorRocha> rochas) {
            for (actorRocha rocha : rochas) {
                if (rocha != this && collider.getBoundingRectangle().overlaps(rocha.collider.getBoundingRectangle())) {
                   return true;
                }
            }
            return false;
        }

        @Override
        public Polygon getCollider() {
            return collider;
        }


}



