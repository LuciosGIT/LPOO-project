package org.example.personagens;

import org.example.construcao.BancoDeArmas;
import org.example.construcao.BancoDeReceitas;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoMaterial;
import org.example.itens.Armas;
import org.example.itens.Ferramentas;
import org.example.itens.Materiais;

import javax.naming.NameNotFoundException;
import java.util.List;
import java.util.Optional;


public class Mecanico extends Personagem {

    public Mecanico(String nome) {
        this.alterarNomePersonagem(nome);
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