package service;

import model.Usuario;

public class SessaoUsuario {
    private static Usuario usuarioLogado;

    private SessaoUsuario() {
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void iniciar(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static void encerrar() {
        usuarioLogado = null;
    }
}
