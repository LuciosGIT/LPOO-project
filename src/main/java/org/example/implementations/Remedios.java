package org.example.implementations;

import org.example.domain.Item;
import org.example.enums.TipoEfeito;
import org.example.enums.TipoRemedio;

public class Remedios extends Item {

    private TipoRemedio tipoRemedio;

    private TipoEfeito tipoEfeito;

    @Override
    public void usar() {
        switch (tipoEfeito) {
            case CURAR -> this.personagem.setVida(this.personagem.getVida() + 15);
            case ALIVIAR -> this.personagem.setVida(this.personagem.getVida() + 10);
            case TRATAR -> this.personagem.setVida(this.personagem.getVida() + 5);
        }
    }
}