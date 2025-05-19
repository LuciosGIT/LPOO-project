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

        // Keep collider thin and shift left
        float colliderWidth = largura * 0.3f;  // 10% of original width
        float colliderHeight = altura * 1.38f;  // 130% of original height

        // Move the collider more to the left by adjusting the offset
        float offsetX = (largura - colliderWidth) / 2 - 50f;  // Shifted 50 units left
        float offsetY = -altura * 0.15f;  // Keep vertical offset the same

        setBounds(x, y, largura, altura);

        float[] vertices = new float[]{
                offsetX, offsetY,                    // bottom left
                offsetX + colliderWidth, offsetY,    // bottom right
                offsetX + colliderWidth, colliderHeight + offsetY,  // top right
                offsetX, colliderHeight + offsetY    // top left
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
        float playerX = player.getX();
        float playerY = player.getY();
        float playerWidth = player.getWidth();
        float playerHeight = player.getHeight();

        // Calculate river boundaries using collider
        float riverLeft = getX() + collider.getVertices()[0];
        float riverRight = riverLeft + (collider.getVertices()[2] - collider.getVertices()[0]);
        float riverBottom = getY() + collider.getVertices()[1];
        float riverTop = riverBottom + (collider.getVertices()[5] - collider.getVertices()[1]);

        float pushForce = 4f;

        // Check for collision
        if (playerX + playerWidth > riverLeft && playerX < riverRight &&
                playerY + playerHeight > riverBottom && playerY < riverTop) {

            // Only push in Y direction based on player's vertical position
            if (playerY < riverBottom + (riverTop - riverBottom) / 2) {
                // Player is in lower half of river, push down
                player.setY(player.getY() - pushForce);
            } else {
                // Player is in upper half of river, push up
                player.setY(player.getY() + pushForce);
            }
        }
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }

    public void dispose() {
        textura.dispose();
    }
}
