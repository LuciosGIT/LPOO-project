package org.example.enums;

public enum Pureza {

    POTAVEL("potável"),
    CONTAMINADA("contaminada");

    private String value;

    private Pureza(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
