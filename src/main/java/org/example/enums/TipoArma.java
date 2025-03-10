package org.example.enums;

public enum TipoArma {

    CORPO(0, "corpo a corpo"),
    DISTANCIA(10, "à distância");

    private Integer municao;

    private String tipoAtaque;

    TipoArma(Integer municao, String tipoAtaque) {
        this.municao = municao;
        this.tipoAtaque = tipoAtaque;
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
}
