package org.example.itens;

import org.example.domain.Item;
import org.example.enums.TipoAlimento;

import java.time.OffsetDateTime;

public class Alimentos extends Item {

    private Double valorNutricional;

    private TipoAlimento tipoAlimento;

    private OffsetDateTime validade;

    public Alimentos(TipoAlimento tipoAlimento, OffsetDateTime validade) {
        this.valorNutricional = tipoAlimento.getValorNutricional();
        this.tipoAlimento = tipoAlimento;
        this.validade = validade;
    }

    public Alimentos() {}

    @Override
    public void usar() {
        System.out.println("Consumindo alimento: " + this.getTipoAlimento().getValue());
    }


    public void consumir() {

        this.usar();
        this.personagem.setFome(this.personagem.getFome() - this.valorNutricional);

        if(this.validade.isBefore(OffsetDateTime.now())) {
            this.personagem.setEnergia(this.personagem.getEnergia() - 50);
            this.personagem.setSanidade(this.personagem.getSanidade() - 15);
        }
    }

    public TipoAlimento getTipoAlimento() {
        return tipoAlimento;
    }

    public void setTipoAlimento(TipoAlimento tipoAlimento) {
        this.tipoAlimento = tipoAlimento;
    }

    public OffsetDateTime getValidade() {
        return validade;
    }

    public void setValidade(OffsetDateTime validade) {
        this.validade = validade;
    }

    public Double getValorNutricional() {
        return valorNutricional;
    }

    public void setValorNutricional(Double valorNutricional) {
        this.valorNutricional = valorNutricional;
    }
}
