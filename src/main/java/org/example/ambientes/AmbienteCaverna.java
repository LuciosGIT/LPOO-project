package org.example.ambientes;

public class AmbienteCaverna extends Ambiente{

    //construtor
    public AmbienteCaverna(String nome, String descricao, int dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas){
        super(nome,descricao,dificuldadeExploracao,probabilidadeEventos,condicoesClimaticas);
    }

    public void explorar(){
        //metodo para encontrar itens ou enfrentar monstros dependendo de probabilidade
    }

    public void gerarEvento(){
        //metodo para gerar eventos aleatorias
    }

    public void modificarClima(){
        //metodo para modificar o clima
    }

}
