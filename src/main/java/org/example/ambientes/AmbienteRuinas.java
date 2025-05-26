package org.example.ambientes;

import org.example.domain.Ambiente;
import org.example.domain.Evento;
import org.example.domain.Personagem;
import org.example.enums.*;
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

    private boolean estruturasInstaveis;

    public AmbienteRuinas(String nome, String descricao, Double dificuldadeExploracao,
                          List<TipoClimatico> condicoesClimaticas, boolean estruturasInstaveis,
                          Personagem player) {
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

        // Evento
        this.adicionarEvento(new EventoCriatura(true, "Batalha", "Evento de Criatura", 0.5,
                getCriaturasAmbientes().get(0), getCriaturasAmbientes().get(0).getNivelDePerigo()));
    }

    public AmbienteRuinas() {}

    @Override
    public void explorar(Personagem jogador) {
        if (this.estruturasInstaveis) {
            System.out.println("As estruturas instáveis dificultam a exploração. Você perde mais energia.");
            jogador.diminuirEnergia(4.0 * getDificuldadeExploracao() * 1.25);
            jogador.diminuirSede(1.0 * getDificuldadeExploracao() * 1.25);
            jogador.diminuirFome(1.0 * getDificuldadeExploracao() * 1.25);
        } else {
            System.out.println("A exploração é relativamente tranquila.");
            jogador.diminuirEnergia(4.0 * getDificuldadeExploracao());
            jogador.diminuirSede(1.0 * getDificuldadeExploracao());
            jogador.diminuirFome(1.0 * getDificuldadeExploracao());
        }

        // Lógica da armadilha incorporada aqui
        if (Utilitario.getBooleanAleatorio()) {
            System.out.println("Uma armadilha foi acionada nas ruínas!");
            jogador.diminuirVida(25.0);
            jogador.diminuirEnergia(15.0);
            jogador.diminuirSanidade(22.0);
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
