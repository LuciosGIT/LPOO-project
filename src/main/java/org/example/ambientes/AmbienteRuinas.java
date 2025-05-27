package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.enums.*;
import org.example.eventos.EventoClimatico;
import org.example.eventos.EventoCriatura;
import org.example.gerenciadores.GerenciadorDeEventos;
import org.example.itens.Alimentos;
import org.example.itens.Ferramentas;
import org.example.itens.Materiais;
import org.example.utilitarios.ConfiguracaoDoMundo;
import org.example.utilitarios.Utilitario;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

public class AmbienteRuinas extends Ambiente {

    private boolean estruturasInstaveis;

    public AmbienteRuinas(String nome, String descricao, Double dificuldadeExploracao, List<TipoClimatico> condicoesClimaticas, boolean estruturasInstaveis, Personagem player) {
        super(nome, descricao, dificuldadeExploracao, condicoesClimaticas);
        this.estruturasInstaveis = estruturasInstaveis;

        // Recursos
        this.adicionarRecurso(new Alimentos("Carne", player, 1.0, 8.0, 0.5, TipoAlimento.CARNE, OffsetDateTime.now().plusDays(10)));
        this.adicionarRecurso(new Alimentos("Raíz", player, 0.3, 12.0, 0.3, TipoAlimento.RAIZES, OffsetDateTime.now().plusDays(12)));
        this.adicionarRecurso(new Alimentos("Sardinha enlatada", player, 0.5, 5.0, 0.4, TipoAlimento.ENLATADO, OffsetDateTime.now().plusDays(5)));
        this.adicionarRecurso(new Alimentos("Atum em lata", player, 0.5, 5.0, 0.4, TipoAlimento.ENLATADO, OffsetDateTime.now().minusDays(5)));

        this.adicionarRecurso(new Materiais("Pedra", player, 8.0, 20.0, 0.9, 10.0, TipoMaterial.PEDRA));
        this.adicionarRecurso(new Materiais("Madeira", player, 2.0, 20.0, 0.8, 5.0, TipoMaterial.MADEIRA));

        this.adicionarRecurso(new Ferramentas("Machado largado", player, 2.0, 20.0, 0.8, 5.0, TipoFerramenta.MACHADO));
        this.adicionarRecurso(new Ferramentas("Faca Esquecida", player, 2.0, 20.0, 0.8, 5.0, TipoFerramenta.FACA));
        this.adicionarRecurso(new Ferramentas("Chave Real", player, 0.8, 100.0, 0.3, 0.8, TipoFerramenta.CHAVE_ESPECIAL));

        // Evento
        this.adicionarEvento(new EventoClimatico(true, "Evento de Calor extremo acionado", "Afeta a visibilidade",
                "Evento de Calor", 0.7, TipoClimatico.CALOR, 5,
                "O calor reduz a visibilidade e aumenta a temperatura, dificultando a exploração e aumentando o consumo de energia."));
    }

    public AmbienteRuinas() {}

    @Override
    public void explorar(Personagem jogador) {
        if (this.estruturasInstaveis) {
            System.out.println("As estruturas instáveis dificultam a exploração. Você perde mais energia.");
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

    public boolean isEstruturasInstaveis() {
        return estruturasInstaveis;
    }
}
