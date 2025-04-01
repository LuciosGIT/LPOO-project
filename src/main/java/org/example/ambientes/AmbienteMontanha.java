package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoAlimento;
import org.example.enums.TipoClimatico;
import org.example.enums.TipoMaterial;
import org.example.gerenciadores.GerenciadorDeEventos;
import org.example.itens.Alimentos;
import org.example.itens.Materiais;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

public class AmbienteMontanha extends Ambiente {

    Boolean terrenoAcidentado;

    //construtor
    public AmbienteMontanha(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas, Boolean terrenoAcidentado){
        super(nome, descricao, dificuldadeExploracao, condicoesClimaticas);
        this.getRecursosDisponiveis().add(new Alimentos("Carne", null, 1.0, 8.0, 0.3, TipoAlimento.CARNE, OffsetDateTime.now().plusDays(10)));
        this.getRecursosDisponiveis().add(new Alimentos("Raíz", null, 0.3, 12.0, 0.5, TipoAlimento.RAIZES, OffsetDateTime.now().plusDays(12)));
        this.getRecursosDisponiveis().add(new Materiais("Pedra", null, 8.0, 20.0, 0.7, 10.0, TipoMaterial.PEDRA));
        this.getRecursosDisponiveis().add(new Materiais("Madeira", null, 2.0, 20.0, 0.5, 5.0, TipoMaterial.MADEIRA));
        this.terrenoAcidentado = terrenoAcidentado;
    }

    //métodos getters

    //métodos que envolvem o ambiente

    @Override
    public void explorar(Personagem jogador){

        //metodo para encontrar itens ou enfrentar monstros dependendo de probabilidade




        ExploracaoService.explorar(jogador, jogador.getInventario().getListaDeItems(), getEventos(),  getDificuldadeExploracao());

        //ao explorar gasta 4 de energia, 1 de fome e 1 de água
        //dificuldade é dada em porcentagem?
        //melhorar isso
        if (this.terrenoAcidentado)
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
    public void gerarEvento(Personagem jogador){
        GerenciadorDeEventos.aplicarEvento(jogador);
    }

    @Override
    public void modificarClima(){
        //metodo para modificar o clima
    }

}
