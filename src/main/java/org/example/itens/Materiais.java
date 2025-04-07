package org.example.itens;

import org.example.construcao.BancoDeReceitas;
import org.example.construcao.Receita;
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

    public Materiais combinar(Materiais outroMaterial) {

        Materiais resultado = BancoDeReceitas.buscarReceita(this.getTipoMaterial(), outroMaterial.getTipoMaterial());

        if (resultado != null) {
            // Faz uma cópia e associa ao personagem atual (que está neste material)
            Materiais copia = resultado.copiarPara(this.getPersonagem());
            this.getPersonagem().getInventario().adicionarItem(copia);
            return copia;
        }

        System.out.println("Não foi possível combinar os materiais: "
                + this.getTipoMaterial() + " + " + outroMaterial.getTipoMaterial());
        return null;
    }

    public Materiais copiarPara(Personagem personagem) {
        return new Materiais(
                this.getNomeItem(),
                personagem,
                this.getPeso(),
                this.getDurabilidade(),
                this.getProbabilidadeDeEncontrar(),
                this.getResistencia(),
                this.getTipoMaterial()
        );
    }

    public Double getResistencia() {
        return resistencia;
    }

    public void setResistencia(Double resistencia) {
        this.resistencia = resistencia;
    }

    public TipoMaterial getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(TipoMaterial tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }
}
