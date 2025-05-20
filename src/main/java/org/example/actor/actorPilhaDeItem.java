package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.Ui.Inventory;
import org.example.domain.Personagem;

public class actorPilhaDeItem extends Actor {

    private Texture textureDaPilha;
    private Polygon collider;
    private int batidas = 0;
    private Personagem player;
    private Inventory inventory;
    private boolean isIstanciado;

    public actorPilhaDeItem(float x, float y, Personagem player, Inventory inventory){
        this.textureDaPilha = new Texture(Gdx.files.internal("imagens/itens do cenario/pilhaDeItens.png"));
        setBounds(x,y, textureDaPilha.getWidth()*0.1f, textureDaPilha.getHeight()*0.1f);
        setPosition(x, y);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureDaPilha,
                getX(), getY(),
                getWidth(), getHeight()
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }




}
