package service;

import model.Usuario;
import model.NivelUsuario;
import model.StatusUsuario;
import repository.UsuarioRepository;
import util.SenhaUtil;

import java.util.List;

public class LoginService {
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();

    public Usuario autenticar(String login, String senha) {
        if (login == null || login.isBlank() || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Informe login e senha.");
        }
        String loginNormalizado = login.trim();
        Usuario usuario = usuarioRepository.findByLogin(loginNormalizado)
                .orElseThrow(() -> falha(login, "Login ou senha invalidos."));
        if (!SenhaUtil.conferir(senha, usuario.getSenha())) {
            throw falha(login, "Login ou senha invalidos.");
        }
        if (!SenhaUtil.pareceBCrypt(usuario.getSenha())) {
            usuarioRepository.atualizarSenha(usuario.getId(), SenhaUtil.gerarHash(senha));
        }
        if ("PENDENTE".equalsIgnoreCase(usuario.getStatus())) {
            usuarioRepository.registrarTentativaLogin(loginNormalizado, false, "Conta pendente");
            throw new IllegalArgumentException("Sua conta ainda esta pendente de aprovacao.");
        }
        if ("BLOQUEADO".equalsIgnoreCase(usuario.getStatus())) {
            usuarioRepository.registrarTentativaLogin(loginNormalizado, false, "Conta bloqueada");
            throw new IllegalArgumentException("Sua conta esta bloqueada. Procure o administrador.");
        }
        SessaoUsuario.iniciar(usuario);
        usuarioRepository.registrarTentativaLogin(loginNormalizado, true, "Acesso autorizado");
        return usuario;
    }

    public void cadastrar(String nome, String login, String senha, String confirmarSenha) {
        if (isBlank(nome) || isBlank(login) || isBlank(senha) || isBlank(confirmarSenha)) {
            throw new IllegalArgumentException("Preencha todos os campos.");
        }
        if (!senha.equals(confirmarSenha)) {
            throw new IllegalArgumentException("As senhas nao conferem.");
        }
        if (senha.length() < 4) {
            throw new IllegalArgumentException("A senha deve possuir pelo menos 4 caracteres.");
        }
        String loginNormalizado = login.trim();
        if (usuarioRepository.findByLogin(loginNormalizado).isPresent()) {
            throw new IllegalArgumentException("Este login ja esta em uso.");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(nome.trim());
        usuario.setLogin(loginNormalizado);
        usuario.setSenha(SenhaUtil.gerarHash(senha));
        usuario.setNivel("OPERADOR");
        usuario.setStatus("PENDENTE");
        usuarioRepository.save(usuario);
        usuarioRepository.registrarAuditoria(null, "CADASTRO_SOLICITADO", "Login: " + usuario.getLogin());
    }

    public List<Usuario> listarUsuarios() {
        SessaoUsuario.exigirAdministrador();
        return usuarioRepository.findAll();
    }

    public void atualizarUsuario(Usuario usuario, String status, String nivel) {
        SessaoUsuario.exigirAdministrador();
        if (usuario == null || usuario.getId() == 0) {
            throw new IllegalArgumentException("Selecione um usuario.");
        }
        validarNivelStatus(nivel, status);
        if (usuario.getId() == SessaoUsuario.getUsuarioLogado().getId()
                && (!StatusUsuario.ATIVO.name().equals(status) || !NivelUsuario.ADMINISTRADOR.name().equals(nivel))) {
            throw new IllegalArgumentException("O administrador logado nao pode remover o proprio acesso administrativo.");
        }
        usuarioRepository.atualizarStatusNivel(usuario.getId(), status, nivel, SessaoUsuario.getUsuarioLogado().getId());
        usuarioRepository.registrarAuditoria(
                SessaoUsuario.getUsuarioLogado().getId(),
                "USUARIO_ATUALIZADO",
                usuario.getLogin() + " -> " + status + " / " + nivel
        );
    }

    public void logout() {
        SessaoUsuario.encerrar();
    }

    private IllegalArgumentException falha(String login, String mensagem) {
        usuarioRepository.registrarTentativaLogin(login, false, mensagem);
        return new IllegalArgumentException(mensagem);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private void validarNivelStatus(String nivel, String status) {
        try {
            NivelUsuario.valueOf(nivel);
            StatusUsuario.valueOf(status);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("Nivel ou status invalido.");
        }
    }
}
