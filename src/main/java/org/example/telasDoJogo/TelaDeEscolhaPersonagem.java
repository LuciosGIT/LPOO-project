package org.example.telasDoJogo;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.example.domain.Personagem;
import org.example.personagens.Mecanico;
import org.example.personagens.Sobrevivente;


public class TelaDeEscolhaPersonagem implements Screen {

    private Batch batch;
    private Stage stage;
    private Texture backgroundTexture;
    private Texture[] texturasDosPersonagens;
    private Texture[] texturaImagensflutuantes;
    private Actor[] atoresImagensFlutuantes;
    private Texture texturaTextoEcolhaSobrevivente;
    private Texture texturaBotaoVoltar;
    private final Game game;
    private Table table;
    private Personagem mecanico;

    public TelaDeEscolhaPersonagem(Game game) {
        this.game = game;
    }

    @Override
    public void show() {

        inicializar();
        criarImagensClicaveis();
        criarBotaos();
        criarTexto();

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
        texturaTextoEcolhaSobrevivente.dispose();
        texturaBotaoVoltar.dispose();
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

        texturaImagensflutuantes = new Texture[]{
                new Texture("imagens/textboxes/textoTelaDeEscolhaSobrevivente.png"),
                new Texture("imagens/textboxes/textoTelaDeEscolhaMecanico.png"),
                new Texture("imagens/textboxes/textoTelaDeEscolhaMedico.png"),
                new Texture("imagens/textboxes/textoTelaDeEscolhaRastreador.png")// criar para o rastreador
        };


    }

    private void textoBox() {
        atoresImagensFlutuantes = new Actor[texturaImagensflutuantes.length];

        // Nova posição na lateral superior direita
        float posX = Gdx.graphics.getWidth() * 0.8f; // 70% da largura da tela
        float posY = Gdx.graphics.getHeight() * 0.67f; // 70% da altura da tela

        for (int i = 0; i < texturaImagensflutuantes.length; i++) {
            final int index = i;

            atoresImagensFlutuantes[i] = new Actor() {
                @Override
                public void draw(Batch batch, float parentAlpha) {
                    if (isVisible()) {
                        batch.draw(texturaImagensflutuantes[index],
                                getX(), getY(),
                                getWidth(), getHeight());
                    }
                }
            };

            atoresImagensFlutuantes[i].setSize(300, 300);
            atoresImagensFlutuantes[i].setPosition(posX, posY);
            atoresImagensFlutuantes[i].setVisible(false);

            stage.addActor(atoresImagensFlutuantes[i]);
        }
    }

    private void criarImagensClicaveis(){

        textoBox();

        int larguraImagem = 350;
        int alturaImagem = 300;
        int espacamento = 5;

        String[] nomesPersonagens = {"Sobrevivente", "Mecânico", "Médico", "Rastreador"};

        texturasDosPersonagens = new Texture[]{
                new Texture("imagens/assets/TelaDeEscolhaPersonagem/imagemPersonagemEscolhaSobrevivente.png"),
                new Texture("imagens/assets/TelaDeEscolhaPersonagem/imagemPersonagemEscolhaMecanico.png"),
                new Texture("imagens/assets/TelaDeEscolhaPersonagem/imagemPersonagemEscolhaMedico.png"),
                new Texture("imagens/assets/TelaDeEscolhaPersonagem/imagemPersonagemEscolhaRastreador.png")//mudar para rastreador
        };

        Table botoesTable = new Table();

        for(int i = 0; i < texturasDosPersonagens.length; i++){
            TextureRegionDrawable drawable = new TextureRegionDrawable(texturasDosPersonagens[i]);
            ImageButton button = new ImageButton(drawable);

            button.setSize(larguraImagem, alturaImagem);

            int finalI = i;

            button.addListener(new ClickListener(){
                private float posicaoOriginal = -1;

                final float DESLOCAMENTO_Y = 20;

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    personagemSelecionado(nomesPersonagens[finalI]);
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (posicaoOriginal == -1) {
                        posicaoOriginal = button.getY();
                    }

                    button.clearActions();
                    button.addAction(Actions.moveTo(button.getX(), posicaoOriginal + DESLOCAMENTO_Y, 0.2f));

                    for (Actor imagem : atoresImagensFlutuantes) {
                        imagem.setVisible(false);
                    }

                    atoresImagensFlutuantes[finalI].setVisible(true);
                    atoresImagensFlutuantes[finalI].addAction(
                            Actions.moveTo(
                                    Gdx.graphics.getWidth() * 0.8f, // Nova posição X
                                    Gdx.graphics.getHeight() * 0.67f, // Nova posição Y
                                    0.2f
                            )
                    );
                }


                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

                    // Limpar todas as ações anteriores
                    button.clearActions();

                    // Anima de volta para a posição original
                    button.addAction(Actions.moveTo(button.getX(), posicaoOriginal, 0.2f));

                    atoresImagensFlutuantes[finalI].setVisible(false);

                    atoresImagensFlutuantes[finalI].addAction(Actions.moveTo(Gdx.graphics.getWidth()*0.9f, Gdx.graphics.getHeight() * 0.67f, 0.2f));

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

    private void personagemSelecionado(String nomePersonagem) {
        System.out.println(nomePersonagem);

        switch (nomePersonagem){
            case "Sobrevivente" ->{
                Personagem sobrevivente = new Sobrevivente("Sobrevivente");
                game.setScreen(new TelaDeJogoFloresta(game, sobrevivente));
            }
            case "Mecânico" ->{
                Personagem mecanico = new Mecanico("Mecânico");
                game.setScreen(new TelaDeJogoFloresta(game, mecanico));
            }
            case "Médico" ->{
                Personagem medico = new Mecanico("Médico");
                game.setScreen(new TelaDeJogoFloresta(game, medico));
            }
            case "Rastreador" ->{
                Personagem rastreador = new Mecanico("Rastreador");
                game.setScreen(new TelaDeJogoFloresta(game, rastreador));
            }
            default -> {
                throw new IllegalArgumentException("Personagem inválido: " + nomePersonagem);
            }
        }

    }

    private void criarBotaos(){

        texturaBotaoVoltar = new Texture("imagens/assets/TelaDeEscolhaPersonagem/botaoDeVoltar.png");

        ImageButton buttonSair = new ImageButton(new TextureRegionDrawable(texturaBotaoVoltar));

        buttonSair.setSize(Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getWidth()*0.07f);

        buttonSair.setPosition(
                (float) Gdx.graphics.getWidth()*0.05f,
                (float) Gdx.graphics.getHeight() * 0.8f
        );

        final float posicaoOriginal = (float) Gdx.graphics.getWidth()*0.05f;

        buttonSair.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TelaDeInicio(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                buttonSair.clearActions();
                buttonSair.addAction(Actions.moveTo(posicaoOriginal-5f, buttonSair.getY(), 0.2f));


            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                buttonSair.clearActions();
                buttonSair.addAction(Actions.moveTo(posicaoOriginal, buttonSair.getY(), 0.2f));
            }
        });

        stage.addActor(buttonSair);

    }

    private void criarTexto(){
        texturaTextoEcolhaSobrevivente = new Texture("imagens/textos/textoDeEscolha.png");

        Image  textoEscolhaSobrevivente = new Image(texturaTextoEcolhaSobrevivente);

        textoEscolhaSobrevivente.setSize(Gdx.graphics.getWidth()*0.3f, Gdx.graphics.getHeight()*0.2f);

        textoEscolhaSobrevivente.setPosition(
                (float) Gdx.graphics.getWidth()/2 - textoEscolhaSobrevivente.getWidth()/2,
                (float) Gdx.graphics.getHeight()/2 + 200
        );

        stage.addActor(textoEscolhaSobrevivente);


    }

}
