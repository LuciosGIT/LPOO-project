package org.example.enums;

public enum TipoItem {

    ALIMENTOS("alimentos"),
    AGUA("água"),
    MATERIAIS("materiais"),
    FERRAMENTAS("ferramentas"),
    ARMAS("armas"),
    REMEDIOS("remédios");

    private String value;

    private TipoItem(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
