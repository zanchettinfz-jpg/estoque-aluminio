package service;

import model.Usuario;
import repository.UsuarioRepository;

public class LoginService {
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();

    public Usuario autenticar(String login, String senha) {
        if (login == null || login.isBlank() || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Informe login e senha.");
        }
        Usuario usuario = usuarioRepository.findByLoginAndSenha(login, senha)
                .orElseThrow(() -> new IllegalArgumentException("Login ou senha invalidos."));
        SessaoUsuario.iniciar(usuario);
        return usuario;
    }

    public void logout() {
        SessaoUsuario.encerrar();
    }
}
