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
        this.getRecursosDisponiveis().add(new Alimentos(OffsetDateTime.now().plusDays(15), TipoAlimento.FRUTA));
        this.getRecursosDisponiveis().add(new Alimentos(OffsetDateTime.now().plusDays(10), TipoAlimento.CARNE));
        this.getRecursosDisponiveis().add(new Alimentos(OffsetDateTime.now().minusDays(15), TipoAlimento.FRUTA));
        this.getRecursosDisponiveis().add(new Alimentos(OffsetDateTime.now().minusDays(15), TipoAlimento.CARNE));
        this.getRecursosDisponiveis().add(new Alimentos(OffsetDateTime.now().plusDays(12), TipoAlimento.RAIZES));
        this.getRecursosDisponiveis().add(new Alimentos(OffsetDateTime.now().plusDays(5), TipoAlimento.COGUMELO));
        this.getRecursosDisponiveis().add(new Materiais(5.0, TipoMaterial.MADEIRA));

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
            Random envenenado = new Random();
            boolean estaEnvenenado = envenenado.nextBoolean();
            TipoAlimento tipoSorteado = TipoAlimento.sortearItem();

            switch(tipoSorteado) {
                case FRUTA -> {
                    System.out.println("Você coletou uma fruta!");
                    jogador.getInventario().adicionarItem(getRecursosDisponiveis().get(0));

                }
                case CARNE -> {
                    System.out.println("Você encontrou uma carne de animal!");
                    jogador.getInventario().adicionarItem(getRecursosDisponiveis().get(1));

                }
                case RAIZES -> {
                    System.out.println("Você encontrou uma raíz!");
                    jogador.getInventario().adicionarItem(getRecursosDisponiveis().get(2));

                }
                case COGUMELO -> {
                    if (estaEnvenenado) {
                        System.out.println("Você encontrou uma cogumelo, porém ele está envenenado!");
                    }
                    else {
                        System.out.println("Você encontrou um cogumelo!");
                        jogador.getInventario().adicionarItem(getRecursosDisponiveis().get(3));
                    }
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
