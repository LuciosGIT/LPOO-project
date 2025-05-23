package org.example.eventos;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoDescoberta;
import org.example.personagens.Rastreador;
import org.example.personagens.Sobrevivente;
import org.example.utilitarios.Utilitario;

import java.util.List;

public class EventoDescoberta extends Evento {

    private TipoDescoberta tipoDescoberta;
    private List<Item> recursosEncontrados;
    private boolean abrigoSpawnado = false;
    private boolean cavernaSpawnada = false;
    private boolean interacaoCavernaRealizada = false;

    public EventoDescoberta(boolean ativavel, String impacto, String nome, Double probabilidadeOcorrencia,
                            String descricao, TipoDescoberta tipoDescoberta, List<Item> recursosEncontrados) {
        super(ativavel, "Evento de descoberta acionado!", impacto, nome, probabilidadeOcorrencia);
        this.tipoDescoberta = tipoDescoberta;
        this.recursosEncontrados = recursosEncontrados;
    }

    public EventoDescoberta() {}

    @Override
    public void executar(Personagem jogador, Ambiente local) {
        switch (tipoDescoberta) {
            case ABRIGO -> {
                System.out.println("Você encontrou um abrigo!");
                // Apenas marca que o abrigo foi encontrado; a interface cuida de mostrar o actor
                abrigoSpawnado = true;
            }

            case CAVERNA -> {
                System.out.println("Você encontrou uma caverna!");
                // Apenas marca que a caverna foi encontrada; a interface cuida de mostrar o actor
                cavernaSpawnada = true;
            }

            case RUINAS_MISTERIOSAS -> {
                System.out.println("Você encontrou ruínas misteriosas!");
                if (Utilitario.armadilhaInstaurada(jogador)) {
                    System.out.println("Mas uma armadilha o impediu de explorá-las!");
                } else if (jogador instanceof Rastreador) {
                    System.out.println("Você tem as habilidades para explorar e coletar os recursos.");
                    pegarItensEncontrados(jogador);
                } else {
                    System.out.println("Mas você não tem habilidade suficiente para explorá-las.");
                }
            }
        }
    }

    public void interagirComAbrigo(Personagem jogador) {
        if (!abrigoSpawnado) return;

        if (Utilitario.spawnarSobrevivente(jogador)) {
            System.out.println("Um sobrevivente apareceu e te expulsou!");
        } else if (jogador instanceof Rastreador) {
            System.out.println("Você explora o abrigo e coleta os recursos.");
            pegarItensEncontrados(jogador);
        } else {
            System.out.println("Você não tem habilidade suficiente para explorar o abrigo.");
        }

        abrigoSpawnado = false; // impede múltiplas interações
    }

    public boolean interagirComCaverna(Personagem jogador) {
        if (!cavernaSpawnada || interacaoCavernaRealizada) return false;

        if (Utilitario.spawnarMorcego(jogador)) {
            System.out.println("Um bando de morcegos protege a entrada da caverna!");
            interacaoCavernaRealizada = true;
            return false;
        } else if (jogador instanceof Sobrevivente) {
            System.out.println("Você tem as habilidades para explorar a caverna.");
            // Não coletamos os recursos aqui, pois isso será feito na TelaDeJogoCavernaEvento
            interacaoCavernaRealizada = true;
            return true; // Retorna true para indicar que deve abrir a TelaDeJogoCavernaEvento
        } else {
            System.out.println("Você não tem habilidade suficiente para explorar a caverna.");
            interacaoCavernaRealizada = true;
            return false;
        }
    }

    private void pegarItensEncontrados(Personagem jogador) {
        for (Item item : recursosEncontrados) {
            jogador.getInventario().adicionarItem(item);
        }
    }

    public TipoDescoberta getTipoDescoberta() {
        return tipoDescoberta;
    }

    public boolean isAbrigoSpawnado() {
        return abrigoSpawnado;
    }

    public boolean isCavernaSpawnada() {
        return cavernaSpawnada;
    }

    public void setCavernaSpawnada(boolean cavernaSpawnada) {
        this.cavernaSpawnada = cavernaSpawnada;
    }

    public boolean isInteracaoCavernaRealizada() {
        return interacaoCavernaRealizada;
    }

    public void setInteracaoCavernaRealizada(boolean interacaoCavernaRealizada) {
        this.interacaoCavernaRealizada = interacaoCavernaRealizada;
    }

    public List<Item> getRecursosEncontrados() {
        return recursosEncontrados;
    }
}