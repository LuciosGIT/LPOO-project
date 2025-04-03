package org.example.eventos;

import org.example.domain.Ambiente;
import org.example.domain.Condicao;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.itens.Remedios;

public class EventoDoencaFerimento extends Evento {

    private Condicao tipoCondicao;

    private Remedios cura;

    public EventoDoencaFerimento(Remedios remedio, Condicao tipoCondicao) {
        this.cura = remedio;
        this.tipoCondicao = tipoCondicao;
    }

    public EventoDoencaFerimento(boolean ativavel, String descricao, String impacto, String nome, Double probabilidadeOcorrencia, Remedios remedio, Condicao tipoCondicao) {
        super(ativavel, descricao, impacto, nome, probabilidadeOcorrencia);
        this.cura = remedio;
        this.tipoCondicao = tipoCondicao;
    }

    public EventoDoencaFerimento() {

    }

    @Override
    public void executar(Personagem jogador, Ambiente local) {
        System.out.println("Você está sendo afetado por uma" + tipoCondicao.getDescricao());

        /* ajeitar lógica! para só dar dano enquanto o jogador não usar o remédio*/
        while (jogador.getInventario().getListaDeItems().contains(cura) == false) {
            tipoCondicao.impacto(jogador);
        }
    }
}