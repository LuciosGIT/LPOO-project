package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.example.domain.Personagem;

import java.util.HashMap;
import java.util.List;

public class actorPersonagem extends Actor implements Collidable{

    private Personagem player;
    private final HashMap<String, TextureRegion> sprites;
    private TextureRegion spriteAtual;
    private Polygon collider;

    private static final float CHARACTER_WIDTH = 100f;
    private static final float CHARACTER_HEIGHT = 100f;

    public actorPersonagem(Personagem player) {
        this.sprites = player.getSprites();
        setPosition(100, 100);
        setTexture("parado");

        setSize(CHARACTER_WIDTH, CHARACTER_HEIGHT);

        // Ajusta o tamanho do collider
        float width = spriteAtual.getRegionWidth() * 0.3f;   // 30% da largura
        float height = spriteAtual.getRegionHeight() * 0.15f; // 20% da altura

        // Define os vértices centralizados
        float[] vertices = new float[]{
                -width/2, 0,           // Inferior esquerdo
                width/2, 0,            // Inferior direito
                width/2, height,       // Superior direito
                -width/2, height       // Superior esquerdo
        };

        collider = new Polygon(vertices);
        collider.setPosition(getX() + (float) spriteAtual.getRegionWidth() /2, getY());
        this.player = player;
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

    public Personagem getPlayer() {
        return this.player;
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

    // Método existente para verificar colisão com uma lista de objetos
    public void checkCollision(List<? extends Collidable> objetos) {
        for (Collidable objeto : objetos) {
            if (checkCollision(objeto)) {
                break; // Se colidiu com algum objeto, interrompe o loop
            }
        }
    }

    // Novo método para verificar colisão com um único objeto
    public boolean checkCollision(Collidable objeto) {
        Polygon objCollider = objeto.getCollider();

        if (objCollider == null || collider == null) return false;

        if (Intersector.overlapConvexPolygons(collider, objCollider)) {
            this.clearActions();

            // Verifica se o objeto é um Actor para obter sua posição central
            float objCenterX;
            if (objeto instanceof Actor) {
                Actor actorObj = (Actor) objeto;
                objCenterX = actorObj.getX() + actorObj.getWidth() / 2;
            } else {
                // Se não for um Actor, usa a posição do collider
                objCenterX = objCollider.getX();
            }

            float playerCenterX = getX() + getWidth() / 2;
            float moveX = playerCenterX < objCenterX ? -5 : 5;

            addAction(Actions.moveBy(moveX, 0, 0.01f));
            return true;
        }

        return false;
    }

    @Override
    public Polygon getCollider() {
        return collider; // Corrigido para retornar o collider em vez de null
    }

}