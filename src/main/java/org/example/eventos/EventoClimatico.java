package org.example.eventos;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.enums.TipoClimatico;
import org.example.utilitarios.Utilitario;

public class EventoClimatico extends Evento {

    private TipoClimatico tipoDeClima;
    private int duracaoDeEvento;
    private String efeitoDoAmbiente;

    public EventoClimatico(boolean ativavel, String descricao, String impacto, String nome, Double probabilidadeOcorrencia, TipoClimatico tipoDeClima, int duracaoDeEvento, String efeitoDoAmbiente){
        super(ativavel,descricao,impacto,nome,probabilidadeOcorrencia);
        this.tipoDeClima = tipoDeClima;
        this.duracaoDeEvento = duracaoDeEvento;
        this.efeitoDoAmbiente = efeitoDoAmbiente;

    }

    public EventoClimatico() {
    }

    public TipoClimatico getTipoDeClima(){
        return this.tipoDeClima;
    }

    public void setTipoDeClima(TipoClimatico tipoDeClima){
        this.tipoDeClima = tipoDeClima;
    }

    public int getDuracaoDeEvento(){
        return this.duracaoDeEvento;
    }

    public String getEfeitoDoAmbiente(){
        return this.efeitoDoAmbiente;
    }

    @Override
    public void executar(Personagem jogador, Ambiente local) {

        for(TipoClimatico tipoDeClimaDoAmbiente : local.getTiposDeClimasDoAmbiente()) { //lista de enum's com os climas do ambiente do ambiente

            for (EventoClimatico climaDoJogoObj : local.getListaDeclimasDoJogo()) {//lista com todos os climas do jogo

                if (tipoDeClimaDoAmbiente == climaDoJogoObj.tipoDeClima && Utilitario.getValorAleatorio() < climaDoJogoObj.getProbabilidadeOcorrencia()) {
                    //o jogador irá sofrer os efeitos do ambiente
                    efeitosDoClima(jogador, climaDoJogoObj, local);

                }
            }

        }

    }

    private void efeitosDoClima(Personagem jogador, EventoClimatico climaDoJogoObj, Ambiente local){

        switch (climaDoJogoObj.tipoDeClima){
            case NEVASCA:
                System.out.printf(climaDoJogoObj.efeitoDoAmbiente);
                jogador.diminuirFome(5.0);
                jogador.diminuirEnergia(5.0);
                local.setDificuldadeExploracao(0.9);
                break;

            case CALOR:
                System.out.printf(climaDoJogoObj.efeitoDoAmbiente);
                jogador.diminuirSede(5.0);
                jogador.diminuirEnergia(5.0);
                local.setDificuldadeExploracao(0.5);
                break;

            case TEMPESTADE:
                System.out.printf(climaDoJogoObj.efeitoDoAmbiente);
                jogador.diminuirEnergia(5.0);
                local.setDificuldadeExploracao(0.9);
                break;

        }
    }

}
