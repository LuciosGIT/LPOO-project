package org.example.personagens;

import org.example.domain.Personagem;
import org.example.itens.Armas;
import org.example.itens.Ferramentas;



public class Mecanico extends Personagem {

    public Mecanico(String nome) {
        this.alterarNomePersonagem(nome);
    }

    @Override
    public void habilidade() {
        // to do
    }

    public void criarNovaArma(Armas arma) {

        System.out.println("Criando arma: " + arma.getNomeItem());
        this.getInventario().adicionarItem(arma);

    }

    public void consertarFerramenta(Ferramentas ferramenta) {

        if (ferramenta.getDurabilidade() < 20.0) {
            ferramenta.setDurabilidade(100.0);
        } else {
            System.out.println("Sua ferramenta ainda está em bom estado");
        }

    }
}