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

import java.util.List;

public class actorPilhaDeRecursos extends Actor implements Collidable {

    private Texture texturePilha;
    private Polygon collider;
    private boolean coletado = false;
    private final Personagem player;
    private final Inventory inventory;
    private final List<Item> recursos;

    public actorPilhaDeRecursos(float x, float y, Personagem player, Inventory inventory, List<Item> recursos) {
        // Inicializar a textura com um valor padrão
        texturePilha = null;

        // Carregar a textura da pilha de recursos
        try {
            texturePilha = new Texture(Gdx.files.internal("imagens/itens do cenario/pilhaDeItensCaverna.png"));
        } catch (Exception e) {
            // Fallback para uma textura alternativa se a principal não existir
            System.out.println("Textura pilhaDeItens.png não encontrada, usando textura alternativa");
        }

        setBounds(x, y, texturePilha.getWidth() * 0.5f, texturePilha.getHeight() * 0.5f);
        setPosition(x, y);

        setZIndex(10);

        setOrigin(getWidth() / 2, getHeight() / 2);

        // Configurar collider (similar ao da árvore)
        float baseWidth = getWidth() * 0.8f;    // 80% da largura
        float baseHeight = getHeight() * 0.4f;   // 40% da altura
        float baseX = getWidth() * 0.1f;         // 10% da esquerda
        float baseY = 0;                         // Base da pilha

        float[] vertices = new float[] {
                baseX, baseY,
                baseX + baseWidth, baseY,
                baseX + baseWidth, baseY + baseHeight,
                baseX, baseY + baseHeight
        };

        collider = new Polygon(vertices);
        collider.setPosition(x, y);

        // Adicionar listener de clique
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                interagir();
            }
        });

        this.player = player;
        this.inventory = inventory;
        this.recursos = recursos;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texturePilha != null) {
            batch.draw(texturePilha,
                    getX(), getY(),             // posição
                    getOriginX(), getOriginY(), // origem da rotação
                    getWidth(), getHeight(),    // dimensões
                    1, 1,                       // escala
                    getRotation(),              // rotação
                    0, 0,                       // região da textura
                    texturePilha.getWidth(), texturePilha.getHeight(),
                    false, false);
        }
    }

    public void dispose() {
        if (texturePilha != null) {
            texturePilha.dispose();
        }
    }

    private void interagir() {
        if (coletado) {
            return;
        }

        this.clearActions();

        // Efeito visual ao coletar
        Action tremer = Actions.sequence(
                Actions.rotateBy(3, 0.05f),
                Actions.rotateTo(0, 0.05f),
                Actions.rotateBy(-3, 0.05f),
                Actions.rotateTo(0, 0.05f),
                Actions.rotateBy(3, 0.05f),
                Actions.rotateTo(0, 0.05f)
        );

        System.out.println("Pilha vasculhada!");

        try {
            if (recursos != null && !recursos.isEmpty()) {
                for (Item item : recursos) {
                    if (player != null && player.getInventario() != null) {
                        player.getInventario().adicionarItem(item);
                        System.out.println("Item adicionado: " + item.getNomeItem());
                    }
                }

                // Atualizar o inventário na interface
                inventory.updateInventory();

                // Adicionar efeito visual e remover
                this.addAction(Actions.sequence(
                        tremer,
                        Actions.fadeOut(0.5f),
                        Actions.run(() -> {
                            collider = null;
                            dispose();
                            this.remove();
                        })
                ));

                coletado = true;
            } else {
                // Apenas tremer se não houver recursos
                this.addAction(tremer);
                System.out.println("Não há recursos para coletar!");
            }
        } catch (Exception e) {
            System.err.println("Error adding item to inventory: " + e.getMessage());
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