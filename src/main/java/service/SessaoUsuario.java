package service;

import model.Usuario;
import model.NivelUsuario;

public class SessaoUsuario {
    private static Usuario usuarioLogado;

    private SessaoUsuario() {
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean isAutenticado() {
        return usuarioLogado != null;
    }

    public static boolean isAdministrador() {
        return usuarioLogado != null && NivelUsuario.ADMINISTRADOR.name().equalsIgnoreCase(usuarioLogado.getNivel());
    }

    public static boolean isConsulta() {
        return usuarioLogado != null && NivelUsuario.CONSULTA.name().equalsIgnoreCase(usuarioLogado.getNivel());
    }

    public static void exigirLogin() {
        if (!isAutenticado()) {
            throw new IllegalStateException("E necessario estar logado para executar esta acao.");
        }
    }

    public static void exigirAdministrador() {
        exigirLogin();
        if (!isAdministrador()) {
            throw new IllegalStateException("Acesso restrito a administradores.");
        }
    }

    public static void exigirOperacao() {
        exigirLogin();
        if (isConsulta()) {
            throw new IllegalStateException("Usuario de consulta pode apenas visualizar estoque.");
        }
    }

    public static void iniciar(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static void encerrar() {
        usuarioLogado = null;
    }
}
