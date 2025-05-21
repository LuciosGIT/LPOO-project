package org.example.enums;

public enum TipoClimatico {

    TEMPESTADE("Tempestade"),
    NEVASCA("Nevasca"),
    CALOR("Calor");

    private final String tipoDeClima;

    TipoClimatico(String tipoDeClima) {
        this.tipoDeClima = tipoDeClima;
    }

    public String getTipoDeClima() {
        return tipoDeClima;
    }


}
