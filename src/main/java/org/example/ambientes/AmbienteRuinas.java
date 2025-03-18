package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoAlimento;
import org.example.enums.TipoMaterial;
import org.example.itens.Alimentos;
import org.example.itens.Materiais;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

public class AmbienteRuinas extends Ambiente {

    Boolean estruturasInstaveis;

    //construtor
    public AmbienteRuinas(String nome, String descricao, Double dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas, Boolean estruturasInstaveis){
        super(nome, descricao, dificuldadeExploracao, probabilidadeEventos, condicoesClimaticas);
        this.getRecursosDisponiveis().add(new Alimentos("Carne", null, 1.0, 8.0, 0.5, TipoAlimento.CARNE, OffsetDateTime.now().plusDays(10)));
        this.getRecursosDisponiveis().add(new Alimentos("Raíz", null, 0.3, 12.0, 0.3, TipoAlimento.RAIZES, OffsetDateTime.now().plusDays(12)));
        this.getRecursosDisponiveis().add(new Materiais("Pedra", null, 8.0, 20.0, 0.9, 10.0, TipoMaterial.PEDRA));
        this.getRecursosDisponiveis().add(new Materiais("Madeira", null, 2.0, 20.0, 0.8, 5.0, TipoMaterial.MADEIRA));
        this.estruturasInstaveis = estruturasInstaveis;
    }

    //métodos getters

    //métodos que envolvem o ambiente

    @Override
    public void explorar(Personagem jogador){

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
        if (this.estruturasInstaveis)
        {
            System.out.println("A pouca luminosidade dificulta a exploração. Você perde mais energia.");
            jogador.diminuirEnergia(4.0*getDificuldadeExploracao()*1.25);
            jogador.diminuirSede(1.0*getDificuldadeExploracao()*1.25);
            jogador.diminuirFome(1.0*getDificuldadeExploracao()*1.25);
        }
        else
        {
            System.out.println("A exploração é relativamente tranquila.");
            jogador.diminuirEnergia(4.0*getDificuldadeExploracao());
            jogador.diminuirSede(1.0*getDificuldadeExploracao());
            jogador.diminuirFome(1.0*getDificuldadeExploracao());
        }

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
