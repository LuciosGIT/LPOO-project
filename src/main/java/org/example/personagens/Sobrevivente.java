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

    private double modificadorFome = 1.0;
    private double modificadorSede = 1.0;

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

        this.getInventario().adicionarItem(new Ferramentas("Chave Real", this, 0.8, 100.0, 0.5, 0.8, TipoFerramenta.CHAVE_ESPECIAL));

        this.alterarNomePersonagem(nome);

        sprites = new HashMap<>( Map.of(
                "parado", new TextureRegion(new Texture("imagens/sprites/sobrevivente.png")),
                "direita", new TextureRegion(new Texture("imagens/sprites/sobrevivente.png")),
                "esquerda", new TextureRegion(new Texture("imagens/sprites/sobrevivente.png")),
                "baixo", new TextureRegion(new Texture("imagens/sprites/sobrevivente.png")),
                "cima", new TextureRegion(new Texture("imagens/sprites/sobrevivente.png"))
        ));


    }

    @Override
    public HashMap<String, TextureRegion> getSprites() {
        return sprites;
    }

    @Override
    public void diminuirFome(Double fome) {
        if (fome <= 0) {
            throw new IllegalArgumentException("A fome deve ser diminuída, portanto só pode admitir valores maiores que 0!");
        }
        setFome(getFome() - (fome * modificadorFome));
    }

    @Override
    public void habilidade() {
        this.modificadorFome = 0.9;
        this.modificadorSede = 0.9;
    }
}
