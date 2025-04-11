package org.example.personagens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.domain.Personagem;

import java.util.HashMap;

public class Medico extends Personagem {

    private HashMap<String, TextureRegion> sprites;

    public Medico(String nome) {
        this.alterarNomePersonagem(nome);
        //to do: criar o hashmap passando os endereçoes das texturas
    }

    @Override
    public HashMap<String, TextureRegion> getSprites() {
        return sprites;
    }

    @Override
    public void habilidade() {
    // to do
    }
}
