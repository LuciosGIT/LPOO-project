package org.example;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.example.game.MyGame;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class DesktopLauncher {
    public static void main(String[] args) {
        // Configuração do aplicativo (janela)
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Última Fronteira");
        config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        config.setWindowedMode(1920, 1080);  // Resolução base
        config.setResizable(true);
        new Lwjgl3Application(new MyGame(), config);  // Inicia o jogo
    }
}