package org.example.gerenciadores;

import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.utilitarios.ConfiguracaoDoMundo;

import java.util.List;

public class GerenciadorDeAmbientes {

    private List<Ambiente> ambientesDisponiveis;

    // private ?? climaGlobal;

    private List<Ambiente> historicoDeAmbientes;

    public GerenciadorDeAmbientes(List<Ambiente> ambientesDisponiveis/*ClimaGlobal climaGlobal*/, List<Ambiente> historicoDeAmbientes) {
        this.ambientesDisponiveis = ConfiguracaoDoMundo.getAmbientesDoJogo();
        // this.climaGlobal = climaGlobal;
        this.historicoDeAmbientes = historicoDeAmbientes;
    }

    public GerenciadorDeAmbientes ()  {}

    public static void mudarAmbiente(Personagem jogador, Ambiente novoAmbiente) {
        jogador.alterarLocalizacao(novoAmbiente);
    }

    public static void modificarRecursos(Ambiente local, Item recurso) {
        local.getRecursosDisponiveis().remove(recurso);
    }

}