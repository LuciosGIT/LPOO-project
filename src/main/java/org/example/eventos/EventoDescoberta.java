package org.example.eventos;

import jdk.jshell.execution.Util;
import org.example.domain.*;
import org.example.enums.TipoDescoberta;
import org.example.utilitarios.Utilitario;

import java.util.List;

public class EventoDescoberta extends Evento {

    private TipoDescoberta tipoDescoberta;

    private List<Item> recursosEncontrados;

    // to do : implementar as habilidades requeridas para descoberta

    public EventoDescoberta(boolean ativavel, String impacto, String nome, Double probabilidadeOcorrencia, String descricao, TipoDescoberta tipoDescoberta, List<Item> recursosEncontrados) {
        super(ativavel, "Evento de criatura acionado!", impacto, nome, probabilidadeOcorrencia);
        this.recursosEncontrados = recursosEncontrados;
        this.tipoDescoberta = tipoDescoberta;

    }

    @Override
    public void executar(Personagem jogador, Ambiente local) {

        switch (tipoDescoberta)  {
            case ABRIGO -> {
                if (Utilitario.spawnarSobrevivente(jogador)) {
                    System.out.println("Você encontrou um abrigo, mas um sobrevivente lhe expulsou!");
                }
                else {
                    System.out.println("Você encontrou um abrigo, hora de coletar os recursos!");
                    pegarItensEncontrados(jogador);
                }
            }
            case CAVERNA -> {
                if (Utilitario.spawnarMorcego(jogador)) {
                    System.out.println("Você encontrou uma caverna, mas um bando de morcegos estava protegendo os recursos!");
                }
                else {
                    System.out.println("Você encontrou uma caverna, hora de coletar os recursos!");
                    pegarItensEncontrados(jogador);
                }
            }
            case RUINAS_MISTERIOSAS -> {
                if (Utilitario.armadilhaInstaurada(jogador)) {
                    System.out.println("Você encontrou ruínas misteriosas, mas uma armadilha lhe impediu de explorá-las!");
                }
                else {
                    System.out.println("Você encontrou ruínas misteriosas, hora de coletar os recursos!");
                    pegarItensEncontrados(jogador);
                }
            }
        }

    }

    private void pegarItensEncontrados(Personagem jogador) {
        for(Item item : recursosEncontrados) {
            jogador.getInventario().adicionarItem(item);
        }

    }
}