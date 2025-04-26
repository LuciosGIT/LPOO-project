package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.example.domain.Personagem;

public class actorArvore extends Actor{

    private Texture textureArvore;

    public actorArvore(float x, float y) {
        textureArvore = new Texture(Gdx.files.internal("imagens/itens do cenario/arvoreFloresta.png"));
        setBounds(x,y, textureArvore.getWidth()*0.5f, textureArvore.getHeight()*0.5f);
        setPosition(x, y);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                interagir();
            }
        });



    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureArvore,
                getX(), getY(),
                getWidth(), getHeight());
    }


    public void dispose() {
        textureArvore.dispose();
    }

    private void interagir() {

        System.out.println("oi esta funcionando");

    }





}
