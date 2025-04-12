package org.example.personagens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.domain.Personagem;
import org.example.enums.TipoFerramenta;
import org.example.enums.TipoMaterial;
import org.example.itens.Ferramentas;
import org.example.itens.Materiais;

import java.util.HashMap;
import java.util.Map;

public class Sobrevivente extends Personagem {

    private HashMap<String, TextureRegion> sprites;

    public Sobrevivente(String nome) {
        super(nome, 5.0);
        this.alterarNomePersonagem(nome);
        this.getInventario().adicionarItem(new Materiais("Galho Firme",
                this,
                7.0,
                65.0,
                0.4,
                24.0,
                TipoMaterial.MADEIRA));

        this.getInventario().adicionarItem(new Ferramentas("Machado Romano",
                this,
                1.5,
                95.0,
                0.5,
                80.0,
                TipoFerramenta.MACHADO));

        this.alterarNomePersonagem(nome);

        sprites = new HashMap<>( Map.of(
                "parado", new TextureRegion(new Texture("sprites/mecanico/parado.png")),
                "direita", new TextureRegion(new Texture("sprites/mecanico/andando.png")),
                "esquerda", new TextureRegion(new Texture("sprites/mecanico/correndo.png")),
                "baixo", new TextureRegion(new Texture("sprites/mecanico/atacando.png")),
                "cima", new TextureRegion(new Texture("sprites/mecanico/pulando.png"))
        ));

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
