package org.example.itens;

import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoEfeito;
import org.example.enums.TipoRemedio;

public class Remedios extends Item {

    private TipoRemedio tipoRemedio;

    private TipoEfeito tipoEfeito;

    public Remedios(String nomeItem, Personagem personagem, Double peso, Double durabilidade, Double probabilidadeDeEncontrar, TipoRemedio tipoRemedio, TipoEfeito tipoEfeito) {
        super( nomeItem, personagem, peso, durabilidade, probabilidadeDeEncontrar);
        this.tipoRemedio = tipoRemedio;
        this.tipoEfeito = tipoEfeito;
    }

    @Override
    public void usar() {
        switch (tipoEfeito) {
            case CURAR -> this.getPersonagem().aumentarVida(15.0);
            case ALIVIAR -> this.getPersonagem().aumentarVida(5.0);
            case TRATAR -> this.getPersonagem().aumentarVida(10.0);
        }
    }
}