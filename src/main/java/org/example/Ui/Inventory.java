package org.example.Ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.example.actor.actorPersonagem;
import org.example.domain.Item;
import org.example.itens.Inventario;
import java.util.List;

public class Inventory {
    private Table inventoryTable;

    private final List<Item> itensInventario;

    public Inventory(Stage stage, int cols, actorPersonagem actorPlayer) {
        inventoryTable = new Table();
        stage.addActor(inventoryTable);

        // Garante que o inventário fique na frente de tudo
        inventoryTable.setZIndex(9999);
        stage.setDebugAll(false);

        for (int i = 0; i < cols; i++) {
            Drawable slotDrawable = createSlotDrawable();
            Image slotImage = new Image(slotDrawable);
            inventoryTable.add(slotImage).size(64, 64).pad(5);
        }

        inventoryTable.pack();

        Inventario inventarioDoPlayer = actorPlayer.getPlayer().getInventario();
        itensInventario = inventarioDoPlayer.getListaDeItems();
    }

    public void setPosition(OrthographicCamera camera) {
        float cameraX = camera.position.x;
        float cameraY = camera.position.y;
        float viewportWidth = camera.viewportWidth;
        float viewportHeight = camera.viewportHeight;

        inventoryTable.setPosition(
                cameraX - inventoryTable.getWidth() / 2,
                cameraY - (viewportHeight/2 * camera.zoom) + 50 // 50 pixels da parte inferior
        );

        // Força o inventário a ficar sempre no topo da hierarquia
        inventoryTable.toFront();
    }

    private Drawable createSlotDrawable() {
        // Create a pixmap for the slot background
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.3f, 0.3f, 0.3f, 0.7f)); // Semi-transparent dark gray
        pixmap.fill();
        pixmap.setColor(new Color(0.4f, 0.4f, 0.4f, 1f)); // Lighter gray for border
        pixmap.drawRectangle(0, 0, 64, 64);

        // Create texture from pixmap
        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        // Create NinePatch for better scaling
        NinePatch patch = new NinePatch(texture, 2, 2, 2, 2);
        return new NinePatchDrawable(patch);
    }

    public void updateInventory() {
        inventoryTable.clearChildren();

        for (int i = 0; i < itensInventario.size(); i++) {
            // Create a container for each slot
            Table slotContainer = new Table();

            // Add slot background
            Drawable slotDrawable = createSlotDrawable();
            Image slotImage = new Image(slotDrawable);
            slotContainer.add(slotImage).size(64, 64);

            // Add item image if exists
            Item item = itensInventario.get(i);
            if (item != null) {
                String imagePath = item.getImage();
                if (imagePath != null) {
                    Texture itemTexture = new Texture(imagePath);
                    Image itemImage = new Image(itemTexture);
                    itemImage.setSize(48, 48);
                    // Stack item on top of slot
                    slotContainer.add(itemImage).size(48, 48).expand().center();
                }
            }

            inventoryTable.add(slotContainer).pad(5);
        }

        inventoryTable.pack();
    }
}