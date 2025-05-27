package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.gerenciadores.GerenciadorDeAmbientes;
import org.example.personagens.Rastreador;
import org.example.utilitarios.Utilitario;

import java.util.List;

public class ExploracaoService {

    public static void explorar(Personagem jogador, Ambiente ambiente) {
        try {
            // Caso o jogador ainda esteja vivo, buscar itens no ambiente
            if (jogador.getVida() > 0 && jogador.getInventario().temEspaco()) {
                encontrarItens(jogador, ambiente.getRecursosDisponiveis());
            } else {
                System.out.println("Você não tem espaço no inventário!");
            }
        }
        catch (Exception e) {
            System.out.println("Erro ao explorar o ambiente: " + e.getMessage());
        }
    }


    private static void encontrarItens(Personagem jogador, List<Item> recursosDisponiveis) {

        if (jogador instanceof Rastreador) {
            jogador.habilidade(); //caso o jogador seja um Rastreador, ele pode usar sua habilidade especial
        }
        boolean encontrouItem = false;

        // Percorrer pelos recursos disponíveis
        for (Item recurso : recursosDisponiveis) {
            if (Utilitario.getValorAleatorio() < recurso.getProbabilidadeDeEncontrar()) {

                    recurso.alterarPersonagem(jogador);
                    jogador.getInventario().adicionarItem(recurso);
                    System.out.printf("Você coletou um(a) %s%n", recurso.getNomeItem());

                    // Remover o recurso do ambiente
                    GerenciadorDeAmbientes.modificarRecursos(jogador.getLocalizacao(), recurso);

                encontrouItem = true;
            }
        }

        if (!encontrouItem) {
            System.out.println("Nenhum item encontrado.");
        }
    }

}