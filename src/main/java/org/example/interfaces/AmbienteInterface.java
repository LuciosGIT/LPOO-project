package org.example.interfaces;

import org.example.domain.Personagem;

public interface AmbienteInterface {

    public void explorar(Personagem jogador);

    public void gerarEvento();

    public void modificarClima();

}
