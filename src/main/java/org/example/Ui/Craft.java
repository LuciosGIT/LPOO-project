package org.example.Ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.example.actor.actorPersonagem;
import org.example.domain.Item;
import org.example.itens.Materiais;
import org.example.enums.TipoMaterial;

import java.util.ArrayList;
import java.util.List;

public class Craft {
    private Skin skin;
    private Dialog dialog;
    private Stage stage;
    private List<ImageButton> buttons;
    private actorPersonagem actorPlayer;
    private List<Item> inventarioDoPlayer;
    private Inventory inventory;

    public Craft(Stage stage, String title, String message, actorPersonagem actorPlayer, Inventory inventory) {
        this.stage = stage;
        this.skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));
        this.dialog = new Dialog(title, skin);
        this.buttons = new ArrayList<>();
        this.actorPlayer = actorPlayer;
        this.inventarioDoPlayer = actorPlayer.getPlayer().getInventario().getListaDeItems();
        this.inventory = inventory;;

        this.dialog.setVisible(false);
        stage.addActor(this.dialog);

        // Create and add multiple image buttons
        String[] imagePaths = {
                "imagens/assets/itens/materiais/blocoDePedra.png",
                "imagens/assets/itens/materiais/placaDeMetalReforçada.png",
                "imagens/assets/itens/materiais/tábuaReforçada.png"

        };

        for (int i = 0; i < imagePaths.length; i++) {
            Texture buttonTexture = new Texture(Gdx.files.internal(imagePaths[i]));
            ImageButton imageButton = new ImageButton(new TextureRegionDrawable(buttonTexture));
            final int index = i; // Needed for use in the listener

            imageButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    switch (index) {
                        case 0 -> metodoBlocoDePedra();
                        case 1 -> metodoPlacaDeMetal();
                        case 2 -> metodoTabuaReforcada();
                    }
                }
            });

            buttons.add(imageButton);
            dialog.getButtonTable().add(imageButton).size(50, 50).pad(5);
        }

        dialog.setSize(200, 80);
        dialog.setModal(true);
    }

    private void metodoTabuaReforcada() {

        Materiais madeiraPrincipal = null;
        Materiais madeiraSecundaria = null;
        int madeirasEncontradas = 0;

        // Procura por 2 madeiras no inventário
        for (Item item : actorPlayer.getPlayer().getInventario().getListaDeItems()) {
            if (item instanceof Materiais materiais && materiais.getTipoMaterial() == TipoMaterial.MADEIRA) {
                if (madeiraPrincipal == null) {
                    madeiraPrincipal = materiais;
                    madeirasEncontradas++;
                } else if (madeiraSecundaria == null) {
                    madeiraSecundaria = materiais;
                    madeirasEncontradas++;
                }
                if (madeirasEncontradas == 2) break;
            }
        }

        // Se encontrou 2 madeiras, combina elas
        if (madeirasEncontradas == 2) {
            try{
                madeiraPrincipal.combinar(madeiraSecundaria);
                inventory.updateInventory();
                System.out.println("Tabua Reforçada craftada!");
            } catch (Exception e) {
                System.out.println("Erro ao combinar: " + e.getMessage());
            }

        } else {
            System.out.println("Materiais insuficientes!");
        }

    }

    private void metodoPlacaDeMetal() {

        Materiais metalPrincipal = null;
        Materiais metalSecundario = null;
        int metaisEncontrados = 0;

        for (Item item : actorPlayer.getPlayer().getInventario().getListaDeItems()) {
            if (item instanceof Materiais materiais && materiais.getTipoMaterial() == TipoMaterial.METAL){
                if (metalPrincipal == null) {
                    metalPrincipal = materiais;
                    metaisEncontrados++;
                } else if (metalSecundario == null) {
                    metalSecundario = materiais;
                    metaisEncontrados++;
                }
                if (metaisEncontrados == 2) break;
            }
        }

        if (metaisEncontrados == 2) {
            try{
                metalPrincipal.combinar(metalSecundario);
                inventory.updateInventory();
                System.out.println("Placa de Metal craftada!");
            } catch (Exception e) {
                System.out.println("Erro ao combinar: " + e.getMessage());
            }

        } else {
            System.out.println("Materiais insuficientes!");
        }

    }

    private void metodoBlocoDePedra() {
        Materiais pedraPrincipal = null;
        Materiais pedraSecundaria = null;
        int pedrasEncontradas = 0;

        for (Item item : actorPlayer.getPlayer().getInventario().getListaDeItems()) {
            if (item instanceof Materiais materiais && materiais.getTipoMaterial() == TipoMaterial.PEDRA) {
                if (pedraPrincipal == null) {
                    pedraPrincipal = materiais;
                    pedrasEncontradas++;
                } else if (pedraSecundaria == null) {
                    pedraSecundaria = materiais;
                    pedrasEncontradas++;
                }
                if (pedrasEncontradas == 2) break;
            }
        }

        if (pedrasEncontradas == 2) {
            try {
                pedraPrincipal.combinar(pedraSecundaria);
                inventory.updateInventory();
                System.out.println("Bloco de Pedra craftado!");
            } catch (Exception e) {
                System.out.println("Erro ao combinar: " + e.getMessage());
            }
        } else {
            System.out.println("Materiais insuficientes!");
        }
    }

    public boolean isVisible() {
        return dialog.isVisible();
    }

    public void setPosition(actorPersonagem player){
        float dialogX = player.getX() + player.getWidth() / 2 - dialog.getWidth() / 2;
        float dialogY = player.getY() + player.getHeight() + 10; // 10 pixels above the player
        dialog.setPosition(dialogX, dialogY);
    }

    public void show() {
        dialog.setVisible(true);
        dialog.toFront();
        dialog.getColor().a = 0f; // Start fully transparent
        dialog.addAction(Actions.sequence(
                Actions.fadeIn(0.5f)
        ));
    }

    public void hide() {
        dialog.addAction(Actions.sequence(
                Actions.fadeOut(0.5f),
                Actions.run(() -> dialog.setVisible(false))
        ));
    }

    public void dispose() {
        if (skin != null) {
            skin.dispose();
        }
    }
}