package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.Ui.Message;
import org.example.actor.Collidable;
import org.example.domain.Personagem;
import org.example.enums.TipoFerramenta;
import org.example.itens.Ferramentas;
import org.example.actor.actorPersonagem;
import org.example.domain.Item;
import com.badlogic.gdx.Game;
import org.example.telasDoJogo.TelaDeVitoria;


public class actorMuralha extends Actor implements Collidable {

    private Texture texture;
    private Polygon collider;
    private Personagem player;
    private Game game;
    private actorPersonagem actorPlayer;


    public actorMuralha(actorPersonagem actorPlayer, Game game, float x, float y) {
        this.game = game;
        this.actorPlayer = actorPlayer;
        this.player = actorPlayer.getPlayer();
        this.texture = new Texture(Gdx.files.internal("imagens/itens do cenario/muralha.png"));
        setBounds(x, y, texture.getWidth()*8, texture.getHeight()*5);
        setPosition(-500, 700);

        setZIndex(10);

        setOrigin(getWidth()/2, getHeight()/2);

        // Cria um collider retangular na base do pilar
        float baseWidth = texture.getWidth()*8 ;    // 70% da largura
        float baseHeight = 100 ;   // 30% da altura
        float baseX = -100;        // 15% da esquerda
        float baseY = 600;                         // Base do pilar

        float[] vertices = new float[] {
                baseX, baseY,
                baseX + baseWidth, baseY,
                baseX + baseWidth, baseY + baseHeight,
                baseX, baseY + baseHeight
        };

        this.collider = new Polygon(vertices);
        collider.setPosition(x, y);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                interagir(player);
            }
        });


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,
                getX(), getY(),             // posição
                getOriginX(), getOriginY(), // origem da rotação
                getWidth(), getHeight(),    // dimensões
                1, 1,                       // escala
                getRotation(),              // rotação
                0, 0,                       // região da textura
                texture.getWidth(), texture.getHeight(),
                false, false);
    }


    public void dispose() {
        texture.dispose();
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }

    public void interagir(Personagem player) {
        for(Item item : player.getInventario().getListaDeItems())
        {
            if(item instanceof Ferramentas){
                Ferramentas ferramenta = (Ferramentas) item;
                if(ferramenta.getTipoFerramenta() == TipoFerramenta.CHAVE_ESPECIAL){
                    game.setScreen(new TelaDeVitoria(game));
                }
            }
        }
        Message message = new Message("Você precisa de uma chave especial para abrir esta muralha.", "Muralha", actorPlayer.getStage(),actorPlayer.getStage().getWidth()/2 - this.getWidth()/10, actorPlayer.getStage().getHeight()-250);
        message.show();
    }

    public void checkCollision(actorPersonagem objeto) {

        Polygon objCollider = objeto.getCollider();

        if (Intersector.overlapConvexPolygons(collider, objCollider)) {
            objeto.clearActions();


            objeto.addAction(Actions.moveBy(0,-10, 0.01f));

        }



    }

}
