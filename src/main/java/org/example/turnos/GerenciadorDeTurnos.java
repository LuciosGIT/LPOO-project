package org.example.turnos;

import org.example.domain.Personagem;

public class GerenciadorDeTurnos {

    private int turnoAtual;
    private Personagem personagem;

    public GerenciadorDeTurnos(Personagem personagem) {
        this.turnoAtual = 0;
        this.personagem = personagem;
    }

    public void proximoTurno() {
        turnoAtual++;
        System.out.println("Turno #" + turnoAtual);
        personagem.aplicarEfeitosDoTurno();
    }

    public int getTurnoAtual() {
        return turnoAtual;
    }
}
