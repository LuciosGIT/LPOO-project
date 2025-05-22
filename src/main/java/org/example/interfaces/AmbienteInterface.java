package org.example.interfaces;

import org.example.domain.Evento;
import org.example.domain.Personagem;

public interface AmbienteInterface {

    public void explorar(Personagem jogador);

    public Evento gerarEvento(Personagem jogador);

    public void modificarClima();

}
