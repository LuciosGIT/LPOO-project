package org.example.utilitariosInterfaceGrafica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import org.example.Ui.Craft;
import org.example.Ui.Inventory;
import org.example.Ui.Message;
import org.example.actor.actorPersonagem;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.Ui.Message;
import java.util.List;

public class Inputs {

    private Personagem player;
    private int keySelect = -1;

    public void inputListener(actorPersonagem actorplayer, Inventory inventory, Craft popUp, Stage hudStage) {
        inputsInventario(inventory);

        player = actorplayer.getPlayer();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            executarTeclaQ();

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            player.diminuirVida(5.0);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            executarTeclaC(popUp, actorplayer);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            executarTeclaT(hudStage, actorplayer);
        }

        inputsInventario(inventory);
        keySelectListener(actorplayer, inventory);

    }

    private void inputsInventario(Inventory inventory) {

        Table inventoryTable = inventory.getInventoryTable();
        List<Item> itensInventario = inventory.getItensInventario();

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            if (inventoryTable.getChildren().size > 0) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(0);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                    keySelect = 1;
                    if (itensInventario.size() > 0) {
                        inventory.setItemSelecionado(itensInventario.get(0));
                    } else {
                        inventory.setItemSelecionado(null);
                    }
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if (inventoryTable.getChildren().size > 1) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(1);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                    keySelect = 2;
                    if (itensInventario.size() > 1) {
                        inventory.setItemSelecionado(itensInventario.get(1));
                    } else {
                        inventory.setItemSelecionado(null);
                    }
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            if (inventoryTable.getChildren().size > 2) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(2);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                    keySelect = 3;
                    if (itensInventario.size() > 2) {
                        inventory.setItemSelecionado(itensInventario.get(2));
                    } else {
                        inventory.setItemSelecionado(null);
                    }
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            if (inventoryTable.getChildren().size > 3) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(3);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                    keySelect = 4;
                    if (itensInventario.size() > 3) {
                        inventory.setItemSelecionado(itensInventario.get(3));
                    } else {
                        inventory.setItemSelecionado(null);
                    }
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            if (inventoryTable.getChildren().size > 4) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(4);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                    keySelect = 5;
                    if (itensInventario.size() > 4) {
                        inventory.setItemSelecionado(itensInventario.get(4));
                    } else {
                        inventory.setItemSelecionado(null);
                    }
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if (inventoryTable.getChildren().size > 1) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(1);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                    keySelect = 2;
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            if (inventoryTable.getChildren().size > 2) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(2);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                    keySelect = 3;
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            if (inventoryTable.getChildren().size > 3) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(3);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                    keySelect = 4;
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            if (inventoryTable.getChildren().size > 4) {
                clearColor(inventoryTable);
                Table slotContainer = (Table) inventoryTable.getChildren().get(4);
                if (slotContainer.getChildren().size > 0) {
                    slotContainer.getChildren().get(0).setColor(Color.GRAY);
                    keySelect = 5;
                }
            }
        }
    }

    private void keySelectListener(actorPersonagem actorplayer, Inventory inventory) {
        List<Item> itensInventario = actorplayer.getPlayer().getInventario().getListaDeItems();

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (keySelect >= 1 && keySelect <= 5) {
                int index = keySelect - 1;
                if (index < itensInventario.size()) {
                    try {
                        actorplayer.getPlayer().getInventario().usarItem(itensInventario.get(index).getNomeItem());
                        inventory.updateInventory();
                    } catch (javax.naming.NameNotFoundException e) {
                        System.out.println("Item not found: " + e.getMessage());
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            if (keySelect >= 1 && keySelect <= 5) {
                int index = keySelect - 1;
                if (index < itensInventario.size()) {
                    try {
                        actorplayer.getPlayer().getInventario().removerItem(itensInventario.get(index).getNomeItem());
                        inventory.updateInventory();
                    } catch (javax.naming.NameNotFoundException e) {
                        System.out.println("Item not found: " + e.getMessage());
                    }
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

    }

    private void executarTeclaA(actorPersonagem actorplayer) {

    }

    private void executarTeclaS(actorPersonagem actorplayer) {

    }

    private void executarTeclaD(actorPersonagem actorplayer) {

    }

    private void executarTeclaQ() {

        Timer.schedule(new Timer.Task(){
            @Override
            public void run(){
                Gdx.app.exit();
            }
        }, 1.0f);
    }

    private void executarTeclaC(Craft popUp, actorPersonagem actorplayer) {
        if (popUp != null) {
            if (popUp.isVisible()) {
                popUp.hide();
            } else {
                // Add this line to update position before showing
                popUp.setPosition(actorplayer);
                popUp.show();
            }
        }
    }

    private void executarTeclaT(Stage hudStage, actorPersonagem actorPlayer) {

        Personagem player = actorPlayer.getPlayer();

        String content = "Nome: " + player.getNome() + "\n" +
                "Vida: " + player.getVida() + "\n" +
                "FOME: " + player.getFome() + "\n" +
                "SEDE: " + player.getSede() + "\n" +
                "ENERGIA: " + player.getEnergia() + "\n" +
                "SANIDADE: " + player.getSanidade() + "\n";

        Message message = new Message(content, "STATUS", hudStage, 10, hudStage.getHeight());
        message.show();

    }

}
