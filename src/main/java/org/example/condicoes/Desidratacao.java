package org.example.condicoes;

import org.example.domain.Condicao;
import org.example.domain.Personagem;

public class Desidratacao extends Condicao {
    @Override
    public void impacto(Personagem jogadorAfetado) {
        jogadorAfetado.diminuirEnergia(25.0);
        jogadorAfetado.aumentarSede(25.0);
    }
}