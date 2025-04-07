package org.example.itens;

import org.example.domain.Criatura;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoArma;

public class Armas extends Item {

    private TipoArma tipoArma;

    private Double dano;

    private Double alcance;

    public Armas(String nomeItem, Double dano, Personagem personagem, Double peso, Double durabilidade, Double probabilidadeDeEncontrar, Double alcance, TipoArma tipoArma) {
        super(nomeItem, personagem,  peso,  durabilidade,  probabilidadeDeEncontrar);
        this.alcance = alcance;
        this.dano = dano;
        this.tipoArma = tipoArma;
    }


    @Override
    public void usar() {
        System.out.println("Você acabou de golpear: " + this.tipoArma.getValue());
    }

    public void atacar(Criatura inimigo) {
        switch (this.tipoArma) {
            case CORPO -> inimigo.diminuirVida(dano);
            case DISTANCIA -> {
                if (this.tipoArma.getMunicao() > 0) {
                    inimigo.diminuirVida(dano);
                    this.tipoArma.diminuirMunicao(1);
                }
                else {
                    System.out.println("Arma sem munição suficiente!");
                }
            }
        }
    }

    public Double getAlcance() {
        return alcance;
    }

    public void setAlcance(Double alcance) {
        this.alcance = alcance;
    }

    public Double getDano() {
        return dano;
    }

    public void setDano(Double dano) {
        this.dano = dano;
    }

    public TipoArma getTipoArma() {
        return tipoArma;
    }

    public void setTipoArma(TipoArma tipoArma) {
        this.tipoArma = tipoArma;
    }
}
