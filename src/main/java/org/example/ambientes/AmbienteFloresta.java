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
import org.example.itens.Alimentos;
import org.example.itens.Ferramentas;
import org.example.itens.Materiais;
import org.example.personagens.Rastreador;
import org.example.personagens.Sobrevivente;
import org.example.utilitarios.ConfiguracaoDoMundo;
import org.example.utilitarios.Utilitario;

import java.time.OffsetDateTime;


import java.util.List;
import java.util.Random;

public class AmbienteFloresta extends Ambiente {

    private boolean vegetacaoDensa;

    private boolean faunaAbundante;

    private boolean climaUmido;

    //construtor
    public AmbienteFloresta(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas, boolean densidadeVegetacao, boolean faunaAbundante, boolean climaUmido, Personagem player) {
        super(nome,descricao,dificuldadeExploracao,condicoesClimaticas);

        this.vegetacaoDensa = densidadeVegetacao;
        this.faunaAbundante = faunaAbundante;
        this.climaUmido = climaUmido;

        this.adicionarRecurso(new Alimentos("Fruta", player, 0.5, 10.0, 0.4, TipoAlimento.FRUTA, OffsetDateTime.now().plusDays(15)));
        this.adicionarRecurso(new Alimentos("Carne", player, 1.0, 8.0, 0.5, TipoAlimento.CARNE, OffsetDateTime.now().minusDays(10)));
        this.adicionarRecurso(new Alimentos("Raíz", player, 0.3, 12.0, 0.6, TipoAlimento.RAIZES, OffsetDateTime.now().plusDays(12)));
        this.adicionarRecurso(new Alimentos("Cogumelo", player, 0.2, 5.0, 0.5, TipoAlimento.COGUMELO, OffsetDateTime.now().plusDays(5)));
        this.adicionarRecurso(new Materiais("Madeira", player, 2.0, 20.0, 0.8, 5.0, TipoMaterial.MADEIRA));
        this.adicionarRecurso(new Ferramentas("Chave Real", player, 0.8, 100.0, 0.4, 0.8, TipoFerramenta.CHAVE_ESPECIAL));

        this.adicionarEvento(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.7,
                ConfiguracaoDoMundo.getCriaturasPadrao().get(2), ConfiguracaoDoMundo.getCriaturasPadrao().get(2).getNivelDePerigo()));

        this.adicionarEvento(new EventoDescoberta(true, "Descoberta", "Evento de Descoberta", 0.7,
                "Você pode encontrar Sobrevivente", TipoDescoberta.ABRIGO,
                List.of(new Materiais("Madeira", player, 2.0, 20.0, 0.8, 5.0, TipoMaterial.MADEIRA))));

        this.adicionarEvento(new EventoClimatico(true, "Evento de Chuva acionado", "Afeta a visibilidade",
                "Evento de Chuva", 0.5, TipoClimatico.TEMPESTADE, 5,
                "A chuva reduz a visibilidade e a temperatura, dificultando a exploração e aumentando o consumo de energia."));

        this.adicionarEvento(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.5,
                ConfiguracaoDoMundo.getCriaturasPadrao().get(5), ConfiguracaoDoMundo.getCriaturasPadrao().get(5).getNivelDePerigo()));

    }

    public AmbienteFloresta() { }

    //métodos getters

    //métodos que envolvem o ambiente
    @Override
    public void explorar(Personagem jogador) {

        System.out.println(jogador.getFome());

        // ExploracaoService.explorar(jogador, this);

        if (this.vegetacaoDensa) {
            System.out.println("A vegetação densa dificulta a exploração. Você perde mais energia.");
                jogador.diminuirEnergia(5.0*getDificuldadeExploracao()*0.25);
                jogador.diminuirSede(4.0*getDificuldadeExploracao()*0.25);
                jogador.diminuirFome(4.0*getDificuldadeExploracao()*0.25);
        }
        else {
            System.out.println("A exploração é relativamente tranquila.");
            jogador.diminuirEnergia(4.0 * getDificuldadeExploracao());
            jogador.diminuirSede(3.0 * getDificuldadeExploracao());
            jogador.diminuirFome(3.0 * getDificuldadeExploracao());
        }

    }


    @Override
    public Evento gerarEvento(Personagem jogador){
        Evento eventoSorteado = GerenciadorDeEventos.sortearEvento(this);
        GerenciadorDeEventos.aplicarEvento(jogador, eventoSorteado);
        return eventoSorteado;
    }


}