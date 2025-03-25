package org.example.ambientes;

import org.example.domain.Item;
import org.example.domain.Personagem;

import java.util.List;
import java.util.Random;

public class ExploracaoService {

        public static void explorar(Personagem jogador, List<Item> recursosDisponiveis, Double dificuldadeExploracao) {
            if (dificuldadeExploracao < 5 && jogador.getInventario().temEspaco()) {
                // Encontrou monstro? Se não, passa a procurar itens
                if (getValorAleatorio() < dificuldadeExploracao) {
                    System.out.println("Você encontrou um monstro! Prepare-se para a batalha!");
                    return; // Encerrar a exploração em caso de batalha
                }

                if (jogador.getVida() > 0) {
                    boolean encontrouItem = false;

                    for (Item recurso : recursosDisponiveis) {
                        if (getValorAleatorio() < recurso.getProbabilidadeDeEncontrar()) {
                            if (recurso.getNomeItem().equals("Cogumelo") && getValorAleatorio() < 0.2) {
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

        private static double getValorAleatorio() {
            Random random = new Random();
            return random.nextDouble();
        }
    }

