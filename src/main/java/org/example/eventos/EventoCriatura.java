package org.example.eventos;

import org.example.domain.Ambiente;
import org.example.domain.Criatura;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.utilitarios.Utilitario;


public class EventoCriatura extends Evento {

    private Criatura criatura;

    private Double nivelDePerigo;

    private boolean evitavel;


    public EventoCriatura(boolean ativavel, String impacto, String nome, Double probabilidadeOcorrencia, Criatura criatura, Double nivelDePerigo) {
        super(ativavel, "Evento de criatura acionado!", impacto, nome, probabilidadeOcorrencia);
        this.criatura = criatura;
        this.evitavel = Utilitario.getBooleanAleatorio();
        this.nivelDePerigo = criatura.getNivelDePerigo();
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

    public Double getNivelDePerigo() {
        return nivelDePerigo;
    }

    public void setNivelDePerigo(Double nivelDePerigo) {
        this.nivelDePerigo = nivelDePerigo;
    }

}
