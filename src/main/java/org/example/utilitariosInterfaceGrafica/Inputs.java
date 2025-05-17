package org.example.utilitariosInterfaceGrafica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import org.example.Ui.Inventory;
import org.example.actor.actorPersonagem;
import org.example.domain.Personagem;

public class Inputs {

    private Personagem player;

    public void inputListener(actorPersonagem actorplayer, Inventory inventory) {

        player = actorplayer.getPlayer();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            executarTeclaQ();
            player.diminuirVida(5.0);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            executarTeclaW(actorplayer);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            executarTeclaA(actorplayer);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            executarTeclaS(actorplayer);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            executarTeclaD(actorplayer);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            executarTeclaE();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            executarTeclaR();
        }


        inputsInventario(inventory);

    }

    private void inputsInventario(Inventory inventory) {
        Table inventoryTable = inventory.getInventoryTable();

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            if (inventoryTable.getChildren().size > 0) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(0);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if (inventoryTable.getChildren().size > 1) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(1);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            if (inventoryTable.getChildren().size > 2) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(2);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            if (inventoryTable.getChildren().size > 3) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(3);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            if (inventoryTable.getChildren().size > 4) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(4);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                }
            }
        }
    }

    private void clearColor(Table inventoryTable) {
        for (int i = 0; i < inventoryTable.getChildren().size; i++) {
            Table slotContainer = (Table) inventoryTable.getChildren().get(i);
            if (slotContainer.getChildren().size > 0) {
                slotContainer.getChildren().get(0).setColor(Color.WHITE);
            }
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
