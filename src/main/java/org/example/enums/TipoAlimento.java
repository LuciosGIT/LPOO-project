package org.example.enums;

import java.util.Random;

public enum TipoAlimento {

    FRUTA("fruta", 5.0),
    CARNE("carne", 15.0),
    ENLATADO("enlatado", 10.0),
    RAIZES("raizes", 10.0),
    COGUMELO("cogumelo", 5.0);

    private static final Random random = new Random();
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

    public static TipoAlimento sortearItem() {
        TipoAlimento[] valores = values();
        return valores[random.nextInt(valores.length)];
    }
}
