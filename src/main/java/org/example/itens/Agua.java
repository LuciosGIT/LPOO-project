package org.example.itens;

import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.Pureza;

public class Agua extends Item {

    private Pureza pureza;

    private Double volume;

    public Agua(String nomeItem, Personagem personagem, Double peso, Double durabilidade, Double probabilidadeDeEncontrar, Pureza pureza, Double volume) {
        super(nomeItem, personagem,  peso,  durabilidade,  probabilidadeDeEncontrar);
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
        this.getPersonagem().diminuirSede(15.0);

        if(this.pureza == Pureza.CONTAMINADA) {

            this.getPersonagem().diminuirVida(10.0);
            this.getPersonagem().diminuirSanidade(15.0);
            this.getPersonagem().diminuirEnergia(10.0);

        }
    }

    public Pureza getPureza() {
        return pureza;
    }

    public void alteraraPureza(Pureza pureza) {

        this.pureza = pureza;
    }

    public Double getVolume() {
        return volume;
    }

    public void alterarVolume(Double volume) {
        this.volume = volume;
    }
}
