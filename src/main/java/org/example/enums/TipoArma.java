package org.example.enums;

public enum TipoArma {

    CORPO(0, "corpo a corpo", 10.0),
    DISTANCIA(10, "à distância", 20.0);

    private Integer municao;

    private String tipoAtaque;

    private Double dano;

    TipoArma(Integer municao, String tipoAtaque, Double dano) {
        this.municao = municao;
        this.tipoAtaque = tipoAtaque;
        this.dano = dano;
    }

    public String getValue() {
        return tipoAtaque;
    }

    public Integer getMunicao() {
        return municao;
    }

    public void setMunicao(Integer novaMunicao) {
        this.municao = novaMunicao;
    }

    public Double getDano() {
        return this.dano;
    }
}
