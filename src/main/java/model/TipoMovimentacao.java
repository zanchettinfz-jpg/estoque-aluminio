package model;

import java.util.List;
import java.util.Set;

public enum TipoMovimentacao {
    ENTRADA("Entrada", false),
    SAIDA("Saida", true),
    DEVOLUCAO("Devolucao", false),
    PERDA("Perda", true),
    DESCARTE("Descarte", true),
    TRANSFERENCIA("Transferencia", true);

    private static final Set<String> TIPOS_SAIDA = Set.of("Saida", "Perda", "Descarte", "Transferencia");

    private final String descricao;
    private final boolean exigeDestino;

    TipoMovimentacao(String descricao, boolean exigeDestino) {
        this.descricao = descricao;
        this.exigeDestino = exigeDestino;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isExigeDestino() {
        return exigeDestino;
    }

    public static List<String> descricoes() {
        return List.of("Entrada", "Saida", "Devolucao", "Perda", "Descarte", "Transferencia");
    }

    public static boolean exigeDestino(String tipo) {
        return TIPOS_SAIDA.contains(tipo);
    }

    public static boolean existe(String tipo) {
        return descricoes().contains(tipo);
    }
}
