package org.example.construcao;

import org.example.enums.TipoMaterial;
import org.example.itens.Armas;

public class ReceitaArma {

    private TipoMaterial material1;
    private TipoMaterial material2;
    private Armas resultado;

    public ReceitaArma(TipoMaterial material1, TipoMaterial material2, Armas resultado) {
        this.material1 = material1;
        this.material2 = material2;
        this.resultado = resultado;
    }

    public boolean combinaCom(TipoMaterial m1, TipoMaterial m2) {
        return (m1 == material1 && m2 == material2) || (m1 == material2 && m2 == material1);
    }

    public Armas getResultado() {
        return resultado;
    }
}
