package org.example.itens;

import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoFerramenta;

public class Ferramentas extends Item {

    private TipoFerramenta tipoFerramenta;

    private Double eficiencia;


    public Ferramentas(String nomeItem, Personagem personagem, Double peso, Double durabilidade, Double probabilidadeDeEncontrar, Double eficiencia, TipoFerramenta tipoFerramenta) {
        super(nomeItem, personagem, peso, durabilidade, probabilidadeDeEncontrar);
        this.eficiencia = eficiencia;
        this.tipoFerramenta = tipoFerramenta;
    }

    public Ferramentas() {}

    @Override
    public void usar() {
        switch(this.tipoFerramenta) {
            case FACA -> this.diminuirDurabilidade(8.0);
            case MACHADO -> this.diminuirDurabilidade(5.0);
            case ISQUEIRO -> this.diminuirDurabilidade(11.0);
            case LANTERNA -> this.diminuirDurabilidade(15.0);
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
