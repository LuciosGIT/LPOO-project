package org.example.construcao;

import org.example.enums.TipoMaterial;
import org.example.itens.Materiais;

public class Receita {

    private final TipoMaterial material1;
    private final TipoMaterial material2;
    private final Materiais resultado;

    public Receita(TipoMaterial material1, TipoMaterial material2, Materiais resultado) {
        this.material1 = material1;
        this.material2 = material2;
        this.resultado = resultado;
    }

    public boolean combinaCom(TipoMaterial m1, TipoMaterial m2) {
        return (m1 == material1 && m2 == material2) || (m1 == material2 && m2 == material1);
    }

    public Materiais getResultado() {
        return resultado;
    }
}

