package org.example.condicoes;

import org.example.domain.Condicao;
import org.example.domain.Personagem;
import org.example.utilitarios.ConfiguracaoDoMundo;

public class Infeccao extends Condicao {

    @Override
    public void impacto(Personagem jogadorAfetado) {
        jogadorAfetado.diminuirEnergia(20.0);
        jogadorAfetado.diminuirVida(25.0);
        jogadorAfetado.diminuirSanidade(15.0);
    }


}
