package org.example.itens;

import org.example.domain.Item;
import org.example.enums.TipoFerramenta;

public class Ferramentas extends Item {

    private TipoFerramenta tipoFerramenta;

    private Double eficiencia;


    public Ferramentas(Double eficiencia, TipoFerramenta tipoFerramenta) {
        this.eficiencia = eficiencia;
        this.tipoFerramenta = tipoFerramenta;
    }

    public Ferramentas() {}

    @Override
    public void usar() {
        switch(this.tipoFerramenta) {
            case FACA -> this.durabilidade -= 10;
            case MACHADO -> this.durabilidade -= 5;
            case ISQUEIRO -> this.durabilidade -= 10;
            case LANTERNA -> this.durabilidade -= 15;
        }

        // to do
    }

    public TipoFerramenta getTipoFerramenta() {
        return tipoFerramenta;
    }

    public void setTipoFerramenta(TipoFerramenta tipoFerramenta) {
        this.tipoFerramenta = tipoFerramenta;
    }

    public Double getEficiencia() {
        return eficiencia;
    }

    public void setEficiencia(Double eficiencia) {
        this.eficiencia = eficiencia;
    }
}
