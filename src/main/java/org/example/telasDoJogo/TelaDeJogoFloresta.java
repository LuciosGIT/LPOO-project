package org.example.telasDoJogo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import org.example.domain.Personagem;

public class TelaDeJogoFloresta implements Screen {

    private Game game;
    private Personagem player;

    public TelaDeJogoFloresta(Game game, Personagem player){
        // Inicializa a tela de jogo com o personagem
        this.game = game;
        this.player = player;
    }

    @Override
    public void show() {
        // Implement show logic here
    }

    @Override
    public void resize(int width, int height) {
        // Implement resize logic here
    }

    @Override
    public void render(float delta) {
        // Implement render logic here
    }

    @Override
    public void pause() {
        // Implement pause logic here
    }

    @Override
    public void resume() {
        // Implement resume logic here
    }

    @Override
    public void hide() {
        // Implement hide logic here
    }

    @Override
    public void dispose() {
        // Implement dispose logic here
    }

}


