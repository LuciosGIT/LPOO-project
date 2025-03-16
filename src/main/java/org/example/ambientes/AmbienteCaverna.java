package org.example.ambientes;


import com.badlogic.gdx.graphics.g3d.Material;
import org.example.domain.Ambiente;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoAlimento;
import org.example.enums.TipoMaterial;
import org.example.itens.Alimentos;
import org.example.itens.Inventario;
import org.example.itens.Materiais;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

public class AmbienteCaverna extends Ambiente {

    Random random = new Random();

    int poucaLuminosidade;

    //construtor
    public AmbienteCaverna(String nome, String descricao, Double dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas, List<Item> recursosDisponiveis, int poucaLuminosidade){
        super(nome,descricao,dificuldadeExploracao,probabilidadeEventos,condicoesClimaticas);
        this.getRecursosDisponiveis().add(new Alimentos(OffsetDateTime.now().plusDays(5), TipoAlimento.COGUMELO));
        this.getRecursosDisponiveis().add(new Materiais(5.0,TipoMaterial.PEDRA));
        this.getRecursosDisponiveis().add(new Materiais(10.0,TipoMaterial.METAL));
        this.poucaLuminosidade = poucaLuminosidade;
    }

    //métodos que envolvem o ambiente

    @Override
    public void explorar(Personagem jogador){

        //metodo para encontrar itens ou enfrentar monstros dependendo de probabilidade
        double numeroAleatorio;

        setProbabilidades();

        //encontrou monstro? se não o personagem passa a procurar itens
        if((numeroAleatorio = random.nextDouble()) < getDificuldadeExploracao()){
            //evento de luta??
        }
        else
        {

            for(Item recursoDisponivel  : this.getRecursosDisponiveis()){

                if((numeroAleatorio = random.nextDouble()) < recursoDisponivel.getProbabilidadeDeEncontrar()){
                    //adiciona o item recursoDisponivel ao inventário
                    //adicionar item já valida se o inventário está cheio
                    jogador.getInventario().adicionarItem(recursoDisponivel);
                    System.out.printf("Você coletou um(a) %s", recursoDisponivel.getNomeItem());
                }
                else{
                    System.out.print("Nenhum item encontrado");
                }
            }
        }

        //ao explorar gasta 4 de energia, 1 de fome e 1 de água
        //dificuldade é dada em porcentagem?

        jogador.diminuirEnergia(4.0*getDificuldadeExploracao());
        jogador.diminuirSede(1.0*getDificuldadeExploracao());
        jogador.diminuirFome(1.0*getDificuldadeExploracao());

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