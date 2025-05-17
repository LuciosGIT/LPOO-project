package org.example.utilitariosInterfaceGrafica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Timer;
import org.example.actor.actorPersonagem;
import org.example.domain.Personagem;

public class Inputs {

    private Personagem player;

    public void inputListener(actorPersonagem actorplayer) {

        player = actorplayer.getPlayer();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            executarTeclaQ();
            player.diminuirVida(5.0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W))
        {
            executarTeclaW(actorplayer);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            executarTeclaA(actorplayer);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S))
        {
            executarTeclaS(actorplayer);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D))
        {
            executarTeclaD(actorplayer);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            executarTeclaE();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            executarTeclaR();
        }

    }

    private void executarTeclaW(actorPersonagem actorplayer) {
         System.out.println("Tecla W pressionada");
    }
    private void executarTeclaA(actorPersonagem actorplayer) {
        System.out.println("Tecla A pressionada");
    }
    private void executarTeclaS(actorPersonagem actorplayer) {
        System.out.println("Tecla S pressionada");
    }
    private void executarTeclaD(actorPersonagem actorplayer) {
        System.out.println("Tecla D pressionada");
    }
    private void executarTeclaQ() {
        System.out.println("Tecla Q pressionada");
        Timer.schedule(new Timer.Task(){
            @Override
            public void run(){
                Gdx.app.exit();
            }
        }, 1.0f);
    }
    private void executarTeclaE() {
        System.out.println("Tecla E pressionada");
    }
    private void executarTeclaR() {
        System.out.println("Tecla R pressionada");
    }

}
