package org.example.eventos;

import org.example.criatura.Lobo;
import org.example.domain.Ambiente;
import org.example.domain.Criatura;
import org.example.domain.Evento;
import org.example.domain.Personagem;

import java.util.List;
import java.util.Random;

public class EventoCriatura extends Evento {

    private Criatura criatura;

    private int nivelDePerigo;

    private boolean evitavel;

    private Random random;// ??


    public EventoCriatura(boolean ativavel, List<String> impacto, String nome, Double probabilidadeOcorrencia, Criatura criatura, boolean evitavel, int nivelDePerigo, Random random) {
        super(ativavel, "Uma criatura surgiu", impacto, nome, probabilidadeOcorrencia);
        this.criatura = criatura;
        this.evitavel = random.nextBoolean();
        this.nivelDePerigo = nivelDePerigo;
        this.random = random;
    }

    @Override
    public void executar(Personagem jogador, Ambiente local) {
        if (isAtivavel()) {
            System.out.println("Más notícias...Uma criatura está à vista!");
        }
        if (!evitavel) {
            criatura.ataque(jogador);
        }
        else {
            System.out.printf("Deu sorte! A criatura %s pelo visto ignorou você%n", this.criatura.getNome());
        }
    }

    public Criatura getCriatura() {
        return criatura;
    }

    public void setCriatura(Criatura criatura) {
        this.criatura = criatura;
    }

    public boolean isEvitavel() {
        return evitavel;
    }

    public void setEvitavel(boolean evitavel) {
        this.evitavel = evitavel;
    }

    public int getNivelDePerigo() {
        return nivelDePerigo;
    }

    public void setNivelDePerigo(int nivelDePerigo) {
        this.nivelDePerigo = nivelDePerigo;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
