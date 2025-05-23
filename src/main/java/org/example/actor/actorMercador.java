package org.example.actor;

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

public class actorMercador extends Actor implements Collidable {

    private Texture texturaMercador;
    private Polygon collider;
    private String nome;
    private Stage stage;

    // Novos campos para armazenar os offsets calculados do collider
    private float colliderOffsetX;
    private float colliderOffsetY;

    public actorMercador(String nome, Stage stage) {
        this.nome = nome;
        this.texturaMercador = new Texture("imagens/sprites/mercador.png");
        this.stage = stage;

        float minX = 300;
        float minY = 300;
        float maxX = 800;
        float maxY = 600;
        setPosition(MathUtils.random(minX, maxX), MathUtils.random(minY, maxY));

        setSize(100, 125);
        setZIndex(10);

        // Define as dimensões do collider
        float colliderWidth = getWidth() * 0.3f;   // 30% da largura do ator
        float colliderHeight = getHeight() * 0.15f; // 15% da altura do ator

        // Calcula o offset para CENTRALIZAR o collider horizontalmente na BASE do ator.
        // Estes são os offsets do (0,0) local do ator para o (0,0) local do collider.
        this.colliderOffsetX = (getWidth() - colliderWidth) / 2;
        this.colliderOffsetY = 0; // O collider começa na base do ator

        // Definir os vértices do Polygon em relação ao seu próprio ponto de origem (0,0).
        // Não incluir os offsets de posicionamento do ator aqui.
        float[] vertices = new float[] {
                0,             0,              // Canto inferior esquerdo do collider
                colliderWidth, 0,              // Canto inferior direito do collider
                colliderWidth, colliderHeight, // Canto superior direito do collider
                0,             colliderHeight  // Canto superior esquerdo do collider
        };

        collider = new Polygon(vertices);
        // Posicionar o collider inicialmente.
        // A posição do collider no mundo será a posição do ator + os offsets calculados.
        setColliderPosition(); // Chamar aqui para posicionar inicialmente

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
        // Atualiza a posição do collider para corresponder à posição atual do ator.
        setColliderPosition();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texturaMercador != null) {
            batch.draw(texturaMercador,
                    getX(), getY(),
                    getWidth(), getHeight()
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

    private void setColliderPosition() {
        // Alinha o collider com a posição atual do ator, adicionando os offsets.
        // Isso moverá o ponto de origem (0,0) do polígono para (getX() + colliderOffsetX, getY() + colliderOffsetY).
        collider.setPosition(getX() + colliderOffsetX, getY() + colliderOffsetY);
    }

    private void interagir() {
        this.clearActions();

        Action tremer = Actions.sequence(
                Actions.moveBy(2, 0, 0.05f),
                Actions.moveBy(-4, 0, 0.05f),
                Actions.moveBy(4, 0, 0.05f),
                Actions.moveBy(-2, 0, 0.05f)
        );

        Message message = new Message("Olá, eu sou o mercador! O que você deseja?","Mercador",stage, getX() - 150, getY() + 200);
        message.show();

        this.addAction(tremer);
    }
}