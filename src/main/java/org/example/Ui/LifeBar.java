package org.example.Ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class LifeBar {

    private Skin skin;
    private Pixmap pixmap;
    private Pixmap pixmap2;
    private ProgressBarStyle style;
    private ProgressBar lifeBar;

    public LifeBar(Actor actorPlayer) {

        skin = new Skin();

        // Cria e configura o pixmap vermelho
        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        Texture redTexture = new Texture(pixmap);
        TextureRegionDrawable redDrawable = new TextureRegionDrawable(new TextureRegion(redTexture));

        // Cria e configura o pixmap verde
        pixmap2 = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap2.setColor(Color.GREEN);
        pixmap2.fill();
        Texture greenTexture = new Texture(pixmap2);
        TextureRegionDrawable greenDrawable = new TextureRegionDrawable(new TextureRegion(greenTexture));

        style = new ProgressBarStyle();
        // Use os drawables diretamente, sem tentar obtê-los do skin
        style.background = redDrawable;
        style.knobBefore = greenDrawable;

        lifeBar = new ProgressBar(0, 100, 1, false, style);
        lifeBar.setValue(75);
        lifeBar.setSize(100, 50);

        float barX = actorPlayer.getX() + (actorPlayer.getWidth() / 2) - (lifeBar.getWidth() / 2);
        float barY = actorPlayer.getY() + actorPlayer.getHeight() + 10;
        lifeBar.setPosition(barX, barY);


    }

    public ProgressBar getLifeBar(){
        return lifeBar;
    }

    public void setPosition(Actor actorPlayer) {
        float barX = actorPlayer.getX() + (actorPlayer.getWidth() / 2) - (lifeBar.getWidth() / 2);
        float barY = actorPlayer.getY() + actorPlayer.getHeight() + 10;
        lifeBar.setPosition(barX, barY);
    }

    public void dispose() {
       if(skin != null) skin.dispose();
       if(pixmap != null) pixmap.dispose();
       if(pixmap2 != null) pixmap2.dispose();
    }

    //to do: set lifebar value according to player life


}
