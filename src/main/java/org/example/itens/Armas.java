package org.example.itens;

import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoArma;

public class Armas extends Item {

    private TipoArma tipoArma;

    private Double dano;

    private Double alcance;

    public Armas(Double alcance, TipoArma tipoArma) {
        this.alcance = alcance;
        this.dano = tipoArma.getDano();
        this.tipoArma = tipoArma;
    }


    @Override
    public void usar() {
        System.out.println("Você acabou de golpear: " + this.tipoArma.getValue());
    }

    public void atacar(Personagem inimigo) {
        switch (this.tipoArma) {
            case CORPO -> inimigo.diminuirVida(dano);
            case DISTANCIA -> {
                if (this.tipoArma.getMunicao() > 0) {
                    inimigo.diminuirVida(dano);
                    this.tipoArma.diminuirMunicao(1);
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
