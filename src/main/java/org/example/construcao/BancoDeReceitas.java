package org.example.construcao;

import org.example.enums.TipoMaterial;
import org.example.itens.Materiais;

import java.util.ArrayList;
import java.util.List;

public class BancoDeReceitas {

    private static final List<Receita> receitas = new ArrayList<>();

    static {
        receitas.add(new Receita(
                TipoMaterial.MADEIRA,
                TipoMaterial.PEDRA,
                new Materiais("Martelo de Pedra", null, 2.5, 70.0, 0.3, 15.0, TipoMaterial.RESULTADO_PEDRA_MADEIRA)
        ));

        receitas.add(new Receita(
                TipoMaterial.MADEIRA,
                TipoMaterial.METAL,
                new Materiais("Barra de Metal", null, 3.0, 90.0, 0.2, 25.0, TipoMaterial.RESULTADO_MADEIRA_METAL)
        ));

        receitas.add(new Receita(
                TipoMaterial.PEDRA,
                TipoMaterial.METAL,
                new Materiais("Bastão de Pedra", null, 4.0, 85.0, 0.15, 30.0, TipoMaterial.RESULTADO_PEDRA_METAL)
        ));

        receitas.add(new Receita(
                TipoMaterial.METAL,
                TipoMaterial.METAL,
                new Materiais("Placa Metálica", null, 2.0, 100.0, 0.1, 40.0, TipoMaterial.RESULTADO_METAL_METAL)
        ));

        receitas.add(new Receita(
                TipoMaterial.MADEIRA,
                TipoMaterial.MADEIRA,
                new Materiais("Tábua Reforçada", null, 1.5, 100.0, 0.25, 10.0, TipoMaterial.RESULTADO_MADEIRA_MADEIRA)
        ));

        receitas.add(new Receita(
                TipoMaterial.PEDRA,
                TipoMaterial.PEDRA,
                new Materiais("Bloco de Pedra", null, 3.5, 100.0, 0.2, 20.0, TipoMaterial.RESULTADO_PEDRA_PEDRA)
        ));
    }

    public static Materiais buscarReceita(TipoMaterial m1, TipoMaterial m2) {
        for (Receita receita : receitas) {
            if (receita.combinaCom(m1, m2)) {
                return receita.getResultado();
            }
        }
        return null;
    }
}