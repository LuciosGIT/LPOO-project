package org.example.telasDoJogo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;



public class TelaDeEscolhaPersonagem implements Screen {

    private Batch batch;
    private Stage stage;
    private Texture backgroundTexture;

    private Texture[] texturasDosPersonagens;

    private Table table;

    @Override
    public void show() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);

        batch = new SpriteBatch();

        backgroundTexture = new Texture("imagens/backgrounds/backgroundTelaDeEscolhaPersonagem.png");

        texturasDosPersonagens = new Texture[]{
                new Texture("imagens/imagemPersonagemEscolhaSobrevivente.png"),
                new Texture("imagens/imagemPersonagemEscolhaMecanico.png"),
                new Texture("imagens/imagemPersonagemEscolhaMecanico.png")
        };
    }

    @Override
    public void resize(int width, int height) {
        // Implement resize logic here
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        int quantidadeImagens = texturasDosPersonagens.length;
        int larguraImagem = 350; // Tamanho fixo para todas as imagens
        int alturaImagem = 300;
        int espacamento = 50; // Espaço fixo entre as imagens

        // Calcular largura total ocupada por todas as imagens e espaços
        int larguraTotal = (larguraImagem * quantidadeImagens) + (espacamento * (quantidadeImagens - 1));

        // Posição inicial X para centralizar todo o conjunto
        int posicaoXInicial = (Gdx.graphics.getWidth() - larguraTotal) / 2;

        // Posição Y (centralizado verticalmente)
        int posicaoY = (Gdx.graphics.getHeight() - alturaImagem) / 2;

        // Desenhar cada personagem na posição calculada
        for (int i = 0; i < quantidadeImagens; i++) {
            int posicaoX = posicaoXInicial + (i * (larguraImagem + espacamento));
            batch.draw(texturasDosPersonagens[i], posicaoX, posicaoY, larguraImagem, alturaImagem);
        }

        batch.end();


        stage.act(delta);
        stage.draw();

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
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();
        for(Texture texture : texturasDosPersonagens){
            texture.dispose();
        }
    }

}
