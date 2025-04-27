package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.example.domain.Personagem;

import java.util.HashMap;
import java.util.List;

public class actorPersonagem extends Actor {

    private final HashMap<String, TextureRegion> sprites;
    private TextureRegion spriteAtual;
    private Polygon collider;

    public actorPersonagem(Personagem player) {
        this.sprites = player.getSprites();
        setPosition(100, 100);
        setTexture("parado");

        float[] vertices = new float[]{
                0, 0,                                              // Inferior esquerdo
                spriteAtual.getRegionWidth() * 0.3f, 0,            // Inferior direito
                spriteAtual.getRegionWidth() * 0.3f, spriteAtual.getRegionHeight() * 0.3f, // Superior direito
                0, spriteAtual.getRegionHeight() * 0.3f            // Superior esquerdo
        };

        collider = new Polygon(vertices);
        collider.setPosition(100,100);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setColliderPosition();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (spriteAtual != null) {
            batch.draw(spriteAtual,
                    getX(), getY(),          // posição
                    getWidth(), getHeight()  // dimensões
            );
        }
    }

    public void setTexture(String key) {
        if (sprites.containsKey(key)) {

             spriteAtual = sprites.get(key);
            // Atualiza os bounds do actor de acordo com as dimensões do novo sprite
            setBounds(getX(), getY(), spriteAtual.getRegionWidth(), spriteAtual.getRegionHeight());
        } else {
            // Trava uma mensagem de log ou lance uma exceção se a chave não for encontrada
            Gdx.app.log("ActorPersonagem", "Chave de textura não encontrada: " + key);
            // Opcional: você pode definir um sprite default ou manter o anterior
        }
    }

    private void setColliderPosition() {
        collider.setPosition(this.getX(), this.getY());
    }



    public void checkCollision(List<actorArvore> arvores) {
        for(actorArvore arvore : arvores) {
            if(Intersector.overlaps(collider.getBoundingRectangle(), arvore.getCollider().getBoundingRectangle())) {
                this.addAction(Actions.moveTo(getX()-5, getY()-5, 0.5f));
                break;
            }
        }

    }

}
