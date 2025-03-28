package org.example.utilitarios;

import org.example.criatura.Cobra;
import org.example.criatura.Corvo;
import org.example.criatura.Lobo;
import org.example.criatura.Urso;
import org.example.domain.Criatura;
import org.example.domain.Evento;
import org.example.eventos.EventoClimatico;

import java.util.List;

import static org.example.enums.TipoClimatico.*;

public class ConfiguracaoDoMundo {

    //lista de animais
    public static List<Criatura> getCriaturasPadrao() {
        return List.of(
                new Cobra("Cobra", 7.0, 3.0, 5.0),
                new Lobo("Lobo", 7.0, 3.0, 5.0),
                new Corvo("Corvo", 7.0, 3.0, 5.0),
                new Urso("Urso", 7.0, 3.0, 5.0)
        );
    }

    // Lista de eventos climáticos padrão
    public static List<Evento> getEventosClimaticosPadrao() {

         return List.of(
                new EventoClimatico(
                        true,
                        "Clima nevasca: o jogador terá mais dificuldade na exploração",
                        "sentirá mais fome, gasto de energia, e diminuição na locomoção",
                        "Nevasca",
                        0.4,
                        NEVASCA,
                        3,
                        "Redução de visibilidade, gasto extra de energia e fome"
                ),
                new EventoClimatico(
                        true,
                        "Clima calor extremo: o jogador terá mais dificuldade na exploração",
                        "mais gasto de energia, diminuição na locomoção e sede",
                        "Calor extremo",
                        0.4,
                        CALOR,
                        3,
                        "O personagem sentirá mais sede e dificuldade ao explorar"
                ),
                new EventoClimatico(
                        true,
                        "Clima tempestade: o jogador terá mais dificuldade na exploração",
                        "gasto de energia, e diminuição na locomoção",
                        "Tempestade",
                        0.4,
                        TEMPESTADE,
                        3,
                        "Redução de visibilidade, gasto extra de energia"
                )
        );
    }

}
