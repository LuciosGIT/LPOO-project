package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.Ui.Inventory;
import org.example.domain.Personagem;

public class actorRio extends Actor implements Collidable {
    private final Texture texturaRio;
    private Polygon collider;
    private final Personagem player;
    private final Inventory inventory;

    public actorRio(float x, float y, float largura, float altura, Personagem player, Inventory inventory) {
        this.player = player;
        this.inventory = inventory;

        // Carrega a textura do rio
        texturaRio = new Texture(Gdx.files.internal("imagens/itens do cenario/rio.png"));

        // Define a posição e tamanho do ator
        setBounds(x, y, largura, altura);

        // Define o Z-index para que o rio fique abaixo de outros elementos
        setZIndex(5);

        // Cria o collider para o rio (toda a área do rio)
        float[] vertices = new float[] {
                0, 0,                // Canto inferior esquerdo
                largura, 0,          // Canto inferior direito
                largura, altura,     // Canto superior direito
                0, altura            // Canto superior esquerdo
        };

        collider = new Polygon(vertices);
        collider.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Desenha a textura do rio
        batch.draw(texturaRio,
                getX(), getY(),             // posição
                getOriginX(), getOriginY(), // origem da rotação
                getWidth(), getHeight(),    // dimensões
                1, 1,                       // escala
                getRotation(),              // rotação
                0, 0,                       // região da textura
                texturaRio.getWidth(), texturaRio.getHeight(),
                false, false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Atualiza a posição do collider se o ator se mover
        collider.setPosition(getX(), getY());
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }

    // Método para aplicar efeito de corrente do rio ao jogador
    public void aplicarEfeitoDeCorrente(actorPersonagem jogador, float delta) {
        // Exemplo: empurrar o jogador para baixo quando estiver no rio
        // Ajuste a força e direção conforme necessário
        float forcaCorrente = 50f * delta;
        jogador.moveBy(0, -forcaCorrente);
    }

    // Método para liberar recursos
    public void dispose() {
        texturaRio.dispose();
    }
}