package org.example.itens;

import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoAlimento;
import org.example.personagens.Sobrevivente;

import java.time.OffsetDateTime;

public class  Alimentos extends Item {

    private Double valorNutricional;

    private TipoAlimento tipoAlimento;

    private OffsetDateTime validade;

    public Alimentos(String nomeItem, Personagem personagem, Double peso, Double durabilidade, Double probabilidadeDeEncontrar, TipoAlimento tipoAlimento, OffsetDateTime validade) {
        super(nomeItem, personagem, peso, durabilidade, probabilidadeDeEncontrar);
        this.valorNutricional = tipoAlimento.getValorNutricional();
        this.tipoAlimento = tipoAlimento;
        this.validade = validade;
    }

    public Alimentos(OffsetDateTime validade, TipoAlimento tipoAlimento) {
        this.validade = validade;
        this.tipoAlimento = tipoAlimento;
        this.valorNutricional = tipoAlimento.getValorNutricional();
    }

    public Alimentos() {}

    public boolean validado() {
        if (this.validade.isBefore(OffsetDateTime.now())) {
            return false;
        }
        else{
            return true;
        }
    }
    @Override
    public void usar() {

        System.out.println("Consumindo alimento: " + this.getTipoAlimento().getValue());
        try {
            consumir();
        }catch (Exception e) {
            System.out.println("Erro ao consumir o alimento: " + e.getMessage());
        }

    }

    public void consumir() {

        if (validado()) {
            if(this.getPersonagem() instanceof Sobrevivente) {
                this.getPersonagem().habilidade();
            }
            this.getPersonagem().diminuirFome(valorNutricional);



        } else {
                System.out.println("Alimento fora da validade, você perdeu 50 de energia e 15 de sanidade!");
                this.getPersonagem().diminuirEnergia(50.0);
                this.getPersonagem().diminuirSanidade(15.0);
        }
    }

    public TipoAlimento getTipoAlimento() {
        return tipoAlimento;
    }

    public void alterarTipoAlimento(TipoAlimento tipoAlimento) {
        this.tipoAlimento = tipoAlimento;
    }

    public OffsetDateTime getValidade() {
        return validade;
    }

    public void alterarDataValidade(OffsetDateTime validade) {
        if (validade.isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("A data de validade precisa ser após a data atual!");
        }
        this.validade = validade;
    }

    public Double getValorNutricional() {
        return valorNutricional;
    }

    public void alterarValorNutricional(Double valorNutricional) {
        if (valorNutricional <=0) {
            throw new IllegalArgumentException("O valor nutricional só pode assumir valores maiores que 0!");
        }
        this.valorNutricional = valorNutricional;
    }

    @Override
    public String getImage(){
        switch (this.tipoAlimento){
            case CARNE -> {
                return "imagens/assets/itens/alimentos/carne.png";
            }
            case FRUTA -> {
                return "imagens/assets/itens/alimentos/fruta.png";
            }
            case PEIXE -> {
                return "imagens/assets/itens/alimentos/peixe.png";
            }
            case RAIZES -> {
                return "imagens/assets/itens/alimentos/raizes.png";
            }
            case COGUMELO -> {
                return "imagens/assets/itens/alimentos/cogumelo.png";
            }
            case ENLATADO -> {
                return "imagens/assets/itens/alimentos/enlatado.png"; // to do: colocar o caminho da imagem do enlatado
            }
            default -> {
                return "imagens/assets/itens/default.png";
            }

        }
    }

}
