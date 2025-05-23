package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.*;
import org.example.eventos.EventoClimatico;
import org.example.eventos.EventoCriatura;
import org.example.eventos.EventoDescoberta;
import org.example.gerenciadores.GerenciadorDeEventos;
import org.example.itens.Agua;
import org.example.itens.Alimentos;
import org.example.itens.Materiais;
import org.example.utilitarios.Utilitario;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

public class AmbienteMontanha extends Ambiente {

    private boolean terrenoAcidentado; // exige mais energia para exploração

    private boolean climaInstavel; // nevasca e vento ocorrem repentinamente


    //construtor
    public AmbienteMontanha(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas, boolean terrenoAcidentado, boolean climaInstavel, Personagem player) {
        super(nome, descricao, dificuldadeExploracao, condicoesClimaticas);
        this.getRecursosDisponiveis().add(new Alimentos("Carne", player, 1.0, 8.0, 0.3, TipoAlimento.CARNE, OffsetDateTime.now().plusDays(10)));
        this.getRecursosDisponiveis().add(new Alimentos("Raíz", player, 0.3, 12.0, 0.5, TipoAlimento.RAIZES, OffsetDateTime.now().plusDays(12)));
        this.getRecursosDisponiveis().add(new Materiais("Pedra Cristalina", player, 8.0, 20.0, 0.7, 10.0, TipoMaterial.PEDRA));
        this.getRecursosDisponiveis().add(new Materiais("Madeira", player, 2.0, 20.0, 0.5, 5.0, TipoMaterial.MADEIRA));
        this.getRecursosDisponiveis().add(new Agua("Água de Degelo", player, 5.0, 20.0, 0.5, Pureza.CONTAMINADA ,10.0));
        this.getEventos().add(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.7,
                getCriaturasAmbientes().get(3) , getCriaturasAmbientes().get(1).getNivelDePerigo()));
        this.getEventos().add(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.5,
                getCriaturasAmbientes().get(0), getCriaturasAmbientes().get(3).getNivelDePerigo()));
        this.getEventos().add(new EventoDescoberta(true, "Descoberta", "Evento de Descoberta", 0.7,"Você pode encontrar Morcego",
                TipoDescoberta.CAVERNA, List.of(new Materiais("Pedra", player, 2.0, 20.0, 0.8, 5.0, TipoMaterial.PEDRA))));
        this.getEventos().add(new EventoClimatico(true, "Evento de Nevasca foi acionado", "Afeta a visibilidade", "Evento de Nevasca" , 0.7, TipoClimatico.NEVASCA, 5, "A nevasca reduz a visibilidade e a temperatura, dificultando a exploração e aumentando o consumo de energia."));
        this.terrenoAcidentado = terrenoAcidentado;
        this.climaInstavel = climaInstavel;

    }

    public AmbienteMontanha() { }

    //métodos getters

    //métodos que envolvem o ambiente

    @Override
    public void explorar(Personagem jogador){

        //metodo para encontrar itens ou enfrentar monstros dependendo de probabilidade


        // ExploracaoService.explorar(jogador,this);

        //ao explorar gasta 4 de energia, 1 de fome e 1 de água
        //dificuldade é dada em porcentagem?
        //melhorar isso
        if (this.terrenoAcidentado)
        {
            System.out.println("O terreno acidentado dificulta a exploração. Você perde mais energia.");
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

        if(this.climaInstavel) {
            this.getEventos().get(1).setProbabilidadeOcorrencia(this.getEventos().get(1).getProbabilidadeOcorrencia() + 0.2);
        }

    }

    @Override
    public Evento gerarEvento(Personagem jogador){
        Evento eventoSorteado = GerenciadorDeEventos.sortearEvento(this);
        GerenciadorDeEventos.aplicarEvento(jogador, eventoSorteado);
        return eventoSorteado;
    }

    @Override
    public void modificarClima(){
        //metodo para modificar o clima
    }

}