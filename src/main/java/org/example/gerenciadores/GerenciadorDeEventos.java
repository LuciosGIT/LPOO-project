package org.example.gerenciadores;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.utilitarios.ConfiguracaoDoMundo;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class GerenciadorDeEventos {

    private List<Evento> eventosPossiveis;

    private Map<Evento, Double> probabilidadeDeOcorrencia;

    private static List<Evento> historicoDeEventos;

    public GerenciadorDeEventos() {
        this.eventosPossiveis = ConfiguracaoDoMundo.getEventosDoJogo();
    }

    public static Evento sortearEvento(Ambiente local) {
        Random random = new Random();

        int index = random.nextInt(local.getEventos().size());

        Evento eventoAleatorio = local.getEventos().get(index);

        return eventoAleatorio;
    }

    public static void aplicarEvento(Personagem jogador) {
        Evento eventoSorteado = sortearEvento(jogador.getLocalizacao());

        eventoSorteado.executar(jogador, jogador.getLocalizacao());

        historicoDeEventos.add(eventoSorteado);
    }

    public static void removerEvento(Evento evento) {

    }


}
