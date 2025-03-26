package org.example.eventos;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Personagem;

import static org.example.enums.TipoClimatico.CALOR;

public class EventoClimatico extends Evento {

    private String tipoDeClima;
    private int duracaoDeEvento;
    private String efeitoDoAmbiente;

    public EventoClimatico(boolean ativavel, String descricao, String impacto, String nome, Double probabilidadeOcorrencia, String tipoDeClima, int duracaoDeEvento, String efeitoDoAmbiente){
        super(ativavel,descricao,impacto,nome,probabilidadeOcorrencia);
        this.tipoDeClima = tipoDeClima;
        this.duracaoDeEvento = duracaoDeEvento;
        this.efeitoDoAmbiente = efeitoDoAmbiente;
    }

    public String getTipoDeClima(){
        return this.tipoDeClima;
    }

    public int getDuracaoDeEvento(){
        return this.duracaoDeEvento;
    }

    public String getEfeitoDoAmbiente(){
        return this.efeitoDoAmbiente;
    }

    @Override
    public void executar(Personagem jogador, Ambiente local) {

    }

}
