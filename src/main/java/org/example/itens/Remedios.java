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
        System.out.println("Usando Remédio " + this.tipoRemedio);

        double vidaAtual = this.getPersonagem().getVida();

        switch (tipoEfeito) {
            case CURAR -> {
                if (vidaAtual < 100.0) {
                    this.getPersonagem().aumentarVida(100.0 - vidaAtual);
                }
            }
            case ALIVIAR -> {
                if (vidaAtual < 100.0) {
                    this.getPersonagem().aumentarVida(Math.min(30.0, 100.0 - vidaAtual));
                }
            }
            case TRATAR -> {
                if (vidaAtual < 100.0) {
                    this.getPersonagem().aumentarVida(Math.min(10.0, 100.0 - vidaAtual));
                }
            }
        }
        System.out.println(this.getPersonagem().getVida());
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