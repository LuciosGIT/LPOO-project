package org.example.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.Ui.Inventory;
import org.example.domain.Personagem;

public class actorRio extends Actor implements Collidable {

    private final Texture textura;
    private final Polygon collider;
    private final Personagem personagem;
    private final Inventory inventory;

    private final float correnteForca = 100f;

    public actorRio(float x, float y, float largura, float altura, Personagem personagem, Inventory inventory) {
        this.textura = new Texture("imagens/itens do cenario/rio.png");
        this.personagem = personagem;
        this.inventory = inventory;

        setBounds(x, y, largura, altura);

        float[] vertices = new float[]{
                0, 0,
                largura, 0,
                largura, altura,
                0, altura
        };

        collider = new Polygon(vertices);
        collider.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textura, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void aplicarEfeitoDeCorrente(actorPersonagem player, float deltaTime) {
        player.setY(player.getY() - correnteForca * deltaTime);
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }

    public void dispose() {
        textura.dispose();
    }
}
