package org.example.ambientes;


import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoAlimento;
import org.example.enums.TipoClimatico;
import org.example.enums.TipoDescoberta;
import org.example.enums.TipoMaterial;
import org.example.eventos.EventoClimatico;
import org.example.eventos.EventoCriatura;
import org.example.eventos.EventoDescoberta;
import org.example.gerenciadores.GerenciadorDeEventos;
import org.example.itens.Alimentos;
import org.example.itens.Materiais;
import org.example.personagens.Rastreador;
import org.example.personagens.Sobrevivente;
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
        Random random = new Random();
        this.vegetacaoDensa = densidadeVegetacao;
        this.faunaAbundante = faunaAbundante;
        this.climaUmido = climaUmido;


        this.getRecursosDisponiveis().add(new Alimentos("Fruta", player, 0.5, 10.0, 0.4, TipoAlimento.FRUTA, OffsetDateTime.now().plusDays(15)));
        this.getRecursosDisponiveis().add(new Alimentos("Carne", player, 1.0, 8.0, 0.5, TipoAlimento.CARNE, OffsetDateTime.now().minusDays(10)));
        this.getRecursosDisponiveis().add(new Alimentos("Raíz", player, 0.3, 12.0, 0.6, TipoAlimento.RAIZES, OffsetDateTime.now().plusDays(12)));
        this.getRecursosDisponiveis().add(new Alimentos("Cogumelo", player, 0.2, 5.0, 0.5, TipoAlimento.COGUMELO, OffsetDateTime.now().plusDays(5)));
        this.getRecursosDisponiveis().add(new Materiais("Madeira", player, 2.0, 20.0, 0.8, 5.0, TipoMaterial.MADEIRA));


        this.getEventos().add(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.7,
                getCriaturasAmbientes().get(2) , getCriaturasAmbientes().get(2).getNivelDePerigo()));
        this.getEventos().add(new EventoDescoberta(true, "Descoberta", "Evento de Descoberta", 0.7,"Você pode encontrar Sobrevivente",
                TipoDescoberta.ABRIGO, List.of(new Materiais("Madeira", player, 2.0, 20.0, 0.8, 5.0, TipoMaterial.MADEIRA))));
        this.getEventos().add(new EventoClimatico(true, "Evento de Chuva acionado", "Afeta a visibilidade", "Evento de Chuva" , 0.5, TipoClimatico.TEMPESTADE, 5, "A chuva reduz a visibilidade e a temperatura, dificultando a exploração e aumentando o consumo de energia."));
        this.getEventos().add(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.5,
                getCriaturasAmbientes().get(5), getCriaturasAmbientes().get(5).getNivelDePerigo()));

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
            if (jogador instanceof Sobrevivente) {
                jogador.diminuirEnergia(4.0*getDificuldadeExploracao()*0.25);
                jogador.diminuirSede(1.0*getDificuldadeExploracao()*0.25);
                jogador.diminuirFome(1.0*getDificuldadeExploracao()*0.25);
            }
            else {
                jogador.diminuirEnergia(4.0*getDificuldadeExploracao()*1.25);
                jogador.diminuirSede(1.0*getDificuldadeExploracao()*1.25);
                jogador.diminuirFome(1.0*getDificuldadeExploracao()*1.25);
            }
        }

        else {
            System.out.println("A exploração é relativamente tranquila.");
            jogador.diminuirEnergia(4.0 * getDificuldadeExploracao());
            jogador.diminuirSede(1.0 * getDificuldadeExploracao());
            jogador.diminuirFome(1.0 * getDificuldadeExploracao());
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