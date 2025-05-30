package org.example.itens;

import org.example.domain.Criatura;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoArma;

public class Armas extends Item {

    private TipoArma tipoArma;

    private Double dano;

    private Double alcance;

    public Armas(String nomeItem, Double dano, Personagem personagem, Double peso, Double durabilidade, Double probabilidadeDeEncontrar, Double alcance, TipoArma tipoArma) {
        super(nomeItem, personagem,  peso,  durabilidade,  probabilidadeDeEncontrar);
        this.alcance = alcance;
        this.dano = dano;
        this.tipoArma = tipoArma;
    }

    public Armas atribuirPara(Personagem personagem) {
        return new Armas(
                this.getNomeItem(),
                this.getDano(),
                personagem,
                this.getPeso(),
                this.getDurabilidade(),
                this.getProbabilidadeDeEncontrar(),
                this.getAlcance(),
                this.getTipoArma()
        );
    }

    @Override
    public void usar() {
        System.out.println("Você acabou de golpear: " + this.tipoArma.getValue());
    }

    public void atacar(Criatura inimigo) {
        switch (this.tipoArma) {
            case CORPO ->  {
                inimigo.diminuirVida(dano);
                diminuirDurabilidade(20.0);
            }
            case DISTANCIA -> {
                if (this.tipoArma.getMunicao() > 0) {
                    inimigo.diminuirVida(dano);
                    this.tipoArma.diminuirMunicao(1);
                    diminuirDurabilidade(15.0);
                }
                else {
                    System.out.println("Arma sem munição suficiente!");
                }
            }
        }
    }

    public Double getAlcance() {
        return alcance;
    }

    public void setAlcance(Double alcance) {
        this.alcance = alcance;
    }

    public Double getDano() {
        return dano;
    }

    public void setDano(Double dano) {
        this.dano = dano;
    }

    public TipoArma getTipoArma() {
        return tipoArma;
    }

    public void setTipoArma(TipoArma tipoArma) {
        this.tipoArma = tipoArma;
    }

    @Override
    public String getImage() {
        switch (this.getNomeItem()){
            case "MACHADO":
                return "imagens/assets/itens/armas/machadoArma.png";
            case "ESPADA":
                return "imagens/assets/itens/armas/espada.png";
            case "FACA":
                return "imagens/assets/itens/armas/facaArma.png";
            case "ARCO":
                return "imagens/assets/itens/armas/arco.png";
            case "BESTA":
                return "imagens/assets/itens/armas/besta.png";
            case "REVOLVER":
                return "imagens/assets/itens/armas/revolver.png";
            case "PUNHAL DOS SUSSURROS":
                return "imagens/assets/itens/armas/punhalDosSussurros.png";
            case "LANÇA ETÉREA":
                return "imagens/assets/itens/armas/lancaEterea.png";
            default:
                return "imagens/assets/itens/default.png";
        }
    }
}