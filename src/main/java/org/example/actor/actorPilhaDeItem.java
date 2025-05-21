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
import org.example.ambientes.AmbienteFloresta;
import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoMaterial;
import org.example.itens.Materiais;

public class actorPilhaDeItem extends Actor implements Collidable{

    private Texture textureDaPilha;
    private Polygon collider;
    private int batidas = 0;
    private Personagem player;
    private Inventory inventory;
    private boolean isIstanciado;
    private Ambiente ambiente;

    public actorPilhaDeItem(float x, float y, Personagem player, Inventory inventory, Ambiente ambiente){
        this.textureDaPilha = new Texture(Gdx.files.internal("imagens/itens do cenario/pilhaDeItens.png"));
        setBounds(x,y, textureDaPilha.getWidth()*0.1f, textureDaPilha.getHeight()*0.1f);
        setPosition(x, y);
        this.player = player;
        this.ambiente = ambiente;
        this.inventory = inventory;

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

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureDaPilha,
                getX(), getY(),
                getWidth(), getHeight()
        );
    }

    public void dispose() {
        textureDaPilha.dispose();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
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
            System.out.println("Vasculhando pilha: " + batidas);
            this.addAction(tremer);
        } else {
            System.out.println("Pilha vasculhada!");
            try {

                Item item = ambiente.getRecursosDisponiveis().get(MathUtils.random(0, ambiente.getRecursosDisponiveis().size() - 1));

                player.getInventario().adicionarItem(item);
                inventory.updateInventory();
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
