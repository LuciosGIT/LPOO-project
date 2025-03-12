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

    private int densidadeVegetacao;

    private boolean faunaAbundante;

    private boolean climaUmido;

    //construtor
    public AmbienteFloresta(String nome, String descricao, int dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas, int densidadeVegetacao, boolean faunaAbundante){
        super(nome,descricao,dificuldadeExploracao,probabilidadeEventos,condicoesClimaticas);
        this.densidadeVegetacao = densidadeVegetacao;
        this.faunaAbundante = faunaAbundante;
        this.recursosDisponiveis.add(new Alimentos(TipoAlimento.FRUTA, OffsetDateTime.now().plusDays(15)));
        this.recursosDisponiveis.add(new Alimentos(TipoAlimento.CARNE, OffsetDateTime.now().plusDays(10)));
        this.recursosDisponiveis.add(new Alimentos(TipoAlimento.RAIZES, OffsetDateTime.now().plusDays(12)));
        this.recursosDisponiveis.add(new Alimentos(TipoAlimento.COGUMELO, OffsetDateTime.now().plusDays(5)));
        this.recursosDisponiveis.add(new Materiais(5.0, TipoMaterial.MADEIRA));

    }

    //métodos getters

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getDescricao(){
        return descricao;
    }

    @Override
    public int getDificuldadeExploracao(){
        return dificuldadeExploracao;
    }

    @Override
    public double getProbabilidadeEventos(){
        return  probabilidadeEventos;
    }

    //métodos que envolvem o ambiente
    @Override
    public void explorar(Personagem jogador){
        this.setProbabilidades();
        if (this.densidadeVegetacao > 5) {

            System.out.println("A vegetação densa dificulta a exploração. Você perde mais energia.");
            jogador.setEnergia(jogador.getEnergia() - dificuldadeExploracao * 1.5);
        }
        else {

            System.out.println("A exploração é relativamente tranquila.");
            jogador.setEnergia(jogador.getEnergia() - dificuldadeExploracao);

        }

        if (dificuldadeExploracao < 5 && jogador.getInventario().temEspaco()) {
            Random envenenado = new Random();
            boolean estaEnvenenado = envenenado.nextBoolean();
            TipoAlimento tipoSorteado = TipoAlimento.sortearItem();

            switch(tipoSorteado) {
                case FRUTA -> {
                    System.out.println("Você coletou uma fruta!");
                    jogador.getInventario().adicionarItem(recursosDisponiveis.get(0));

                }
                case CARNE -> {
                    System.out.println("Você encontrou uma carne de animal!");
                    jogador.getInventario().adicionarItem(recursosDisponiveis.get(1));

                }
                case RAIZES -> {
                    System.out.println("Você encontrou uma raíz!");
                    jogador.getInventario().adicionarItem(recursosDisponiveis.get(2));

                }
                case COGUMELO -> {
                    if (estaEnvenenado) {
                        System.out.println("Você encontrou uma cogumelo, porém ele está envenenado!");
                    }
                    else {
                        System.out.println("Você encontrou um cogumelo!");
                        jogador.getInventario().adicionarItem(recursosDisponiveis.get(3));
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
