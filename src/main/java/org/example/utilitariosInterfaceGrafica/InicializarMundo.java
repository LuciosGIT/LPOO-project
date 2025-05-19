package org.example.utilitariosInterfaceGrafica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.example.actor.actorPersonagem;

public class InicializarMundo {

    private final OrthographicCamera camera;
    private final Stage stage;
    private final SpriteBatch batch;
    private final Texture backgroundFloresta;
    private final float worldWidth;
    private final float worldHeight;
    private final float viewportWidth;
    private final float viewportHeight;


    public InicializarMundo(actorPersonagem actorPlayer, String imagemPath) {
        // 1. Configuração da câmera
        camera = new OrthographicCamera(1920f, 1080f);

        // 2. Define dimensões do mundo
        worldWidth = 1920f;
        worldHeight = 1080f;

        // 3. Configuração do viewport e stage
        stage = new Stage(new FitViewport(worldWidth, worldHeight, camera));
        Gdx.input.setInputProcessor(stage);

        // 4. Batch e texturas
        batch = new SpriteBatch();
        backgroundFloresta = new Texture(imagemPath);

        // 5. Atualiza viewport
        viewportWidth = worldWidth;
        viewportHeight = worldHeight;

        // 6. Configuração do player
        actorPlayer.setTexture("parado");
        actorPlayer.setSize(64, 64);
        actorPlayer.setPosition(
                worldWidth / 2 - actorPlayer.getWidth() / 2,
                worldHeight / 2 - actorPlayer.getHeight() / 2
        );


        // 7. Aplica zoom por último
        camera.zoom = 0.7f; // Teste com 0.5 para ver se está funcionando
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
    public Stage getStage() {
        return stage;
    }
    public SpriteBatch getBatch() {
        return batch;
    }
    public Texture getBackgroundFloresta() {
        return backgroundFloresta;
    }
    public float getWorldWidth() {
        return worldWidth;
    }
    public float getWorldHeight() {
        return worldHeight;
    }
    public float getViewportWidth() {
        return viewportWidth;
    }
    public float getViewportHeight() {
        return viewportHeight;
    }

    public void dispose() {
        if (batch != null) batch.dispose();
        if (backgroundFloresta != null) backgroundFloresta.dispose();
        if (stage != null) stage.dispose();
    }
}
