package org.example.utilitarios.telas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Timer;

public class Inputs {
    public void inputListener(){

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            executarTeclaQ();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            executarTeclaW();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            executarTeclaE();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            executarTeclaR();
        }

    }

    private void executarTeclaW() {
         System.out.println("Tecla W pressionada");

         Timer.schedule(new Timer.Task(){
             @Override
             public void run(){
                 Gdx.app.exit();
             }
         }, 1.0f); // Executa após 1 segundo




    }
    private void executarTeclaQ() {
        System.out.println("Tecla Q pressionada");
    }
    private void executarTeclaE() {
        System.out.println("Tecla E pressionada");
    }
    private void executarTeclaR() {
        System.out.println("Tecla R pressionada");
    }

}
