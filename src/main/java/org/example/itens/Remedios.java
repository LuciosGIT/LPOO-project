package org.example.itens;

import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoEfeito;
import org.example.enums.TipoRemedio;

public class Remedios extends Item {

    private TipoRemedio tipoRemedio;

    private TipoEfeito tipoEfeito;

    public Remedios(String nomeItem, Personagem personagem, Double peso, Double durabilidade, Double probabilidadeDeEncontrar, TipoRemedio tipoRemedio) {
        super( nomeItem, personagem, peso, durabilidade, probabilidadeDeEncontrar);
        this.tipoRemedio = tipoRemedio;
        this.tipoEfeito = tipoRemedio.getValue();
    }

    @Override
    public void usar() {
        switch (tipoEfeito) {
            case CURAR -> this.getPersonagem().aumentarVida(100.0 - this.getPersonagem().getVida());
            case ALIVIAR -> this.getPersonagem().aumentarVida(5.0);
            case TRATAR -> this.getPersonagem().aumentarVida(10.0);
        }
    }

    @Override
    public String getImage() {
        switch (tipoRemedio) {
            case POCAO -> {
                return "imagens/assets/itens/remedio/cura.png";
            }
            case BANDAGEM -> {
                return "imagens/assets/itens/remedio/bandagem.png";
            }
            case ANTIBIOTICO -> {
                return "imagens/assets/itens/remedio/trata.png";
            }
            default -> {
                return "imagens/assets/itens/default.png";
            }
        }
    }

}