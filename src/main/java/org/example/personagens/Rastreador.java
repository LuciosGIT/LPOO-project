package org.example.personagens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoAlimento;
import org.example.enums.TipoFerramenta;
import org.example.enums.TipoMaterial;
import org.example.itens.Alimentos;
import org.example.itens.Ferramentas;
import org.example.itens.Materiais;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

public class Rastreador extends Personagem {

    private HashMap<String, TextureRegion> sprites;

    public Rastreador(String nome) {
        super(nome, 5.0);
        this.getInventario().adicionarItem(new Materiais("Granito Perfurante",
                this,
                7.0,
                90.0,
                0.4,
                15.0,
                TipoMaterial.PEDRA));
        this.getInventario().adicionarItem(new Alimentos("carne",
                this,
                7.0,
                90.0,
                0.4,
                TipoAlimento.CARNE,
                OffsetDateTime.now().plusDays(5)));

        this.getInventario().adicionarItem(new Ferramentas("Lanterna Solar",
                this,
                1.5,
                75.0,
                0.5,
                55.0,
                TipoFerramenta.LANTERNA));

        this.getInventario().adicionarItem(new Alimentos("Fruta",
                this,
                1.5,
                75.0,
                0.5,
                TipoAlimento.FRUTA,
                OffsetDateTime.now().minusDays(5)));

        this.alterarNomePersonagem(nome);
        //to do: criar o hashmap passando os endereçoes das texturas

        sprites = new HashMap<>( Map.of(
                "parado", new TextureRegion(new Texture("imagens/sprites/rastreador.png")),
                "direita", new TextureRegion(new Texture("imagens/sprites/rastreador.png")),
                "esquerda", new TextureRegion(new Texture("imagens/sprites/rastreador.png")),
                "baixo", new TextureRegion(new Texture("imagens/sprites/rastreador.png")),
                "cima", new TextureRegion(new Texture("imagens/sprites/rastreador.png"))
        ));

    }

    @Override
    public HashMap<String, TextureRegion> getSprites() {
        return sprites;
    }

    @Override
    public void habilidade() {
            for (Item recurso : this.getLocalizacao().getRecursosDisponiveis()) {
                double probabilidadeAtual = recurso.getProbabilidadeDeEncontrar();
                double novaProbabilidade = Math.min(probabilidadeAtual * 1.3, 0.95);
                recurso.setProbabilidadeDeEncontrar(novaProbabilidade);
            }
        }
}
