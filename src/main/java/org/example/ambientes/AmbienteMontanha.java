package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.enums.*;
import org.example.eventos.EventoClimatico;
import org.example.eventos.EventoCriatura;
import org.example.eventos.EventoDescoberta;
import org.example.gerenciadores.GerenciadorDeEventos;
import org.example.itens.Agua;
import org.example.itens.Alimentos;
import org.example.itens.Materiais;

import java.time.OffsetDateTime;
import java.util.List;

public class AmbienteMontanha extends Ambiente {

    private boolean terrenoAcidentado;
    private boolean climaInstavel;

    public AmbienteMontanha(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas, boolean terrenoAcidentado, boolean climaInstavel, Personagem player) {
        super(nome, descricao, dificuldadeExploracao, condicoesClimaticas);

        this.terrenoAcidentado = terrenoAcidentado;
        this.climaInstavel = climaInstavel;

        // Recursos
        this.adicionarRecurso(new Alimentos("Carne", player, 1.0, 8.0, 0.3, TipoAlimento.CARNE, OffsetDateTime.now().plusDays(10)));
        this.adicionarRecurso(new Alimentos("Raíz", player, 0.3, 12.0, 0.5, TipoAlimento.RAIZES, OffsetDateTime.now().plusDays(12)));
        this.adicionarRecurso(new Materiais("Pedra Cristalina", player, 8.0, 20.0, 0.7, 10.0, TipoMaterial.PEDRA));
        this.adicionarRecurso(new Materiais("Madeira", player, 2.0, 20.0, 0.5, 5.0, TipoMaterial.MADEIRA));
        this.adicionarRecurso(new Agua("Água de Degelo", player, 5.0, 20.0, 0.5, Pureza.CONTAMINADA, 10.0));

        // Eventos
        this.adicionarEvento(new EventoDescoberta(true, "Descoberta", "Evento de Descoberta", 0.7,
                "Você pode encontrar Morcego",
                TipoDescoberta.CAVERNA,
                List.of(new Materiais("Pedra", player, 2.0, 20.0, 0.8, 5.0, TipoMaterial.PEDRA))));

        this.adicionarEvento(new EventoClimatico(true, "Evento de Nevasca foi acionado", "Afeta a visibilidade",
                "Evento de Nevasca", 0.7, TipoClimatico.NEVASCA, 5,
                "A nevasca reduz a visibilidade e a temperatura, dificultando a exploração e aumentando o consumo de energia."));
    }

    public AmbienteMontanha() {}

    @Override
    public void explorar(Personagem jogador) {
        if (this.terrenoAcidentado) {
            System.out.println("O terreno acidentado dificulta a exploração. Você perde mais energia.");
            jogador.diminuirEnergia(4.0 * getDificuldadeExploracao() * 1.25);
            jogador.diminuirSede(1.0 * getDificuldadeExploracao() * 1.25);
            jogador.diminuirFome(1.0 * getDificuldadeExploracao() * 1.25);
        } else {
            System.out.println("A exploração é relativamente tranquila.");
            jogador.diminuirEnergia(4.0 * getDificuldadeExploracao());
            jogador.diminuirSede(1.0 * getDificuldadeExploracao());
            jogador.diminuirFome(1.0 * getDificuldadeExploracao());
        }

        if (this.climaInstavel) {
            // Exemplo: aumenta a chance de um evento climático se repetir
            getEventos().stream()
                    .filter(evento -> evento instanceof EventoClimatico)
                    .forEach(evento -> evento.setProbabilidadeOcorrencia(evento.getProbabilidadeOcorrencia() + 0.2));
        }
    }

    @Override
    public Evento gerarEvento(Personagem jogador) {
        Evento eventoSorteado = GerenciadorDeEventos.sortearEvento(this);
        GerenciadorDeEventos.aplicarEvento(jogador, eventoSorteado);
        return eventoSorteado;
    }



    // Getters
    public boolean isTerrenoAcidentado() {
        return terrenoAcidentado;
    }

    public boolean isClimaInstavel() {
        return climaInstavel;
    }
}
