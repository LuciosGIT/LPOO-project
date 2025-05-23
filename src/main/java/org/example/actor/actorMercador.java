package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.example.Ui.Message;
import org.example.domain.Item;
import org.example.enums.TipoMaterial;
import org.example.itens.Materiais;

public class actorMercador extends Actor implements Collidable {

    private Texture texturaMercador;
    private Polygon collider;
    private String nome;
    private float x;
    private float y;
    private Stage stage;


    public actorMercador(String nome, Stage stage) {
        this.nome = nome;
        this.texturaMercador = new Texture("imagens/sprites/mercador.png");
        this.stage = stage;
        // Position setup
        float minX = 300;
        float minY = 300;
        float maxX = 800;
        float maxY = 600;
        x = MathUtils.random(minX, maxX);
        y = MathUtils.random(minY, maxY);

        // Set size and position first
        setSize(100, 125);
        setPosition(x, y);
        setZIndex(10);

        // Create collider exactly like actorPersonagem
        float width = getWidth() * 0.3f;
        float height = getHeight() * 0.15f;

        float[] vertices = new float[] {
                -width/2, 0,
                width/2, 0,
                width/2, height,
                -width/2, height
        };

        collider = new Polygon(vertices);
        collider.setPosition(x, y); // Initial position same as actor

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                interagir();
            }
        });

        stage.addActor(this);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        setColliderPosition(); // Renamed to match actorPersonagem
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texturaMercador != null) {
            batch.draw(texturaMercador,
                    getX(), getY(),          // posição
                    getWidth(), getHeight()  // dimensões
            );
        }
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }

    public void dispose() {
        texturaMercador.dispose();
    }

    private void setColliderPosition() { // Renamed to match actorPersonagem
        collider.setPosition(getX(), getY());
    }

    private void interagir() {
        this.clearActions();

        // Efeito de tremor mais sutil para o pilar
        Action tremer = Actions.sequence(
                Actions.moveBy(2, 0, 0.05f),
                Actions.moveBy(-4, 0, 0.05f),
                Actions.moveBy(4, 0, 0.05f),
                Actions.moveBy(-2, 0, 0.05f)
        );


        Message message = new Message("Olá, eu sou o mercador! O que você deseja?","Mercador",stage, x-150, y+200);
        message.show();

        this.addAction(tremer);
    }

}
