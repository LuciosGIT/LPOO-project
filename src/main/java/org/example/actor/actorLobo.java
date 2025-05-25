package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.example.Ui.Inventory;
import org.example.criatura.Corvo;
import org.example.criatura.Lobo;
import org.example.domain.Item;
import org.example.domain.Personagem;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.itens.Armas;

import java.util.List;

public class actorLobo extends Actor implements Collidable {

    private double vida;
    private double dano;
    private Lobo lobo;

    private final Texture texturaCriatura;
    private Polygon collider;
    private Personagem player;
    private Inventory inventory;
    private double velocidade = 250;// velocidade do lobo
    private boolean isMorto = false;

    public actorLobo(Personagem player, Inventory inventory, Lobo lobo) {

        this.lobo = lobo;
        this.player = player;
        this.inventory = inventory;
        vida = lobo.getVida();
        dano = lobo.getDanoDeAtaque();

        texturaCriatura = new Texture(Gdx.files.internal("imagens/sprites/lobo.png"));

        float x = MathUtils.random(0, Gdx.graphics.getWidth()-100);
        float y = MathUtils.random(0, Gdx.graphics.getHeight()-100);

        setBounds(x, y, texturaCriatura.getWidth()*0.5f, texturaCriatura.getHeight()*0.5f);
        setPosition(x, y);

        setZIndex(10);

        setSize(0.3f * texturaCriatura.getWidth(), 0.3f * texturaCriatura.getHeight());

        setOrigin(getWidth()/2, getHeight()/2);

        float baseWidth = getWidth() * 0.5f;    // 50% da largura
        float baseHeight = getHeight() * 0.2f;   // 20% da altura
        float baseX = getWidth() * -0.1f;        // posição X
        float baseY = getWidth() * 0.2f;        // posição Y

        float[] vertices = new float[] {
                baseX, baseY,
                baseX + baseWidth, baseY,
                baseX + baseWidth, baseY + baseHeight,
                baseX, baseY + baseHeight
        };

        collider = new Polygon(vertices);
        collider.setPosition(getX(), getY());

        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                atingido(player);
            }
        });

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texturaCriatura != null) {
            batch.draw(texturaCriatura,
                    getX(), getY(),
                    getWidth(), getHeight()
            );
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Atualizar posição do collider
        if (collider != null) {
            collider.setPosition(getX(), getY());
        }
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }

    public void ataque(actorPersonagem player) {
        // Seguir o jogador
        double distancia = Math.sqrt(Math.pow(player.getX()-getX(),2) + Math.pow(player.getY()-getY(),2));
        float tempo = (float) (distancia / velocidade);

        if(distancia < 50) {
            // Parar movimento quando estiver perto
            clearActions();

            // Executar ataque
            lobo.ataque(player.getPlayer());

            // OPÇÃO 1: Ficar grudado no personagem (comentar as linhas abaixo)
            // Não adiciona nenhuma ação de movimento

            // OPÇÃO 2: Fugir após atacar (descomente se quiser o comportamento original)
            /*
            float posX = MathUtils.random(0, Gdx.graphics.getWidth()-100);
            float posY = MathUtils.random(0, Gdx.graphics.getHeight()-100);
            addAction(Actions.moveTo(posX, posY, tempo*1.5f));
            */

        } else {
            // Only add new movement if not already moving to avoid stacking actions
            if (getActions().size == 0) {
                addAction(Actions.moveTo(player.getX(), player.getY(), tempo));
            }
        }
    }

    // MÉTODO PRINCIPAL PARA COLISÕES COM OBSTÁCULOS
    // Este método deve ser chamado no game loop para obstáculos que IMPEDEM movimento
    public void checkObstacleCollisions(List<actorArvore> arvores, actorMercador mercador, actorAbrigo abrigo) {
        // Verificar colisões com árvores
        if (arvores != null) {
            checkCollisionWithTrees(arvores);
        }

        // Verificar colisão com mercador
        if (mercador != null) {
            checkCollisionWithMercador(mercador);
        }

        // Verificar colisão com abrigo
        if (abrigo != null) {
            checkCollisionWithAbrigo(abrigo);
        }
    }

    // MÉTODO SEPARADO PARA INTERAÇÃO COM PERSONAGEM
    // Este método deve ser chamado separadamente e NÃO impede movimento
    public boolean isNearPlayer(actorPersonagem playerActor) {
        if (playerActor == null) return false;

        double distancia = Math.sqrt(Math.pow(playerActor.getX()-getX(),2) + Math.pow(playerActor.getY()-getY(),2));
        return distancia < 60; // Range de interação com o personagem
    }

    // Collision detection with trees - RANGE REDUZIDO
    public void checkCollisionWithTrees(List<actorArvore> arvores) {
        float colliderReduction = 0.8f;
        float wolfColliderWidth = getWidth() * colliderReduction;
        float wolfColliderHeight = getHeight() * colliderReduction;
        float wolfColliderX = getX() + (getWidth() - wolfColliderWidth) / 2;
        float wolfColliderY = getY() + (getHeight() - wolfColliderHeight) / 2;

        Rectangle wolfBounds = new Rectangle(wolfColliderX, wolfColliderY, wolfColliderWidth, wolfColliderHeight);

        for (actorArvore arvore : arvores) {
            float treeColliderReduction = 0.7f;
            float treeColliderWidth = arvore.getWidth() * treeColliderReduction;
            float treeColliderHeight = arvore.getHeight() * treeColliderReduction;
            float treeColliderX = arvore.getX() + (arvore.getWidth() - treeColliderWidth) / 2;
            float treeColliderY = arvore.getY() + (arvore.getHeight() - treeColliderHeight) / 2;

            Rectangle treeBounds = new Rectangle(treeColliderX, treeColliderY,
                    treeColliderWidth, treeColliderHeight);

            if (wolfBounds.overlaps(treeBounds)) {
                handleTreeCollision(arvore);
                break; // Sair do loop após primeira colisão
            }
        }
    }

    // Collision detection with mercador - RANGE REDUZIDO
    public void checkCollisionWithMercador(actorMercador mercador) {
        float colliderReduction = 0.8f;
        float wolfColliderWidth = getWidth() * colliderReduction;
        float wolfColliderHeight = getHeight() * colliderReduction;
        float wolfColliderX = getX() + (getWidth() - wolfColliderWidth) / 2;
        float wolfColliderY = getY() + (getHeight() - wolfColliderHeight) / 2;

        Rectangle wolfBounds = new Rectangle(wolfColliderX, wolfColliderY, wolfColliderWidth, wolfColliderHeight);

        float mercadorColliderReduction = 0.75f;
        float mercadorColliderWidth = mercador.getWidth() * mercadorColliderReduction;
        float mercadorColliderHeight = mercador.getHeight() * mercadorColliderReduction;
        float mercadorColliderX = mercador.getX() + (mercador.getWidth() - mercadorColliderWidth) / 2;
        float mercadorColliderY = mercador.getY() + (mercador.getHeight() - mercadorColliderHeight) / 2;

        Rectangle mercadorBounds = new Rectangle(mercadorColliderX, mercadorColliderY,
                mercadorColliderWidth, mercadorColliderHeight);

        if (wolfBounds.overlaps(mercadorBounds)) {
            handleMercadorCollision(mercador);
        }
    }

    // Collision detection with abrigo - RANGE REDUZIDO
    public void checkCollisionWithAbrigo(actorAbrigo abrigo) {
        float colliderReduction = 0.8f;
        float wolfColliderWidth = getWidth() * colliderReduction;
        float wolfColliderHeight = getHeight() * colliderReduction;
        float wolfColliderX = getX() + (getWidth() - wolfColliderWidth) / 2;
        float wolfColliderY = getY() + (getHeight() - wolfColliderHeight) / 2;

        Rectangle wolfBounds = new Rectangle(wolfColliderX, wolfColliderY, wolfColliderWidth, wolfColliderHeight);

        float abrigoColliderReduction = 0.7f;
        float abrigoColliderWidth = abrigo.getWidth() * abrigoColliderReduction;
        float abrigoColliderHeight = abrigo.getHeight() * abrigoColliderReduction;
        float abrigoColliderX = abrigo.getX() + (abrigo.getWidth() - abrigoColliderWidth) / 2;
        float abrigoColliderY = abrigo.getY() + (abrigo.getHeight() - abrigoColliderHeight) / 2;

        Rectangle abrigoBounds = new Rectangle(abrigoColliderX, abrigoColliderY,
                abrigoColliderWidth, abrigoColliderHeight);

        if (wolfBounds.overlaps(abrigoBounds)) {
            handleAbrigoCollision(abrigo);
        }
    }

    // Handle collision with tree
    private void handleTreeCollision(actorArvore arvore) {
        clearActions();

        float treeX = arvore.getX() + arvore.getWidth()/2;
        float treeY = arvore.getY() + arvore.getHeight()/2;
        float wolfX = getX() + getWidth()/2;
        float wolfY = getY() + getHeight()/2;

        float dirX = wolfX - treeX;
        float dirY = wolfY - treeY;

        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (length > 0) {
            dirX /= length;
            dirY /= length;

            float newX = wolfX + dirX * 30;
            float newY = wolfY + dirY * 30;
            setPosition(newX - getWidth()/2, newY - getHeight()/2);
        }
    }

    // Handle collision with mercador
    private void handleMercadorCollision(actorMercador mercador) {
        clearActions();

        float mercadorX = mercador.getX() + mercador.getWidth()/2;
        float mercadorY = mercador.getY() + mercador.getHeight()/2;
        float wolfX = getX() + getWidth()/2;
        float wolfY = getY() + getHeight()/2;

        float dirX = wolfX - mercadorX;
        float dirY = wolfY - mercadorY;

        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (length > 0) {
            dirX /= length;
            dirY /= length;

            float newX = wolfX + dirX * 30;
            float newY = wolfY + dirY * 30;
            setPosition(newX - getWidth()/2, newY - getHeight()/2);
        }
    }

    // Handle collision with abrigo
    private void handleAbrigoCollision(actorAbrigo abrigo) {
        clearActions();

        float abrigoX = abrigo.getX() + abrigo.getWidth()/2;
        float abrigoY = abrigo.getY() + abrigo.getHeight()/2;
        float wolfX = getX() + getWidth()/2;
        float wolfY = getY() + getHeight()/2;

        float dirX = wolfX - abrigoX;
        float dirY = wolfY - abrigoY;

        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (length > 0) {
            dirX /= length;
            dirY /= length;

            float newX = wolfX + dirX * 30;
            float newY = wolfY + dirY * 30;
            setPosition(newX - getWidth()/2, newY - getHeight()/2);
        }
    }

    public void atingido(Personagem player){

        double dano = 5;

        for(Item item : player.getInventario().getListaDeItems()){
            if(item instanceof Armas arma){
                dano = arma.getDano();
            }
        }

        vida -= dano;

        System.out.printf("\nDano: %f", dano);

        if (vida <= 0) {
            // Lógica para o lobo morrer
            System.out.println("Lobo morreu!");
            collider = null;
            this.isMorto = true;
        }
    }

    public boolean getIsMorto(){
        return isMorto;
    }

    // Método para limpar recursos quando não precisar mais do lobo
    public void dispose() {
        if (texturaCriatura != null) {
            texturaCriatura.dispose();
        }
    }
}