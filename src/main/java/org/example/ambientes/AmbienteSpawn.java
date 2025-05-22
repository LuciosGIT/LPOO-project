package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.enums.TipoClimatico;
import org.example.gerenciadores.GerenciadorDeEventos;

import java.util.List;

public class AmbienteSpawn extends Ambiente {
    public AmbienteSpawn(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas) {
        super(nome, descricao, dificuldadeExploracao, condicoesClimaticas);
    }

    public AmbienteSpawn() {
    }

    @Override
    public void explorar(Personagem jogador) {

    }

    @Override
    public Evento gerarEvento(Personagem jogador){
        Evento eventoSorteado = GerenciadorDeEventos.sortearEvento(this);
        GerenciadorDeEventos.aplicarEvento(jogador, eventoSorteado);
        return eventoSorteado;
    }

    @Override
    public void modificarClima() {

    }
}