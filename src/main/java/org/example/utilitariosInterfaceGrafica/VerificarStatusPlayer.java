package org.example.utilitariosInterfaceGrafica;

import com.badlogic.gdx.Game;
import org.example.domain.Personagem;

public class VerificarStatusPlayer {

    private Personagem player;
    private boolean isMorto = false;

    public VerificarStatusPlayer(Personagem player){
        this.player = player;
    }

   public void verificandoStatus() {



        if (player.getVida() <= 0) {
            isMorto = true;
            System.out.println("O personagem " + player.getNome() + " está morto.");
        } else if (player.getSanidade() <= 0) {
            isMorto = true;
            System.out.println("O personagem " + player.getNome() + " está insano.");
        } else if (player.getEnergia() <= 0 ) {
            isMorto = true;
            System.out.println("O personagem " + player.getNome() + " está exausto: " + player.getEnergia());

        } else if (player.getFome() <= 0) {
            isMorto = true;
            System.out.println("O personagem " + player.getNome() + " está faminto.");
        } else if (player.getSede() <= 0) {
            isMorto = true;
            System.out.println("O personagem " + player.getNome() + " está desidratado.");
        }
    }


    public boolean getIsMorto() {
        return isMorto;
    }

}
