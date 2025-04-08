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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Color;


public class TelaDeEscolhaPersonagem implements Screen {

    private Batch batch;
    private Stage stage;
    private Texture backgroundTexture;

    private Texture[] texturasDosPersonagens;

    private Table table;

    @Override
    public void show() {

        inicializar();
        criarImagensClicaveis();




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

    private void inicializar(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();

        table = new Table();
        table.setFillParent(true);

        backgroundTexture = new Texture("imagens/backgrounds/backgroundTelaDeEscolhaPersonagem.png");



    }

    private void criarImagensClicaveis(){

        int larguraImagem = 350;
        int alturaImagem = 300;
        int espacamento = 50;

        String[] nomesPersonagens = {"Sobrevivente", "Mecânico", "Médico"};

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
                private float posicaoOriginal = -1;

                float DESLOCAMENTO_Y = 20;

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    personagemSelecionado(nomesPersonagens[finalI]);
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (posicaoOriginal == -1) {
                        posicaoOriginal = button.getY();
                    }

                    // Para qualquer animação em andamento
                    button.clearActions();

                    // Anima para cima
                    button.addAction(Actions.moveTo(button.getX(), posicaoOriginal + DESLOCAMENTO_Y, 0.2f));



                }


                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

                    // Limpar todas as ações anteriores
                    button.clearActions();

                    // Anima de volta para a posição original
                    button.addAction(Actions.moveTo(button.getX(), posicaoOriginal, 0.2f));




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

    private void personagemSelecionado(String nomesPersonagen) {
        System.out.println(nomesPersonagen);
    }

}
