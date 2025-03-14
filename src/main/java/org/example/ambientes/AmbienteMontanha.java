package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;

import java.util.List;

public class AmbienteMontanha extends Ambiente {

    //construtor
    public AmbienteMontanha(String nome, String descricao, Double dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas, List<Item> recursosDisponiveis){
        super(nome, descricao, dificuldadeExploracao, probabilidadeEventos, condicoesClimaticas);
    }

    //métodos getters

    //métodos que envolvem o ambiente

    @Override
    public void explorar(Personagem jogador){
        //metodo para encontrar itens ou enfrentar monstros dependendo de probabilidade
    }

    @Override
    public void gerarEvento(){
        //metodo para gerar eventos aleatorias
    }

    @Override
    public void modificarClima(){
        //metodo para modificar o clima
    }

}
