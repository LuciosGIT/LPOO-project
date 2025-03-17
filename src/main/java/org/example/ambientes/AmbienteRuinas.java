package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;

import java.util.List;
import java.util.Random;

public class AmbienteRuinas extends Ambiente {

    //construtor
    public AmbienteRuinas(String nome, String descricao, Double dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas, List<Item> recursosDisponiveis){
        super(nome, descricao, dificuldadeExploracao, probabilidadeEventos, condicoesClimaticas);
    }

    //métodos getters

    //métodos que envolvem o ambiente

    @Override
    public void explorar(Personagem jogador)
    {

        //metodo para encontrar itens ou enfrentar monstros dependendo de probabilidade


        setProbabilidades();

        if (getDificuldadeExploracao() < 5 && jogador.getInventario().temEspaco())
        {
            Random random = new Random();

            //encontrou monstro? se não o personagem passa a procurar itens
            if(random.nextDouble() < getDificuldadeExploracao())
            {
                //evento de luta??
            }
            if (jogador.getVida() > 0)
            {
                boolean encontrouItem = false; // Flag para verificar se coletou algum item

                for(Item recursoDisponivel  : this.getRecursosDisponiveis())
                {

                    double numeroAleatorio = random.nextDouble();
                    if((numeroAleatorio < recursoDisponivel.getProbabilidadeDeEncontrar()))
                    {

                        if (recursoDisponivel.getNomeItem().equals("Cogumelo"))
                        {
                            if (numeroAleatorio < 0.2)
                            {
                                System.out.println("Você coletou um cogumelo, porém ele está envenenado!");
                                jogador.diminuirVida(15.0);
                                jogador.diminuirSanidade(5.0);
                                jogador.diminuirEnergia(15.0);
                                encontrouItem = true;
                                break;
                            }

                            recursoDisponivel.alterarPersonagem(jogador);
                            jogador.getInventario().adicionarItem(recursoDisponivel);
                            System.out.printf("Você coletou um(a) %s%n", recursoDisponivel.getNomeItem());
                            encontrouItem = true;

                        }
                        else
                        {
                            recursoDisponivel.alterarPersonagem(jogador);
                            jogador.getInventario().adicionarItem(recursoDisponivel);
                            System.out.printf("Você coletou um(a) %s%n", recursoDisponivel.getNomeItem());
                            encontrouItem = true;
                        }
                    }

                }

                if (!encontrouItem)
                {
                    System.out.print("Nenhum item encontrado");
                }



            }
        }

        //ao explorar gasta 4 de energia, 1 de fome e 1 de água
        //dificuldade é dada em porcentagem?
        //melhorar isso
        jogador.diminuirEnergia(4.0*getDificuldadeExploracao());
        jogador.diminuirSede(1.0*getDificuldadeExploracao());
        jogador.diminuirFome(1.0*getDificuldadeExploracao());

    }

    @Override
    public void gerarEvento(){
        //metodo para gerar eventos aleatorias
    }

    @Override
    public void modificarClima(){
        //metodo para modificar o clima
    }

}
