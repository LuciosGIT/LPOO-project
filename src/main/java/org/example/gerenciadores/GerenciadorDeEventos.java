package org.example.gerenciadores;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.utilitarios.ConfiguracaoDoMundo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GerenciadorDeEventos {

    private List<Evento> eventosPossiveis;

    private Map<Evento, Double> probabilidadeDeOcorrencia;

    private static final List<Evento> historicoDeEventos = new ArrayList<>();

    public GerenciadorDeEventos() {
        this.eventosPossiveis = ConfiguracaoDoMundo.getEventosDoJogo();
    }

    public static Evento sortearEvento(Ambiente local) {
        Random random = new Random();

        List<Evento> eventos = local.getEventos();

        // Soma todas as probabilidades
        double somaProbabilidades = 0;
        for (Evento evento : eventos) {
            somaProbabilidades += evento.getProbabilidadeOcorrencia();
        }

        // Gera um número aleatório entre 0 e soma das probabilidades
        double r = random.nextDouble() * somaProbabilidades;

        // Seleciona o evento correspondente
        double acumulador = 0;
        for (Evento evento : eventos) {
            acumulador += evento.getProbabilidadeOcorrencia();
            if (r <= acumulador) {
                return evento;
            }
        }
        return eventos.get(eventos.size() - 1);
    }

    public static void aplicarEvento(Personagem jogador, Evento evento) {
        evento.executar(jogador, jogador.getLocalizacao());
        historicoDeEventos.add(evento);
    }

    public static void removerEvento(Evento evento) {

    }


}