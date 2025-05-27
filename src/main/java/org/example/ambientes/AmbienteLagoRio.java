package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.enums.*;
import org.example.eventos.EventoClimatico;
import org.example.eventos.EventoCriatura;
import org.example.gerenciadores.GerenciadorDeEventos;
import org.example.itens.Agua;
import org.example.itens.Alimentos;
import org.example.itens.Ferramentas;
import org.example.itens.Materiais;
import org.example.utilitarios.ConfiguracaoDoMundo;

import java.time.OffsetDateTime;
import java.util.List;

public class AmbienteLagoRio extends Ambiente {

    private Boolean terrenoLamacento;

    // Construtor
    public AmbienteLagoRio(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas, Boolean terrenoLamacento, Personagem player) {
        super(nome, descricao, dificuldadeExploracao, condicoesClimaticas);

        this.terrenoLamacento = terrenoLamacento;

        // Adiciona recursos
        this.adicionarRecurso(new Materiais("Madeira", player, 2.0, 20.0, 0.9, 5.0, TipoMaterial.MADEIRA));
        this.adicionarRecurso(new Agua("Água Potável", player, 5.0, 20.0, 0.5, Pureza.POTAVEL, 10.0));
        this.adicionarRecurso(new Agua("Água Contaminada", player, 5.0, 20.0, 0.5, Pureza.CONTAMINADA, 10.0));
        this.adicionarRecurso(new Materiais("Pedra", player, 8.0, 20.0, 0.6, 10.0, TipoMaterial.PEDRA));
        this.adicionarRecurso(new Materiais("Madeira", player, 2.0, 20.0, 0.9, 5.0, TipoMaterial.MADEIRA));
        this.adicionarRecurso(new Alimentos("Peixe", player, 0.5, 5.0, 0.4, TipoAlimento.PEIXE, OffsetDateTime.now().plusDays(5)));
        this.adicionarRecurso(new Alimentos("Alga Doce", player, 0.5, 5.0, 0.4, TipoAlimento.ENLATADO, OffsetDateTime.now().plusDays(5)));
        this.adicionarRecurso(new Ferramentas("Chave Real", player, 0.8, 100.0, 0.3, 0.8, TipoFerramenta.CHAVE_ESPECIAL));

        // Adiciona eventos
        this.adicionarEvento(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.7,
                ConfiguracaoDoMundo.getCriaturasPadrao().get(3), ConfiguracaoDoMundo.getCriaturasPadrao().get(3).getNivelDePerigo()));

        this.adicionarEvento(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.5,
                ConfiguracaoDoMundo.getCriaturasPadrao().get(4), ConfiguracaoDoMundo.getCriaturasPadrao().get(4).getNivelDePerigo()));

        this.adicionarEvento(new EventoClimatico(true, "Evento de Tempestade acionado", "Afeta a visibilidade",
                "Evento de Tempestade", 0.5, TipoClimatico.TEMPESTADE, 5,
                "A chuva reduz a visibilidade e a temperatura, dificultando a exploração e aumentando o consumo de energia."));
    }

    public AmbienteLagoRio() { }

    @Override
    public void explorar(Personagem jogador) {
        if (this.terrenoLamacento) {
            System.out.println("O terreno lamacento dificulta a exploração. Você perde mais energia.");
            jogador.diminuirEnergia(5.0 * getDificuldadeExploracao() * 1.25);
            jogador.diminuirSede(4.0 * getDificuldadeExploracao() * 1.25);
            jogador.diminuirFome(4.0 * getDificuldadeExploracao() * 1.25);
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


    // Getter
    public Boolean isTerrenoLamacento() {
        return terrenoLamacento;
    }
}
