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
                new Materiais("Martelo de Pedra", null, 2.5, 0.7, 0.3, 15.0, TipoMaterial.METAL)
        ));

        receitas.add(new Receita(
                TipoMaterial.MADEIRA,
                TipoMaterial.METAL,
                new Materiais("Lança de Metal", null, 3.0, 0.9, 0.2, 25.0, TipoMaterial.METAL)
        ));

        receitas.add(new Receita(
                TipoMaterial.PEDRA,
                TipoMaterial.METAL,
                new Materiais("Machado de Batalha", null, 4.0, 0.85, 0.15, 30.0, TipoMaterial.METAL)
        ));

        receitas.add(new Receita(
                TipoMaterial.METAL,
                TipoMaterial.METAL,
                new Materiais("Placa Metálica", null, 2.0, 1.0, 0.1, 40.0, TipoMaterial.METAL)
        ));

        receitas.add(new Receita(
                TipoMaterial.MADEIRA,
                TipoMaterial.MADEIRA,
                new Materiais("Tábua Reforçada", null, 1.5, 1.0, 0.25, 10.0, TipoMaterial.MADEIRA)
        ));

        receitas.add(new Receita(
                TipoMaterial.PEDRA,
                TipoMaterial.PEDRA,
                new Materiais("Bloco de Pedra", null, 3.5, 1.0, 0.2, 20.0, TipoMaterial.PEDRA)
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
