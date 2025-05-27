package org.example.ambientes;

import org.example.domain.*;
import org.example.enums.TipoAlimento;
import org.example.enums.TipoClimatico;
import org.example.enums.TipoFerramenta;
import org.example.enums.TipoMaterial;
import org.example.eventos.EventoCriatura;
import org.example.gerenciadores.GerenciadorDeEventos;
import org.example.itens.Alimentos;
import org.example.itens.Ferramentas;
import org.example.itens.Materiais;
import org.example.utilitarios.ConfiguracaoDoMundo;

import java.time.OffsetDateTime;
import java.util.List;

public class AmbienteCaverna extends Ambiente {

    private boolean poucaLuminosidade;
    private boolean temCriaturas;
    private boolean fonteAgua;

    // Construtor
    public AmbienteCaverna(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas, boolean poucaLuminosidade, Personagem player) {
        super(nome, descricao, dificuldadeExploracao, condicoesClimaticas);

        this.poucaLuminosidade = poucaLuminosidade;

        // Adiciona recursos usando o método da superclasse
        this.adicionarRecurso(new Alimentos("Cogumelo", player, 0.2, 5.0, 0.5,
                TipoAlimento.COGUMELO, OffsetDateTime.now().plusDays(5)));
        this.adicionarRecurso(new Materiais("Diamante Vermelho", player, 8.0, 20.0, 0.6,
                10.0, TipoMaterial.PEDRA));
        this.adicionarRecurso(new Materiais("Metal Firme", player, 8.0, 40.0, 0.2,
                20.0, TipoMaterial.METAL));
        this.adicionarRecurso(new Ferramentas("Chave Real", player, 0.8, 100.0, 0.4, 0.8, TipoFerramenta.CHAVE_ESPECIAL));

        // Adiciona eventos de criaturas usando o método da superclasse
        this.adicionarEvento(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.7,
                ConfiguracaoDoMundo.getCriaturasPadrao().get(3), ConfiguracaoDoMundo.getCriaturasPadrao().get(3).getNivelDePerigo()));
        this.adicionarEvento(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.7,
                ConfiguracaoDoMundo.getCriaturasPadrao().get(1), ConfiguracaoDoMundo.getCriaturasPadrao().get(1).getNivelDePerigo()));
    }

    public AmbienteCaverna() { }

    @Override
    public void explorar(Personagem jogador) {
        if (this.poucaLuminosidade) {
            System.out.println("A pouca luminosidade dificulta a exploração. Você perde mais energia.");
            jogador.diminuirEnergia(5.0 * getDificuldadeExploracao() * 1.25);
            jogador.diminuirFome(4.0 * getDificuldadeExploracao() * 1.25);
            jogador.diminuirSede(4.0 * getDificuldadeExploracao() * 1.25);
        } else {
            System.out.println("A exploração é relativamente tranquila.");
            jogador.diminuirEnergia(4.0 * getDificuldadeExploracao());
            jogador.diminuirSede(3.0 * getDificuldadeExploracao());
            jogador.diminuirFome(3.0 * getDificuldadeExploracao());
        }

    }

    @Override
    public Evento gerarEvento(Personagem jogador) {
        Evento eventoSorteado = GerenciadorDeEventos.sortearEvento(this);
        GerenciadorDeEventos.aplicarEvento(jogador, eventoSorteado);
        return eventoSorteado;
    }

}


