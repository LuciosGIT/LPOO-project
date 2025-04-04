package org.example.interfaces;

public interface ScreenInterface {
    public void show();
    public void render(float delta);
    public void resize(int width, int height);
    public void pause();
    public void resume();
    public void hide();
    public void dispose();

}
