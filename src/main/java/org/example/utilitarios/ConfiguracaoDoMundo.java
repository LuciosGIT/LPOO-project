package org.example.utilitarios;

import org.example.ambientes.*;
import org.example.criatura.*;
import org.example.domain.Ambiente;
import org.example.domain.Criatura;
import org.example.domain.Evento;
import org.example.eventos.EventoClimatico;
import org.example.eventos.EventoCriatura;
import org.example.eventos.EventoDescoberta;
import org.example.eventos.EventoDoencaFerimento;

import java.util.List;

import static org.example.enums.TipoClimatico.*;

public class ConfiguracaoDoMundo {

    //lista de animais
    public static List<Criatura> getCriaturasPadrao() {
        return List.of(
                new Cobra("Cobra Venenosa", 100.0, 4.0, 7.5),
                new Morcego("Morcego Espantoso", 100.0, 2.0, 5.0),
                new Lobo("Lobo Nômade", 100.0, 4.5, 0.5),
                new Corvo("Corvo Atordoante", 100.0, 2.0, 5.0),
                new Crocodilo("Crocodilo Feroz", 100.0, 4.0, 0.5),
                new Urso("Urso Raivoso", 100.0, 5.0, 0.9)
        );
    }

    // Lista de eventos climáticos padrão
    public static List<EventoClimatico> getEventosClimaticosPadrao() {

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

    public static List<Evento> getEventosDoJogo() {
        return List.of(new EventoCriatura(), new EventoClimatico(), new EventoDescoberta(), new EventoDoencaFerimento());
    }

    public static List<Ambiente> getAmbientesDoJogo() {
        return List.of(new AmbienteFloresta(), new AmbienteCaverna(), new AmbienteRuinas()
                , new AmbienteLagoRio(), new AmbienteMontanha());
    }

}