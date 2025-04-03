package org.example.condicoes;

import org.example.domain.Condicao;
import org.example.domain.Personagem;

public class Hipotermia extends Condicao {


    @Override
    public void impacto(Personagem jogadorAfetado) {
        jogadorAfetado.diminuirEnergia(20.0);
        jogadorAfetado.diminuirVida(10.0);
        jogadorAfetado.diminuirSanidade(10.0);
    }
}