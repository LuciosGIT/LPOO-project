package org.example.implementations;

import org.example.domain.Item;
import org.example.enums.Pureza;

public class Agua extends Item {

    private Pureza pureza;

    private Double volume;

    public Agua(Pureza pureza, Double volume) {
        this.pureza = pureza;
        this.volume = volume;
    }
    public Agua() {}

    @Override
    public void usar() {
        System.out.println("Consumindo Água" + this.pureza.getValue());
    }


    public void beber() {

        this.usar();
        this.personagem.setSede(this.personagem.getSede() - 15);

        if(this.pureza == Pureza.CONTAMINADA) {
            this.personagem.setVida(this.personagem.getVida() - 10);
            this.personagem.setSanidade(this.personagem.getSanidade() - 15);
            this.personagem.setEnergia(this.personagem.getEnergia() - 10);
        }
    }

    public Pureza getPureza() {
        return pureza;
    }

    public void setPureza(Pureza pureza) {
        this.pureza = pureza;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }
}
