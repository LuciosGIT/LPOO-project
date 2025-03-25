package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoAlimento;
import org.example.enums.TipoMaterial;
import org.example.itens.Alimentos;
import org.example.itens.Materiais;
import org.example.personagens.Rastreador;
import org.example.personagens.Sobrevivente;

import java.time.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AmbienteFloresta extends Ambiente {

    private boolean vegetacaoDensa;

    private boolean faunaAbundante;

    private boolean climaUmido;

    //construtor
    public AmbienteFloresta(String nome, String descricao, Double dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas, boolean densidadeVegetacao, boolean faunaAbundante, boolean climaUmido){
        super(nome,descricao,dificuldadeExploracao,probabilidadeEventos,condicoesClimaticas);
        this.vegetacaoDensa = densidadeVegetacao;
        this.faunaAbundante = faunaAbundante;
        this.climaUmido = climaUmido;
        this.getRecursosDisponiveis().add(new Alimentos("Fruta", null, 0.5, 10.0, 0.4, TipoAlimento.FRUTA, OffsetDateTime.now().plusDays(15)));
        this.getRecursosDisponiveis().add(new Alimentos("Carne", null, 1.0, 8.0, 0.5, TipoAlimento.CARNE, OffsetDateTime.now().plusDays(10)));
        this.getRecursosDisponiveis().add(new Alimentos("Raíz", null, 0.3, 12.0, 0.6, TipoAlimento.RAIZES, OffsetDateTime.now().plusDays(12)));
        this.getRecursosDisponiveis().add(new Alimentos("Cogumelo", null, 0.2, 5.0, 0.5, TipoAlimento.COGUMELO, OffsetDateTime.now().plusDays(5)));
        this.getRecursosDisponiveis().add(new Materiais("Madeira", null, 2.0, 20.0, 0.8, 5.0, TipoMaterial.MADEIRA));

    }

    //métodos getters

    //métodos que envolvem o ambiente
    @Override
    public void explorar(Personagem jogador) {

        // to do : se o jogador for instancia de Rastreador : as chances de se encontrar recursos são maiores!!!
        if (jogador instanceof Rastreador) {
            Random random = new Random();
            for (Item recurso : this.getRecursosDisponiveis()) {
                recurso.setProbabilidadeDeEncontrar(random.nextDouble(recurso.getProbabilidadeDeEncontrar() + 0.1, 0.99));
            }
        }


        ExploracaoService.explorar(jogador, jogador.getInventario().getListaDeItems(), getDificuldadeExploracao());

        if (this.vegetacaoDensa) {
            System.out.println("A vegetação densa dificulta a exploração. Você perde mais energia.");
            jogador.diminuirEnergia(4.0*getDificuldadeExploracao()*1.25);
            jogador.diminuirSede(1.0*getDificuldadeExploracao()*1.25);
            jogador.diminuirFome(1.0*getDificuldadeExploracao()*1.25);
        }

        if (this.vegetacaoDensa && jogador instanceof Sobrevivente)
        {
            System.out.println("A vegetação densa dificulta a exploração. Você perde mais energia.");
            jogador.diminuirEnergia(4.0*getDificuldadeExploracao()*0.25);
            jogador.diminuirSede(1.0*getDificuldadeExploracao()*0.25);
            jogador.diminuirFome(1.0*getDificuldadeExploracao()*0.25);
        }
        else {
            System.out.println("A exploração é relativamente tranquila.");
            jogador.diminuirEnergia(4.0 * getDificuldadeExploracao());
            jogador.diminuirSede(1.0 * getDificuldadeExploracao());
            jogador.diminuirFome(1.0 * getDificuldadeExploracao());
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