package org.example.ambientes;


import com.badlogic.gdx.graphics.g3d.Material;
import org.example.criatura.Cobra;
import org.example.domain.*;
import org.example.enums.TipoAlimento;
import org.example.enums.TipoClimatico;
import org.example.enums.TipoMaterial;
import org.example.eventos.EventoCriatura;
import org.example.gerenciadores.GerenciadorDeEventos;
import org.example.itens.Alimentos;
import org.example.itens.Inventario;
import org.example.itens.Materiais;
import org.example.gerenciadores.GerenciadorDeEventos;
import org.example.utilitarios.Utilitario;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.example.enums.TipoClimatico.CALOR;

public class AmbienteCaverna extends Ambiente {

    private boolean poucaLuminosidade; // se o atributo for true, colocar na interface tela muito escura, até que o personagem utilize sua lanterna!

    private boolean temCriaturas;

    private boolean fonteAgua;

    private boolean utilizandoLanterna;

    //construtor
    public AmbienteCaverna(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas, boolean poucaLuminosidade, Personagem player) {
        super(nome,descricao,dificuldadeExploracao, condicoesClimaticas);
        this.getRecursosDisponiveis().add(new Alimentos("Cogumelo", player, 0.2, 5.0, 0.5, TipoAlimento.COGUMELO, OffsetDateTime.now().plusDays(5)));
        this.getRecursosDisponiveis().add(new Materiais("Diamante Vermelho", player, 8.0, 20.0, 0.6, 10.0, TipoMaterial.PEDRA));
        this.getRecursosDisponiveis().add(new Materiais("Metal Firme", player, 8.0, 40.0, 0.2, 20.0, TipoMaterial.METAL));
        this.getEventos().add(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.7,
                getCriaturasAmbientes().get(3) , getCriaturasAmbientes().get(1).getNivelDePerigo()));
        this.getEventos().add(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.5,
                getCriaturasAmbientes().get(0), getCriaturasAmbientes().get(3).getNivelDePerigo()));
        this.poucaLuminosidade = poucaLuminosidade;
        this.utilizandoLanterna = false;

    }

    public AmbienteCaverna() { }

    //métodos que envolvem o ambiente

    @Override
    public void explorar(Personagem jogador){

        this.gerarEvento(jogador);
        //metodo para encontrar itens ou enfrentar monstros dependendo de probabilidade

        // ExploracaoService.explorar(jogador,this);

        //ao explorar gasta 4 de energia, 1 de fome e 1 de água
        //dificuldade é dada em porcentagem?
        //melhorar isso


        if (this.poucaLuminosidade && !utilizandoLanterna)
        {
            System.out.println("A pouca luminosidade dificulta a exploração. Você perde mais energia.");
            jogador.diminuirEnergia(4.0*getDificuldadeExploracao()*1.25);
            jogador.diminuirFome(1.0*getDificuldadeExploracao()*1.25);
            jogador.diminuirSede(1.0*getDificuldadeExploracao()*1.25);
        }
        else
        {
            System.out.println("A exploração é relativamente tranquila.");
            jogador.diminuirEnergia(4.0*getDificuldadeExploracao());
            jogador.diminuirSede(1.0*getDificuldadeExploracao());
            jogador.diminuirFome(1.0*getDificuldadeExploracao());
        }

        if(this.temCriaturas) {
            this.getEventos().get(1).setProbabilidadeOcorrencia(1.0);
        }

    }

    @Override
    public void gerarEvento(Personagem jogador){
        Evento eventoSorteado = GerenciadorDeEventos.sortearEvento(this);
        GerenciadorDeEventos.aplicarEvento(jogador, eventoSorteado);
    }

    @Override
    public void modificarClima(){
        //metodo para modificar o clima
    }

    public void ativarLanterna() {
        this.utilizandoLanterna = true;
    }

}