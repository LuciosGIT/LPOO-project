package org.example.enums;

public enum TipoEfeito {

    CURAR(true),
    ALIVIAR(true),
    TRATAR(true),
    Envenedado(true);

    private boolean efeitoAtivo;

   private TipoEfeito(boolean efeitoAtivo){
        this.efeitoAtivo = efeitoAtivo;
   }


}
