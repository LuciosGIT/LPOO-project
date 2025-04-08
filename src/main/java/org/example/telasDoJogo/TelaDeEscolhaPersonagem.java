package org.example.telasDoJogo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.domain.Personagem;


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

        int larguraImagem = 350;
        int alturaImagem = 300;
        int espacamento = 50;

        String[] nomesPersonagens = {"Sobrevivente", "Mecânico", "Médico"};


        backgroundTexture = new Texture("imagens/backgrounds/backgroundTelaDeEscolhaPersonagem.png");

        texturasDosPersonagens = new Texture[]{
                new Texture("imagens/imagemPersonagemEscolhaSobrevivente.png"),
                new Texture("imagens/imagemPersonagemEscolhaMecanico.png"),
                new Texture("imagens/imagemPersonagemEscolhaMedico.png")
        };

        Table botoesTable = new Table();

        for(int i = 0; i < texturasDosPersonagens.length; i++){
            TextureRegionDrawable drawable = new TextureRegionDrawable(texturasDosPersonagens[i]);
            ImageButton button = new ImageButton(drawable);

            button.setSize(larguraImagem, alturaImagem);

            int finalI = i;

            button.addListener(new ClickListener(){
                public void clicked(InputEvent event, float x, float y) {
                    personagemSelecionado(nomesPersonagens[finalI]);
                }
            });

            if(i > 0){
                botoesTable.add(button).width(larguraImagem).height(alturaImagem).padLeft(espacamento);

            }
            else{
                botoesTable.add(button).width(larguraImagem).height(alturaImagem);

            }

        }

        table.add(botoesTable).center();
        stage.addActor(table);

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

    private void personagemSelecionado(String nomesPersonagen) {
        System.out.println(nomesPersonagen);
    }

}
