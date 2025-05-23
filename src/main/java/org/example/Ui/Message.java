package org.example.Ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Message {

    private Skin skin;
    private Dialog dialog;
    private Stage stage;
    private String content;

    public Message(String content, String name, Stage stage, float posx, float posy) {

        if (stage == null) throw new IllegalArgumentException("O stage não pode ser nulo");
        if (content == null) content = "";
        if (name == null) name = "";

        this.skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));
        this.dialog = new Dialog(name, skin);
        this.stage = stage;
        this.content = content;

        dialog.text(content);
        dialog.pack();

        dialog.setPosition(
                posx,
                posy
        );

        dialog.setVisible(false);
        stage.addActor(this.dialog);

        dialog.addAction(Actions.sequence(
                Actions.fadeIn(0.5f),
                Actions.delay(3f), // Wait 3 seconds
                Actions.fadeOut(0.5f),
                Actions.run(() -> dialog.setVisible(false))
        ));

    }

    public Dialog getDialog() {
        return dialog;
    }

    public void show() {
        if (dialog == null) throw new IllegalArgumentException("O dialog não pode ser nulo");
        dialog.setVisible(true);
        dialog.toFront();
        dialog.getColor().a = 0f;
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
        if (dialog != null) {
            dialog.remove(); // Remove from stage
        }
        if (skin != null) {
            skin.dispose();
        }
    }

}
