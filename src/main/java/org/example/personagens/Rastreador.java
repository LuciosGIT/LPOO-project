package org.example.personagens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.domain.Item;
import org.example.domain.Personagem;

import java.util.HashMap;

public class Rastreador extends Personagem {

    private HashMap<String, TextureRegion> sprites;

    public Rastreador() {
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
