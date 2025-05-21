package org.example.itens;

import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoFerramenta;
import org.example.personagens.Mecanico;

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

        // Verifica se a ferramenta está danificada
        if (this.getDurabilidade() < 20.0) {
            System.out.println("A ferramenta " + getNomeItem() + " está danificada!");

            // Verifica se o personagem é um mecânico e tenta consertar
            if (this.getPersonagem() instanceof Mecanico mecanico) {
                System.out.println(mecanico.getNome() + " é um mecânico! Primeiro aguarde o conserto da ferramenta...");
                mecanico.consertarFerramenta(this);
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e) {
                    System.out.println("Erro durante o tempo de espera!");
                    Thread.currentThread().interrupt();
                }

                aplicarEfeitoFerramenta(this.tipoFerramenta);
            }

        }

        else {
            aplicarEfeitoFerramenta(this.tipoFerramenta);
        }

    }

    public TipoFerramenta getTipoFerramenta() {
        return tipoFerramenta;
    }

    public void aplicarEfeitoFerramenta(TipoFerramenta tipoFerramenta) {

        switch (this.tipoFerramenta) {
            case FACA -> {
                System.out.println("Cortando com a ferramenta " + this.getNomeItem());
                // aplicar efeito no ambiente
                this.diminuirDurabilidade(8.0);
            }
            case MACHADO -> {
                System.out.println("Cortando com a ferramenta " + this.getNomeItem());
                // aplicar efeito no ambiente
                this.diminuirDurabilidade(5.0);
            }
            case ISQUEIRO -> {
                System.out.println("Acendendo fogueira com: " + this.getNomeItem());
                // aplicar efeito no ambiente
                this.diminuirDurabilidade(11.0);
            }
            case LANTERNA -> {
                System.out.println("Iluminando o ambiente com: " + this.getNomeItem());
                // aplicar efeito no ambiente
                this.diminuirDurabilidade(15.0);
            }

            case CHAVE_ESPECIAL -> {
                System.out.println("Você está utilizando a Chave do Rei " + this.getNomeItem());
                // aplicar efeito no ambiente
                this.diminuirDurabilidade(15.0);
            }
        }
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

    @Override
    public String getImage() {

        switch (this.tipoFerramenta) {
            case FACA -> {
                return "imagens/assets/itens/ferramentas/faca.png";
            }
            case MACHADO -> {
                return "imagens/assets/itens/ferramentas/machado.png";
            }
            case ISQUEIRO -> {
                return "imagens/assets/itens/ferramentas/isqueiro.png";
            }
            case LANTERNA -> {
                return "imagens/assets/itens/ferramentas/lanterna.png";
            }

            case CHAVE_ESPECIAL -> {
                return "imagens/assets/itens/ferramentas/chave-especial.png";
            }
        }
        return "imagens/assets/itens/default.png";
    }

}
