package org.example.criatura;

import org.example.domain.Criatura;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.itens.Alimentos;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Lobo extends Criatura {

    public Lobo(String nome, int vida, int nivelDePerigo, Double danoDeAtaque) {
        super(nome, vida, nivelDePerigo, danoDeAtaque);
    }

    @Override
    public void ataque(Personagem jogador) {
        Random random = new Random();
        jogador.diminuirVida(this.getDanoDeAtaque());

        if (random.nextBoolean()) {
            List<Item> itens = jogador.getInventario().getListaDeItems();// ['Laranja', 'Madeira', 'Cogumelo']
            Optional<Item> alimentoParaRemover = itens.stream()
                    .filter(item -> item instanceof Alimentos)
                    .findFirst();

            if (alimentoParaRemover.isPresent()) {
                System.out.println("Deu azar! O lobo roubou uma comida do seu inventário!");
                itens.remove(alimentoParaRemover.get());
            }
        }
    }
}
