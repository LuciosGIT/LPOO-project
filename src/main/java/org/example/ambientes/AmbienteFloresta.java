package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoAlimento;
import org.example.enums.TipoMaterial;
import org.example.itens.Alimentos;
import org.example.itens.Materiais;

import java.time.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AmbienteFloresta extends Ambiente {

    private boolean vegetacaoDensa;

    private boolean faunaAbundante;

    private boolean climaUmido;

    //construtor
    public AmbienteFloresta(String nome, String descricao, Double dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas, boolean densidadeVegetacao, boolean faunaAbundante){
        super(nome,descricao,dificuldadeExploracao,probabilidadeEventos,condicoesClimaticas);
        this.vegetacaoDensa = densidadeVegetacao;
        this.faunaAbundante = faunaAbundante;
        this.getRecursosDisponiveis().add(new Alimentos("Fruta", null, 0.5, 10.0, 0.8, TipoAlimento.FRUTA, OffsetDateTime.now().plusDays(15)));
        this.getRecursosDisponiveis().add(new Alimentos("Carne", null, 1.0, 8.0, 0.6, TipoAlimento.CARNE, OffsetDateTime.now().plusDays(10)));
        this.getRecursosDisponiveis().add(new Alimentos("Raíz", null, 0.3, 12.0, 0.7, TipoAlimento.RAIZES, OffsetDateTime.now().plusDays(12)));
        this.getRecursosDisponiveis().add(new Alimentos("Cogumelo", null, 0.2, 5.0, 0.5, TipoAlimento.COGUMELO, OffsetDateTime.now().plusDays(5)));
        this.getRecursosDisponiveis().add(new Materiais("Madeira", null, 2.0, 20.0, 0.9, 5.0, TipoMaterial.MADEIRA));

    }

    //métodos getters

    //métodos que envolvem o ambiente
    @Override
    public void explorar(Personagem jogador){
        this.setProbabilidades();


        if (this.vegetacaoDensa) {

            System.out.println("A vegetação densa dificulta a exploração. Você perde mais energia.");
            jogador.diminuirEnergia(getDificuldadeExploracao() * 1.5);
        }
        else {

            System.out.println("A exploração é relativamente tranquila.");
            jogador.diminuirEnergia(getDificuldadeExploracao());

        }

        if (getDificuldadeExploracao() < 5 && jogador.getInventario().temEspaco()) {
            Random random = new Random();


            //encontrou monstro? se não o personagem passa a procurar itens
            if(random.nextDouble() < getDificuldadeExploracao()){
                //evento de luta??
            }
                if (jogador.getVida() > 0) {
                boolean encontrouItem = false; // Flag para verificar se coletou algum item

                for(Item recursoDisponivel  : this.getRecursosDisponiveis()){
                    double numeroAleatorio = random.nextDouble();
                    if((numeroAleatorio < recursoDisponivel.getProbabilidadeDeEncontrar())){

                        if (recursoDisponivel.getNomeItem().equals("Cogumelo")) {
                            if (numeroAleatorio < 0.2) {
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
                        else {
                            recursoDisponivel.alterarPersonagem(jogador);
                            jogador.getInventario().adicionarItem(recursoDisponivel);
                            System.out.printf("Você coletou um(a) %s%n", recursoDisponivel.getNomeItem());
                            encontrouItem = true;
                        }
                    }

                }
                if (!encontrouItem){
                    System.out.print("Nenhum item encontrado");
                }



        }
        }
    }


    @Override
    public void gerarEvento(){
        if (faunaAbundante) {
            //aumentar probabilidade de acontecer ataque animal
        }
        if (climaUmido) {
            // aumentar probabilidade de acontecer evento climático
        }

    }

    @Override
    public void modificarClima(){
        //metodo para modificar o clima
    }

}