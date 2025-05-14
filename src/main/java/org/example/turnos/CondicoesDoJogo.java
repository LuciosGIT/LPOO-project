package org.example.turnos;

import org.example.domain.Personagem;
import org.example.enums.EstadoDoJogo;

public class CondicoesDoJogo {

    private int turnosSobrevividos;
    private final int turnosParaVencer;

    private boolean encontrouRefugio;
    private boolean construiuAbrigo;

    public CondicoesDoJogo(int turnosParaVencer) {
        this.turnosParaVencer = turnosParaVencer;
        this.turnosSobrevividos = 0;
    }

    public EstadoDoJogo verificarEstado(Personagem personagem) {
        turnosSobrevividos++;

        // DERROTA
        if (personagem.getVida() <= 0) return EstadoDoJogo.DERROTA;
        if (personagem.getFome() >= 100 || personagem.getSede() >= 100) return EstadoDoJogo.DERROTA;
        if (personagem.getSanidade() <= 0) return EstadoDoJogo.DERROTA;
        if (personagem.getEnergia() <= 0) return EstadoDoJogo.DERROTA;

        // VITÓRIA
        if (turnosSobrevividos >= turnosParaVencer) return EstadoDoJogo.VITORIA;
        if (encontrouRefugio || construiuAbrigo) return EstadoDoJogo.VITORIA;

        return EstadoDoJogo.JOGANDO;
    }

    public void setEncontrouRefugio(boolean b) { this.encontrouRefugio = b; }
    public void setConstruiuAbrigo(boolean b) { this.construiuAbrigo = b; }

    public int getTurnosSobrevividos() {
        return turnosSobrevividos;
    }
}
