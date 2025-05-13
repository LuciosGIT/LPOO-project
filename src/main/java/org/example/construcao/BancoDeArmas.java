package org.example.construcao;

import org.example.domain.Personagem;
import org.example.enums.TipoArma;
import org.example.enums.TipoMaterial;
import org.example.itens.Armas;

import java.util.ArrayList;
import java.util.List;

public class BancoDeArmas  {

    private static final List<ReceitaArma> receitasDeArmas = new ArrayList<>();

    static {
        receitasDeArmas.add(new ReceitaArma(
                TipoMaterial.MADEIRA,
                TipoMaterial.METAL,
                new Armas("Arco Reforçado", 15.0, null, 2.0, 80.0, 0.3, 10.0, TipoArma.DISTANCIA)
        ));

        receitasDeArmas.add(new ReceitaArma(
                TipoMaterial.PEDRA,
                TipoMaterial.METAL,
                new Armas("Espada de Pedra", 20.0, null, 3.0, 90.0, 0.2, 1.5, TipoArma.CORPO)
        ));

        receitasDeArmas.add(new ReceitaArma(
                TipoMaterial.MADEIRA,
                TipoMaterial.PEDRA,
                new Armas("Clava com Pedra", 12.0, null, 2.5, 70.0, 0.25, 1.0, TipoArma.CORPO)
        ));

        receitasDeArmas.add(new ReceitaArma(
                TipoMaterial.METAL,
                TipoMaterial.METAL,
                new Armas("Lança de Guerra", 30.0, null, 4.0, 95.0, 0.15, 1.8, TipoArma.DISTANCIA)
        ));

        receitasDeArmas.add(new ReceitaArma(
                TipoMaterial.MADEIRA,
                TipoMaterial.MADEIRA,
                new Armas("Bastão Duplo", 10.0, null, 2.0, 75.0, 0.3, 1.2, TipoArma.CORPO)
        ));

        receitasDeArmas.add(new ReceitaArma(
                TipoMaterial.PEDRA,
                TipoMaterial.PEDRA,
                new Armas("Machado de Pedra", 25.0, null, 3.5, 85.0, 0.2, 1.5, TipoArma.CORPO)
        ));
    }

    public static Armas buscarArma(TipoMaterial m1, TipoMaterial m2, Personagem personagem) {
        for (ReceitaArma r : receitasDeArmas) {
            if (r.combinaCom(m1, m2)) {
                return r.getResultado().copiarPara(personagem);
            }
        }
        return null;
    }
}