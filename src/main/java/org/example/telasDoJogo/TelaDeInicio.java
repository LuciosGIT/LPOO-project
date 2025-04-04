package org.example.telasDoJogo;

import org.example.interfaces.ScreenInterface;

public class TelaDeInicio implements ScreenInterface {

    @Override
    public void show() {
        System.out.println("TelaDeInicio - Show method called");
    }

    @Override
    public void hide() {
        System.out.println("TelaDeInicio - Hide method called");
    }

    @Override
    public void resize(int width, int height) {
        System.out.printf("TelaDeInicio - Resize called with width=%d and height=%d%n", width, height);
    }

    @Override
    public void pause() {
        System.out.println("TelaDeInicio - Pause method called");
    }

    @Override
    public void resume() {
        System.out.println("TelaDeInicio - Resume method called");
    }

    @Override
    public void dispose() {
        System.out.println("TelaDeInicio - Dispose method called");
    }

    @Override
    public void render(float delta) {
        System.out.printf("TelaDeInicio - Render called with delta=%.2f%n", delta);
    }
}

