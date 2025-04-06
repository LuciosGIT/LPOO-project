package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.gerenciadores.GerenciadorDeAmbientes;
import org.example.personagens.Rastreador;
import org.example.utilitarios.Utilitario;

import java.util.List;
import java.util.Random;

public class ExploracaoService {

    public static void explorar(Personagem jogador, Ambiente ambiente) {
        // Verificar se é viável explorar
        if (ambiente.getDificuldadeExploracao() >= 5 || !jogador.getInventario().temEspaco()) {
            System.out.println("Exploração impossível: dificuldade alta ou inventário cheio.");
            return;
        }

        // Gerar um evento no ambiente
        ambiente.gerarEvento(jogador);

        // Caso o jogador ainda esteja vivo, buscar itens no ambiente
        if (jogador.getVida() > 0) {
            encontrarItens(jogador, ambiente.getRecursosDisponiveis());
        }
    }


    private static void encontrarItens(Personagem jogador, List<Item> recursosDisponiveis) {

        if (jogador instanceof Rastreador) {
            jogador.habilidade();
        }
        boolean encontrouItem = false;

        // Percorrer pelos recursos disponíveis
        for (Item recurso : recursosDisponiveis) {
            if (Utilitario.getValorAleatorio() < recurso.getProbabilidadeDeEncontrar()) {
                if (isCogumeloEnvenenado(recurso)) {
                    aplicarEfeitoCogumeloEnvenenado(jogador);
                    GerenciadorDeAmbientes.modificarRecursos(jogador.getLocalizacao(), recurso);
                } else {
                    recurso.alterarPersonagem(jogador);
                    jogador.getInventario().adicionarItem(recurso);
                    System.out.printf("Você coletou um(a) %s%n", recurso.getNomeItem());

                    // Remover o recurso do ambiente
                    GerenciadorDeAmbientes.modificarRecursos(jogador.getLocalizacao(), recurso);
                }
                encontrouItem = true;
            }
        }

        if (!encontrouItem) {
            System.out.println("Nenhum item encontrado.");
        }
    }

    private static boolean isCogumeloEnvenenado(Item recurso) {
        // Identificar cogumelos envenenados
        return recurso.getNomeItem().equals("Cogumelo") && Utilitario.getValorAleatorio() < 0.2;
    }

    private static void aplicarEfeitoCogumeloEnvenenado(Personagem jogador) {
        // Aplica penalidades ao jogador caso encontre um cogumelo envenenado
        System.out.println("Você coletou um cogumelo, porém ele está envenenado!");
        jogador.diminuirVida(15.0);
        jogador.diminuirSanidade(5.0);
        jogador.diminuirEnergia(15.0);
    }
}