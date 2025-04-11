package org.example.enums;

public enum TipoRemedio {

    BANDAGEM(TipoEfeito.ALIVIAR),
    ANTIBIOTICO(TipoEfeito.TRATAR),
    POCAO(TipoEfeito.CURAR);

    private TipoEfeito value;

    private TipoRemedio(TipoEfeito value) {

        this.value = value;
    }

    public TipoEfeito getValue() {
        return this.value;
    }
}
