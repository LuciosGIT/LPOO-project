package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;

import java.util.List;

public class AmbienteFloresta extends Ambiente {

    //construtor
    public AmbienteFloresta(String nome, String descricao, int dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas, List<Item> recursosDisponiveis){
        super(nome, descricao, dificuldadeExploracao, probabilidadeEventos, condicoesClimaticas, recursosDisponiveis);
    }

    //métodos getters

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getDescricao(){
        return descricao;
    }

    @Override
    public int getDificuldadeExploracao(){
        return dificuldadeExploracao;
    }

    @Override
    public double getProbabilidadeEventos(){
        return  probabilidadeEventos;
    }

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
