package org.example.personagens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoFerramenta;
import org.example.enums.TipoMaterial;
import org.example.itens.Ferramentas;
import org.example.itens.Materiais;

import java.util.HashMap;

public class Rastreador extends Personagem {

    private HashMap<String, TextureRegion> sprites;

    public Rastreador(String nome) {
        this.getInventario().adicionarItem(new Materiais("Granito Perfurante",
                this,
                7.0,
                90.0,
                0.4,
                15.0,
                TipoMaterial.PEDRA));

        this.getInventario().adicionarItem(new Ferramentas("Lanterna Solar",
                this,
                1.5,
                75.0,
                0.5,
                55.0,
                TipoFerramenta.LANTERNA));

        this.alterarNomePersonagem(nome);
        //to do: criar o hashmap passando os endereçoes das texturas
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
