package org.example.personagens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.construcao.BancoDeArmas;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoArma;
import org.example.enums.TipoFerramenta;
import org.example.enums.TipoMaterial;
import org.example.itens.Armas;
import org.example.itens.Ferramentas;
import org.example.itens.Materiais;
import org.example.itens.Remedios;
import org.example.enums.TipoRemedio;

import javax.naming.NameNotFoundException;
import java.util.*;
import java.util.HashMap;


public class Mecanico extends Personagem {

    private HashMap<String, TextureRegion> sprites = new HashMap<>();

    public Mecanico(String nome) {
        super(nome, 10.0);

        this.getInventario().adicionarItem(new Materiais("Placa Metálica",
                this,
                2.5,
                70.0,
                0.3,
                15.0,
                TipoMaterial.MADEIRA));

        this.getInventario().adicionarItem(new Materiais(
                "Pedra",
                this,
                2.5,
                70.0,
                0.3,
                15.0,
                TipoMaterial.PEDRA));

        this.getInventario().adicionarItem(new Materiais("Madeira",
                this,
                2.5,
                70.0,
                0.3,
                15.0,
                TipoMaterial.MADEIRA));

        this.getInventario().adicionarItem(
                new Armas("MACHADO", 20.0,this,0.1,0.5,12.0,0.1, TipoArma.CORPO)
        );

        sprites = new HashMap<>( Map.of(
                "parado", new TextureRegion(new Texture("imagens/sprites/mecanicoImagemTemporaria.png")),
                "direita", new TextureRegion(new Texture("imagens/sprites/mecanicoImagemTemporaria.png")),
                "esquerda", new TextureRegion(new Texture("imagens/sprites/mecanicoImagemTemporaria.png")),
                "baixo", new TextureRegion(new Texture("imagens/sprites/mecanicoImagemTemporaria.png")),
                "cima", new TextureRegion(new Texture("imagens/sprites/mecanicoImagemTemporaria.png"))
        ));



        //to do: criar o hashmap passando os endereçoes das texturas
    }

    public HashMap<String, TextureRegion> getSprites(){
        return sprites;
    }

    @Override
    public void habilidade() {
        System.out.println("Você é um mecanico, suas habilidades são: Construir Armas e Consertar Ferramentas. Aproveite!");
    }

    public void criarNovaArma(TipoMaterial material1, TipoMaterial material2) throws NameNotFoundException {

        List<Item> inventario = this.getInventario().getListaDeItems();

        // Busca os materiais no inventário
        Optional<Item> encontrado1 = inventario.stream()
                .filter(item -> item instanceof Materiais m && m.getTipoMaterial() == material1)
                .findFirst();

        Optional<Item> encontrado2 = inventario.stream()
                .filter(item -> item instanceof Materiais m && m.getTipoMaterial() == material2 && !item.equals(encontrado1.orElse(null)))
                .findFirst();

        if (encontrado1.isPresent() && encontrado2.isPresent()) {
            Armas arma = BancoDeArmas.buscarArma(material1, material2, this);

            if (arma != null) {
                System.out.println("Criando arma: " + arma.getNomeItem());
                this.getInventario().adicionarItem(arma);
                this.getInventario().removerItem(encontrado1.get().getNomeItem());
                this.getInventario().removerItem(encontrado2.get().getNomeItem());

            } else {
                System.out.println("Nenhuma arma pode ser criada com essa combinação de materiais.");
            }
        }
        else {
            System.out.println("Você não possui os materiais necessários!");
        }
    }

    public void consertarFerramenta(Ferramentas ferramenta) {

        if (ferramenta.getDurabilidade() < 20.0) {
            ferramenta.setDurabilidade(100.0);
        } else {
            System.out.println("Sua ferramenta ainda está em bom estado");
        }

    }
}