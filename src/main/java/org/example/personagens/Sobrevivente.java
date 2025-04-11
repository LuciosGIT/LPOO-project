package org.example.personagens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.domain.Personagem;

import java.util.HashMap;

public class Sobrevivente extends Personagem {

    private HashMap<String, TextureRegion> sprites;

    public Sobrevivente(String nome) {
        this.alterarNomePersonagem(nome);
        //to do: criar o hashmap passando os endereçoes das texturas
    }

    @Override
    public HashMap<String, TextureRegion> getSprites() {
        return sprites;
    }

    @Override
    public void habilidade() {
        this.modificadorFome = 0.7;
        this.modificadorSede = 0.7;
    }
}
