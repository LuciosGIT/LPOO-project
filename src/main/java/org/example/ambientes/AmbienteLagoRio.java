package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.Pureza;
import org.example.enums.TipoAlimento;
import org.example.enums.TipoClimatico;
import org.example.enums.TipoMaterial;
import org.example.eventos.EventoClimatico;
import org.example.eventos.EventoCriatura;
import org.example.gerenciadores.GerenciadorDeEventos;
import org.example.itens.Agua;
import org.example.itens.Alimentos;
import org.example.itens.Materiais;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

public class AmbienteLagoRio extends Ambiente {

    Boolean terrenoLamacento;

    //construtor
    public AmbienteLagoRio(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas, Boolean terrenoLamacento, Personagem player) {
        super(nome,descricao,dificuldadeExploracao,condicoesClimaticas);
        this.getRecursosDisponiveis().add(new Materiais("Madeira", player, 2.0, 20.0, 0.9, 5.0, TipoMaterial.MADEIRA));
        this.getRecursosDisponiveis().add(new Agua("Água Potável", player, 5.0, 20.0, 0.5, Pureza.POTAVEL ,10.0));
        this.getRecursosDisponiveis().add(new Agua("Água Contaminada", player, 5.0, 20.0, 0.5, Pureza.CONTAMINADA ,10.0));
        this.getRecursosDisponiveis().add(new Materiais("Pedra", player, 8.0, 20.0, 0.6, 10.0, TipoMaterial.PEDRA));
        this.getRecursosDisponiveis().add(new Materiais("Madeira", player, 2.0, 20.0, 0.9, 5.0, TipoMaterial.MADEIRA));
        this.getEventos().add(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.7,
                getCriaturasAmbientes().get(3) , getCriaturasAmbientes().get(0).getNivelDePerigo()));
        this.getEventos().add(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.5,
                getCriaturasAmbientes().get(0), getCriaturasAmbientes().get(4).getNivelDePerigo()));
        this.getRecursosDisponiveis().add(new Alimentos("Peixe", player, 0.5, 5.0, 0.4, TipoAlimento.PEIXE, OffsetDateTime.now().plusDays(5)));
        this.getRecursosDisponiveis().add(new Alimentos("Alga Doce", player, 0.5, 5.0, 0.4, TipoAlimento.ENLATADO, OffsetDateTime.now().plusDays(5)));
        this.getEventos().add(new EventoClimatico(true, "Evento de Temepestade acionado", "Afeta a visibilidade", "Evento de Tempestade" , 0.5, TipoClimatico.TEMPESTADE, 5, "A chuva reduz a visibilidade e a temperatura, dificultando a exploração e aumentando o consumo de energia."));
        this.terrenoLamacento = terrenoLamacento;
    }

    public AmbienteLagoRio() { }

    //métodos getters

    //métodos que envolvem o ambiente

    @Override
    public void explorar(Personagem jogador){


        this.gerarEvento(jogador);
        //metodo para encontrar itens ou enfrentar monstros dependendo de probabilidade

        // ExploracaoService.explorar(jogador,this);

        //ao explorar gasta 4 de energia, 1 de fome e 1 de água
        //dificuldade é dada em porcentagem?
        //melhorar isso
        if (this.terrenoLamacento)
        {
            System.out.println("O terreno lamacento dificulta a exploração. Você perde mais energia.");
            jogador.diminuirEnergia(4.0*getDificuldadeExploracao()*1.25);
            jogador.diminuirSede(1.0*getDificuldadeExploracao()*1.25);
            jogador.diminuirFome(1.0*getDificuldadeExploracao()*1.25);
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
    public void gerarEvento(Personagem jogador){
        Evento eventoSorteado = GerenciadorDeEventos.sortearEvento(this);
        GerenciadorDeEventos.aplicarEvento(jogador, eventoSorteado);
    }

    @Override
    public void modificarClima(){
        //metodo para modificar o clima
    }

}