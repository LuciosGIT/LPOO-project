package org.example.itens;

import org.example.construcao.BancoDeReceitas;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoMaterial;

import javax.naming.NameNotFoundException;

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
        switch (tipoMaterial) {
            case METAL -> System.out.println("Usando material de metal");
            case PEDRA -> System.out.println("Usando material de pedra");
            case MADEIRA -> System.out.println("Usando material de madeira");
        }
    }

    public Materiais combinar(Materiais outroMaterial) throws NameNotFoundException {

        Materiais resultado = BancoDeReceitas.buscarReceita(this.getTipoMaterial(), outroMaterial.getTipoMaterial());

        if (resultado != null) {
            // Faz uma cópia e associa ao personagem atual (que está neste material)
            Materiais novo = resultado.atribuirPara(this.getPersonagem());
            this.getPersonagem().getInventario().removerItem(this.getNomeItem());
            this.getPersonagem().getInventario().removerItem(outroMaterial.getNomeItem());
            this.getPersonagem().getInventario().adicionarItem(novo);
            return novo;
        }

        System.out.println("Não foi possível combinar os materiais: "
                + this.getTipoMaterial() + " + " + outroMaterial.getTipoMaterial());
        return null;
    }

    public Materiais atribuirPara(Personagem personagem) {
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

    @Override
    public String getImage(){

        switch (this.tipoMaterial){
            case METAL -> {
                return "imagens/assets/itens/materiais/metal.png";
            }
            case PEDRA -> {
                return "imagens/assets/itens/materiais/pedra.png";
            }
            case MADEIRA -> {
                return "imagens/assets/itens/materiais/madeira.png";
            }
            case RESULTADO_MADEIRA_MADEIRA -> {
                return "imagens/assets/itens/materiais/tábuaReforçada.png";
            }
            case RESULTADO_METAL_METAL -> {
                return "imagens/assets/itens/materiais/placaDeMetalReforçada.png";
            }
            case RESULTADO_PEDRA_PEDRA -> {
                return "imagens/assets/itens/materiais/blocoDePedra.png";
            }

            /*to do: colocar as imagens dos resultados restantes
            case RESULTADO_MADEIRA_METAL -> {

            }
            case RESULTADO_PEDRA_MADEIRA -> {

            }
            case RESULTADO_PEDRA_METAL -> {

            }

             */

            default -> {
                return "imagens/assets/itens/default.png";
            }

        }

    }


}