package org.example.eventos;

import org.example.domain.Ambiente;
import org.example.domain.Condicao;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.enums.TipoCondicao;
import org.example.enums.TipoRemedio;
import org.example.itens.Remedios;

public class EventoDoencaFerimento extends Evento {

    private TipoCondicao tipoCondicao;


    public EventoDoencaFerimento(boolean ativavel, String descricao, String impacto, String nome, Double probabilidadeOcorrencia, TipoCondicao tipoCondicao) {
        super(ativavel, descricao, impacto, nome, probabilidadeOcorrencia);
        this.tipoCondicao = tipoCondicao;
    }

    public EventoDoencaFerimento() {

    }

    @Override
    public void executar(Personagem jogador, Ambiente local) {
        System.out.println("Um evento de doença ou ferimento foi acionado!");
       switch (tipoCondicao) {

           case INFECCAO -> {
               jogador.diminuirEnergia(20.0);
               jogador.diminuirVida(25.0);
               jogador.diminuirSanidade(15.0);
           }
           case HIPOTERMIA  -> {
               jogador.diminuirEnergia(20.0);
               jogador.diminuirVida(10.0);
               jogador.diminuirSanidade(10.0);

               // Após 4 segundos, perde energia novamente
               new Thread(() -> {
                   try {
                       Thread.sleep(4000);
                       jogador.diminuirEnergia(15.0);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }).start();

           }
           case DESIDRATACAO ->  {
               jogador.diminuirEnergia(15.0);
               jogador.diminuirSede(15.0);

               // Segunda vez após 4 segundos
               new Thread(() -> {
                   try {
                       Thread.sleep(4000);
                       jogador.diminuirSede(15.0);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }).start();
           }
       }
    }
}