package org.example.personagens;

import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.itens.Ferramentas;

public class Rastreador extends Personagem {

    public Rastreador() {
    }

    @Override
    public void habilidade() {
            for (Item recurso : this.getLocalizacao().getRecursosDisponiveis()) {
                double probabilidadeAtual = recurso.getProbabilidadeDeEncontrar();
                double novaProbabilidade = Math.min(probabilidadeAtual * 1.3, 0.95);
                recurso.setProbabilidadeDeEncontrar(novaProbabilidade);
            }
        }
}
