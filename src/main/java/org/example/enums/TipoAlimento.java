package org.example.enums;

public enum TipoAlimento {

    FRUTA("fruta", 5.0),
    CARNE("carne", 15.0),
    ENLATADO("enlatado", 10.0);

    private String value;
    private Double valorNutricional;

    private TipoAlimento(String value, Double valorNutricional) {
        this.value = value;
        this.valorNutricional = valorNutricional;
    }

    public String getValue() {
        return this.value;
    }
    public Double getValorNutricional() {
        return this.valorNutricional;
    }
}
