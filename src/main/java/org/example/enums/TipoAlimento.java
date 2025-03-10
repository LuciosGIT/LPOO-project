package org.example.enums;

public enum TipoAlimento {

    FRUTA("fruta"),
    CARNE("carne"),
    ENLATADO("enlatado");

    private String value;

    private TipoAlimento(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
