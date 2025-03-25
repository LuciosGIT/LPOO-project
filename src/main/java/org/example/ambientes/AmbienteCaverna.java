package org.example.ambientes;


import com.badlogic.gdx.graphics.g3d.Material;
import org.example.criatura.Cobra;
import org.example.domain.Ambiente;
import org.example.domain.Criatura;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoAlimento;
import org.example.enums.TipoMaterial;
import org.example.itens.Alimentos;
import org.example.itens.Inventario;
import org.example.itens.Materiais;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class AmbienteCaverna extends Ambiente {

    Boolean poucaLuminosidade;

    //construtor
    public AmbienteCaverna(String nome, String descricao, Double dificuldadeExploracao, double probabilidadeEventos, String condicoesClimaticas, boolean poucaLuminosidade){
        super(nome,descricao,dificuldadeExploracao,probabilidadeEventos,condicoesClimaticas);
        this.getRecursosDisponiveis().add(new Alimentos("Cogumelo", null, 0.2, 5.0, 0.5, TipoAlimento.COGUMELO, OffsetDateTime.now().plusDays(5)));
        this.getRecursosDisponiveis().add(new Materiais("Pedra", null, 8.0, 20.0, 0.6, 10.0, TipoMaterial.PEDRA));
        this.getRecursosDisponiveis().add(new Materiais("Metal", null, 8.0, 40.0, 0.2, 20.0, TipoMaterial.METAL));
        this.poucaLuminosidade = poucaLuminosidade;

    }

    //métodos que envolvem o ambiente

    @Override
    public void explorar(Personagem jogador){

        //metodo para encontrar itens ou enfrentar monstros dependendo de probabilidade


        ExploracaoService.explorar(jogador, jogador.getInventario().getListaDeItems(), getDificuldadeExploracao());

        //ao explorar gasta 4 de energia, 1 de fome e 1 de água
        //dificuldade é dada em porcentagem?
        //melhorar isso


        if (this.poucaLuminosidade)
        {
            System.out.println("A pouca luminosidade dificulta a exploração. Você perde mais energia.");
            jogador.diminuirEnergia(4.0*getDificuldadeExploracao()*1.25);
            jogador.diminuirSede(1.0*getDificuldadeExploracao()*1.25);
            jogador.diminuirFome(1.0*getDificuldadeExploracao()*1.25);

            //implementar dano da cobra
            if(jogador.getEstaEnvenedo()){
                jogador.danoPorEnvenenamento(getCriaturasAmbientes(),getDificuldadeExploracao());
            }

        }
        else
        {
            System.out.println("A exploração é relativamente tranquila.");
            jogador.diminuirEnergia(4.0*getDificuldadeExploracao());
            jogador.diminuirSede(1.0*getDificuldadeExploracao());
            jogador.diminuirFome(1.0*getDificuldadeExploracao());
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