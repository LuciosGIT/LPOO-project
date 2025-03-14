package org.example.ambientes;


import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;


import java.util.List;
import java.util.Random;

public class AmbienteCaverna extends Ambiente {

    Random random = new Random();

    //construtor
    public AmbienteCaverna(String nome, String descricao, Double dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas, List<Item> recursosDisponiveis){
        super(nome,descricao,dificuldadeExploracao,probabilidadeEventos,condicoesClimaticas);
    }

    //métodos getters

    /* @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getDescricao(){
        return descricao;
    }

    @Override
    public Double getDificuldadeExploracao(){
        return dificuldadeExploracao;
    }

    @Override
    public double getProbabilidadeEventos(){
        return  probabilidadeEventos;
    }

     */


    //métodos que envolvem o ambiente

    @Override
    public void explorar(Personagem jogador){

        //metodo para encontrar itens ou enfrentar monstros dependendo de probabilidade

        for(Item recursosDisponiveis  : this.getRecursosDisponiveis())
            if((random.nextDouble()) > recursosDisponiveis.getProbabilidadeDeEncontrar()){
               /*
                   A quantidade de madeira será multiplicada pelo número aleatório e por 10
                   fazendo que a quantidade de madeira seja aleatória, caso o usuário tenha um machado
                   a sua porcentagem de conseguir madeira será maior (nùmero aleatoria irá ter um valor mínimo)
                */

            }

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
