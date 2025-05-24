package org.example.personagens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.domain.Personagem;
import org.example.enums.TipoEfeito;
import org.example.enums.TipoFerramenta;
import org.example.enums.TipoMaterial;
import org.example.enums.TipoRemedio;
import org.example.itens.Ferramentas;
import org.example.itens.Materiais;
import org.example.itens.Remedios;

import java.util.HashMap;
import java.util.Map;

public class Medico extends Personagem {

    private Double cura;

    private HashMap<String, TextureRegion> sprites;

    public Medico(String nome) {
        super(nome, 5.0);
        this.alterarNomePersonagem(nome);
        this.cura = 15.0;

        this.getInventario().adicionarItem(new Remedios("Bandagem Auxiliar",
                this,
                7.0,
                90.0,
                0.4,
                TipoRemedio.BANDAGEM
               ));

        this.getInventario().adicionarItem(new Remedios("Soro Antibiótico",
                this,
                7.0,
                90.0,
                0.4,
                TipoRemedio.ANTIBIOTICO
        ));

        this.alterarNomePersonagem(nome);

        sprites = new HashMap<>( Map.of(
                "parado", new TextureRegion(new Texture("imagens/sprites/medico.png"))
        ));

    }

    @Override
    public HashMap<String, TextureRegion> getSprites() {
        return sprites;
    }

    @Override
    public void habilidade() {
        tratarFerimentos(this);
    }

    public void tratarFerimentos(Personagem personagem) {
        personagem.aumentarVida(this.cura);
    }
}
