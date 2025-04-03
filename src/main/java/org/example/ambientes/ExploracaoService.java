package org.example.ambientes;

import org.example.domain.Evento;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.eventos.EventoCriatura;
import org.example.utilitarios.Utilitario;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ExploracaoService {

    public static void explorar(Personagem jogador, List<Item> recursosDisponiveis, List<Evento> eventos, Double dificuldadeExploracao) {
        if (dificuldadeExploracao < 5 && jogador.getInventario().temEspaco()) {
            // Encontrou monstro? Se não, passa a procurar itens
            if (Utilitario.getValorAleatorio() < dificuldadeExploracao) {

                // Filtra apenas os eventos de criatura
                List<EventoCriatura> eventosCriatura = eventos.stream()
                        .filter(evento -> evento instanceof EventoCriatura)
                        .map(evento -> (EventoCriatura) evento)
                        .collect(Collectors.toList());

                // Verifica se ocorre um evento de criatura
                for (EventoCriatura evento : eventosCriatura) {
                    if (Utilitario.getValorAleatorio() < evento.getProbabilidadeOcorrencia()) {
                        System.out.printf("Você encontrou um(a) %s! Prepare-se para a batalha!%n", evento.getCriatura().getNome());
                        evento.getCriatura().ataque(jogador);
                        return; // Encerra a exploração se encontrou uma criatura
                    }
                }

                if (jogador.getVida() > 0) {
                    boolean encontrouItem = false;

                    for (Item recurso : recursosDisponiveis) {
                        if (Utilitario.getValorAleatorio() < recurso.getProbabilidadeDeEncontrar()) {
                            if (recurso.getNomeItem().equals("Cogumelo") && Utilitario.getValorAleatorio() < 0.2) {
                                System.out.println("Você coletou um cogumelo, porém ele está envenenado!");
                                jogador.diminuirVida(15.0);
                                jogador.diminuirSanidade(5.0);
                                jogador.diminuirEnergia(15.0);
                                encontrouItem = true;
                                break;
                            }

                            recurso.alterarPersonagem(jogador);
                            jogador.getInventario().adicionarItem(recurso);
                            System.out.printf("Você coletou um(a) %s%n", recurso.getNomeItem());
                            encontrouItem = true;
                        }
                    }

                    if (!encontrouItem) {
                        System.out.println("Nenhum item encontrado.");
                    }
                }
            }
        }

    }
}