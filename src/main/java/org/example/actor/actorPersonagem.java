package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.domain.Personagem;

import java.util.HashMap;

public class actorPersonagem extends Actor {

    private final HashMap<String, TextureRegion> sprites;
    private TextureRegion spriteAtual;

    public actorPersonagem(Personagem player) {
        this.sprites = player.getSprites();
        setPosition(100,100);
        setTexture("parado");
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

}
