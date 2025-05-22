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

public class HungerBar {

    private Skin skin;
    private Pixmap pixmapBackground;
    private Pixmap pixmapKnob;
    private ProgressBarStyle style;
    private ProgressBar hungerBar;

    public HungerBar(Actor actorPlayer) {
        skin = new Skin();

        // Fundo da barra (vermelho escuro)
        pixmapBackground = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapBackground.setColor(Color.BROWN);
        pixmapBackground.fill();
        Texture backgroundTexture = new Texture(pixmapBackground);
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        // Parte preenchida da barra (amarelo)
        pixmapKnob = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapKnob.setColor(Color.YELLOW);
        pixmapKnob.fill();
        Texture knobTexture = new Texture(pixmapKnob);
        TextureRegionDrawable knobDrawable = new TextureRegionDrawable(new TextureRegion(knobTexture));

        style = new ProgressBarStyle();
        style.background = backgroundDrawable;
        style.knobBefore = knobDrawable;

        hungerBar = new ProgressBar(0, 100, 1, false, style);
        hungerBar.setValue(100);
        hungerBar.setSize(100, 10); // Tamanho menor que a LifeBar

        setPosition(actorPlayer);
    }

    public ProgressBar getHungerBar() {
        return hungerBar;
    }

    public void setPosition(Actor actorPlayer) {
        float barX = actorPlayer.getX() + (actorPlayer.getWidth() / 2f) - (hungerBar.getWidth() / 2f);
        float barY = actorPlayer.getY() + actorPlayer.getHeight() + 25; // Acima da barra de vida
        hungerBar.setPosition(barX, barY);
    }

    public void setHungerValue(double value) {
        hungerBar.setValue((float) value);
    }

    public float getHungerValue() {
        return hungerBar.getValue();
    }

    public void dispose() {
        if (skin != null) skin.dispose();
        if (pixmapBackground != null) pixmapBackground.dispose();
        if (pixmapKnob != null) pixmapKnob.dispose();
    }
}
