package org.example.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class actorPonte extends Image {

    public actorPonte(float x, float y, float largura, float altura) {
        super(new Texture("imagens/itens do cenario/ponte.png"));
        setBounds(x, y, largura, altura);
    }
}
