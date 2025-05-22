package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Item;
import org.example.domain.Personagem;
import org.example.enums.TipoAlimento;
import org.example.enums.TipoClimatico;
import org.example.enums.TipoFerramenta;
import org.example.enums.TipoMaterial;
import org.example.eventos.EventoCriatura;
import org.example.gerenciadores.GerenciadorDeEventos;
import org.example.itens.Alimentos;
import org.example.itens.Ferramentas;
import org.example.itens.Materiais;
import org.example.utilitarios.Utilitario;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

public class AmbienteRuinas extends Ambiente {

    Boolean estruturasInstaveis;

    //construtor
    public AmbienteRuinas(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas, Boolean estruturasInstaveis, Personagem player) {
        super(nome, descricao, dificuldadeExploracao, condicoesClimaticas);
        this.getRecursosDisponiveis().add(new Alimentos("Carne", player, 1.0, 8.0, 0.5, TipoAlimento.CARNE, OffsetDateTime.now().plusDays(10)));
        this.getRecursosDisponiveis().add(new Alimentos("Raíz", player, 0.3, 12.0, 0.3, TipoAlimento.RAIZES, OffsetDateTime.now().plusDays(12)));
        this.getRecursosDisponiveis().add(new Materiais("Pedra", player, 8.0, 20.0, 0.9, 10.0, TipoMaterial.PEDRA));
        this.getRecursosDisponiveis().add(new Materiais("Madeira", player, 2.0, 20.0, 0.8, 5.0, TipoMaterial.MADEIRA));
        this.getRecursosDisponiveis().add(new Ferramentas("Machado largado", player, 2.0, 20.0, 0.8, 5.0, TipoFerramenta.MACHADO));
        this.getRecursosDisponiveis().add(new Ferramentas("Faca Esquecida", player, 2.0, 20.0, 0.8, 5.0, TipoFerramenta.FACA));
        this.getRecursosDisponiveis().add(new Alimentos("Sardinha enlatada", player, 0.5, 5.0, 0.4, TipoAlimento.ENLATADO, OffsetDateTime.now().plusDays(5)));
        this.getRecursosDisponiveis().add(new Alimentos("Atum em lata", player, 0.5, 5.0, 0.4, TipoAlimento.ENLATADO, OffsetDateTime.now().minusDays(5)));
        this.getEventos().add(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.5,
                getCriaturasAmbientes().get(0), getCriaturasAmbientes().get(0).getNivelDePerigo()));

        this.estruturasInstaveis = estruturasInstaveis;
    }

    public AmbienteRuinas() { }

    //métodos getters

    //métodos que envolvem o ambiente

    @Override
    public void explorar(Personagem jogador){

        //metodo para encontrar itens ou enfrentar monstros dependendo de probabilidade
        this.gerarEvento(jogador);

        // ExploracaoService.explorar(jogador,this);

        //ao explorar gasta 4 de energia, 1 de fome e 1 de água
        //dificuldade é dada em porcentagem?
        //melhorar isso
        if (this.estruturasInstaveis)
        {
            System.out.println("A pouca luminosidade dificulta a exploração. Você perde mais energia.");
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

        Utilitario.armadilhaInstaurada(jogador);

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