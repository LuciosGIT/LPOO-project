package org.example.ambientes;

public class AmbienteRuinas extends Ambiente{

    //construtor
    public AmbienteRuinas(String nome, String descricao, int dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas){
        super(nome,descricao,dificuldadeExploracao,probabilidadeEventos,condicoesClimaticas);
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
    public void explorar(){
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
