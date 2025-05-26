package org.example.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.example.Ui.Inventory;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoArma;
import org.example.enums.TipoFerramenta;
import org.example.itens.Armas;
import org.example.itens.Ferramentas;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class actorEstatua extends Actor implements Collidable {

    private Texture texturaEstatua;
    private Polygon collider;
    private boolean interagido = false;
    private Personagem player;
    private Inventory inventory;

    // Tipos de estátuas disponíveis
    private static final String[] TIPOS_ESTATUAS = {"Guerreiro", "Sabio", "Guardiao", "Rei"};

    // Pesos de probabilidade para cada tipo (Rei tem chance aumentada para 15%)
    private static final double[] PESOS_ESTATUAS = {0.30, 0.30, 0.25, 0.15}; // 15% de chance para Rei

    private final String tipoEstatua;

    // Mapa para armazenar as texturas de cada tipo de estátua
    private static Map<String, Texture> texturasEstatuas = new HashMap<>();
    private static Texture texturaGenerica = null;
    private static boolean texturasInicializadas = false;

    private float tempoAnimacao = 0;
    private boolean brilhando;
    private static final Random random = new Random();

    public actorEstatua(float x, float y, Personagem player, Inventory inventory) {
        // Inicializa as texturas se ainda não foram inicializadas
        if (!texturasInicializadas) {
            inicializarTexturas();
        }

        // Seleciona um tipo de estátua com base nos pesos de probabilidade
        this.tipoEstatua = selecionarTipoEstatuaPonderado();

        // Log para debug
        Gdx.app.log("actorEstatua", "Criando estátua do tipo: " + tipoEstatua);

        // Carrega a textura específica para o tipo de estátua
        this.texturaEstatua = texturasEstatuas.getOrDefault(tipoEstatua, texturaGenerica);

        // Verifica se a textura foi carregada corretamente
        if (this.texturaEstatua == null) {
            Gdx.app.error("actorEstatua", "Textura nula para o tipo: " + tipoEstatua + ". Usando textura genérica.");
            this.texturaEstatua = texturaGenerica;

            // Se mesmo a textura genérica for nula, cria uma textura vazia
            if (this.texturaEstatua == null) {
                Gdx.app.error("actorEstatua", "Textura genérica também é nula. Criando textura vazia.");
                Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
                pixmap.setColor(Color.WHITE);
                pixmap.fill();
                this.texturaEstatua = new Texture(pixmap);
                pixmap.dispose();
            }
        }

        // Log para debug
        Gdx.app.log("actorEstatua", "Textura carregada: " +
                (this.texturaEstatua == texturaGenerica ? "genérica" : "específica para " + tipoEstatua));

        setBounds(x, y, texturaEstatua.getWidth()*0.5f, texturaEstatua.getHeight()*0.5f);
        setPosition(x, y);

        setZIndex(10);
        setOrigin(getWidth()/2, getHeight()/2);

        // Cria um collider retangular na base da estátua
        float baseWidth = getWidth() * 0.6f;    // 60% da largura
        float baseHeight = getHeight() * 0.25f;  // 25% da altura
        float baseX = getWidth() * 0.2f;         // 20% da esquerda
        float baseY = 0;                         // Base da estátua

        float[] vertices = new float[] {
                baseX, baseY,
                baseX + baseWidth, baseY,
                baseX + baseWidth, baseY + baseHeight,
                baseX, baseY + baseHeight
        };

        collider = new Polygon(vertices);
        collider.setPosition(x, y);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                interagir();
            }
        });

        this.player = player;
        this.inventory = inventory;

        // Chance de a estátua começar brilhando (indicando que tem algo especial)
        // Estátuas do Rei sempre brilham para destacar sua raridade
        brilhando = "Rei".equals(tipoEstatua) || random.nextDouble() < 0.3;
    }

    /**
     * Inicializa todas as texturas necessárias
     */
    private static void inicializarTexturas() {
        Gdx.app.log("actorEstatua", "Inicializando texturas das estátuas");

        // Primeiro, tenta carregar a textura genérica como fallback
        try {
            texturaGenerica = new Texture(Gdx.files.internal("imagens/itens do cenario/estatuaGenerica.png"));
            Gdx.app.log("actorEstatua", "Textura genérica carregada com sucesso");
        } catch (Exception e) {
            Gdx.app.error("actorEstatua", "ERRO CRÍTICO: Não foi possível carregar a textura genérica: " + e.getMessage());
            // Cria uma textura vazia como último recurso
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            texturaGenerica = new Texture(pixmap);
            pixmap.dispose();
        }

        // Depois, carrega as texturas específicas para cada tipo
        for (String tipo : TIPOS_ESTATUAS) {
            try {
                String caminhoArquivo = "imagens/itens do cenario/estatua" + tipo + ".png";

                // Verifica se o arquivo existe antes de tentar carregá-lo
                if (Gdx.files.internal(caminhoArquivo).exists()) {
                    Texture textura = new Texture(Gdx.files.internal(caminhoArquivo));
                    texturasEstatuas.put(tipo, textura);
                    Gdx.app.log("actorEstatua", "Textura para " + tipo + " carregada com sucesso: " + caminhoArquivo);
                } else {
                    Gdx.app.error("actorEstatua", "Arquivo não encontrado: " + caminhoArquivo);
                    texturasEstatuas.put(tipo, texturaGenerica);
                }
            } catch (Exception e) {
                Gdx.app.error("actorEstatua", "Erro ao carregar textura para " + tipo + ": " + e.getMessage());
                texturasEstatuas.put(tipo, texturaGenerica);
            }
        }

        // Verifica se todas as texturas foram carregadas
        for (String tipo : TIPOS_ESTATUAS) {
            if (!texturasEstatuas.containsKey(tipo) || texturasEstatuas.get(tipo) == null) {
                Gdx.app.error("actorEstatua", "Textura para " + tipo + " não foi carregada. Usando genérica.");
                texturasEstatuas.put(tipo, texturaGenerica);
            }
        }

        texturasInicializadas = true;
    }

    /**
     * Seleciona um tipo de estátua com base nos pesos de probabilidade definidos
     */
    private String selecionarTipoEstatuaPonderado() {
        double valorAleatorio = random.nextDouble();
        double acumulado = 0.0;

        for (int i = 0; i < TIPOS_ESTATUAS.length; i++) {
            acumulado += PESOS_ESTATUAS[i];
            if (valorAleatorio <= acumulado) {
                return TIPOS_ESTATUAS[i];
            }
        }

        // Fallback para o primeiro tipo (não deveria chegar aqui)
        return TIPOS_ESTATUAS[0];
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Animação de brilho se aplicável
        if (brilhando) {
            tempoAnimacao += delta;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Aplica efeito de brilho se necessário
        if (brilhando) {
            // Estátua do Rei tem um brilho mais intenso e dourado
            if ("Rei".equals(tipoEstatua)) {
                float pulso = 0.7f + 0.3f * (float)Math.sin(tempoAnimacao * 3);
                batch.setColor(pulso, pulso * 0.9f, pulso * 0.7f, parentAlpha); // Tom dourado
            } else {
                float pulso = 0.7f + 0.3f * (float)Math.sin(tempoAnimacao * 3);
                batch.setColor(pulso, pulso, pulso, parentAlpha); // Brilho normal
            }
        } else if (interagido) {
            // Estátua já interagida fica um pouco mais escura
            batch.setColor(0.8f, 0.8f, 0.8f, parentAlpha);
        }

        batch.draw(texturaEstatua,
                getX(), getY(),             // posição
                getOriginX(), getOriginY(), // origem da rotação
                getWidth(), getHeight(),    // dimensões
                1, 1,                       // escala
                getRotation(),              // rotação
                0, 0,                       // região da textura
                texturaEstatua.getWidth(), texturaEstatua.getHeight(),
                false, false);

        // Restaura a cor padrão do batch
        batch.setColor(1, 1, 1, 1);
    }

    public void dispose() {
        // Não descartamos as texturas aqui, pois são compartilhadas entre instâncias
        // Elas devem ser descartadas em um método estático ou no dispose da tela
    }

    // Método estático para descartar todas as texturas quando não forem mais necessárias
    public static void disposeTextures() {
        for (Texture texture : texturasEstatuas.values()) {
            if (texture != null && texture != texturaGenerica) {
                texture.dispose();
            }
        }

        if (texturaGenerica != null) {
            texturaGenerica.dispose();
            texturaGenerica = null;
        }

        texturasEstatuas.clear();
        texturasInicializadas = false;
    }

    private void interagir() {
        if (interagido) {
            System.out.println("Esta estátua já foi examinada.");
            return;
        }

        this.clearActions();

        // Efeito visual de interação
        Action examinar = Actions.sequence(
                Actions.scaleBy(0.05f, 0.05f, 0.1f),
                Actions.scaleBy(-0.05f, -0.05f, 0.1f)
        );

        this.addAction(examinar);

        System.out.println("Examinando a estátua de " + tipoEstatua);

        try {
            // Chance de encontrar um item baseado no tipo da estátua
            double chanceItem = brilhando ? 0.8 : 0.4;

            // Estátua do Rei sempre dá um item
            if ("Rei".equals(tipoEstatua) || random.nextDouble() < chanceItem) {
                Item item = null;

                switch (tipoEstatua) {
                    case "Guerreiro":
                        item = new Armas("LANÇA ETÉREA", 25.0, player, 0.5, 100.0, 0.2, 0.5, TipoArma.CORPO);
                        break;
                    case "Sabio":
                        item = new Armas("PUNHAL DOS SUSSURROS", 35.0,player, 0.5, 100.0, 0.2, 0.5, TipoArma.CORPO);
                        break;
                    case "Guardiao":
                        item = new Ferramentas("Lanterna Guardiã", player, 0.5, 100.0, 0.2, 0.5, TipoFerramenta.LANTERNA);
                        break;
                    case "Rei":
                        // Item do Rei é mais poderoso
                        item = new Ferramentas("Chave Real", player, 0.8, 100.0, 0.5, 0.8, TipoFerramenta.CHAVE_ESPECIAL);
                        System.out.println("Você encontrou um item raro e poderoso!");
                        break;
                }

                if (item != null && player != null && player.getInventario() != null) {
                    player.getInventario().adicionarItem(item);
                    inventory.updateInventory();
                    System.out.println("Você encontrou: " + item.getNomeItem());
                }
            } else {
                System.out.println("A estátua parece ter sido saqueada há muito tempo.");
            }

            interagido = true;
            brilhando = false;

        } catch (Exception e) {
            System.err.println("Error interacting with statue: " + e.getMessage());
        }
    }

    @Override
    public Polygon getCollider() {
        return collider;
    }

    public String getTipoEstatua() {
        return tipoEstatua;
    }

    public boolean isInteragido() {
        return interagido;
    }
}