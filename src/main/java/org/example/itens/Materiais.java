package org.example.itens;

import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoMaterial;

public class Materiais extends Item {

    private TipoMaterial tipoMaterial;

    private Double resistencia;

    public Materiais(String nomeItem, Personagem personagem, Double peso, Double durabilidade, Double probabilidadeDeEncontrar, Double resistencia, TipoMaterial tipoMaterial) {
        super(nomeItem,  personagem,  peso,  durabilidade,  probabilidadeDeEncontrar);
        this.resistencia = resistencia;
        this.tipoMaterial = tipoMaterial;
    }

    public Materiais(Double resistencia, TipoMaterial tipoMaterial) {
        this.resistencia = resistencia;
        this.tipoMaterial = tipoMaterial;
    }

    public Materiais() {
    }

    @Override
    public void usar() {
        // System.out.println("");
    }

    //public Materiais combinar(Materiais material) {
        // to do
    // }

}
