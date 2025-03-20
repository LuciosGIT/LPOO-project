package org.example.interfaces;

import org.example.domain.Ambiente;
import org.example.domain.Personagem;

public interface EventoInterface {

    public void executar(Personagem jogador, Ambiente local);
}
