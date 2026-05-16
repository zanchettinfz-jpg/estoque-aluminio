package repository;

import model.Usuario;
import util.ConexaoSQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {

    public Optional<Usuario> findByLogin(String login) {
        String sql = "SELECT * FROM usuarios WHERE login = ?";
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(map(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao localizar usuario", exception);
        }
        return Optional.empty();
    }

    public Usuario save(Usuario usuario) {
        String sql = """
                INSERT INTO usuarios (nome, login, senha, nivel, status)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getLogin());
            statement.setString(3, usuario.getSenha());
            statement.setString(4, usuario.getNivel());
            statement.setString(5, usuario.getStatus());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.setId(keys.getInt(1));
                }
            }
            return usuario;
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao cadastrar usuario", exception);
        }
    }

    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY status, nome";
        try (Connection connection = ConexaoSQLite.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                usuarios.add(map(resultSet));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao listar usuarios", exception);
        }
        return usuarios;
    }

    public void atualizarStatusNivel(int usuarioId, String status, String nivel, int aprovadoPor) {
        String sql = "UPDATE usuarios SET status = ?, nivel = ?, aprovado_por = ? WHERE id = ?";
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setString(2, nivel);
            statement.setInt(3, aprovadoPor);
            statement.setInt(4, usuarioId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao atualizar usuario", exception);
        }
    }

    public void atualizarSenha(int usuarioId, String senhaHash) {
        String sql = "UPDATE usuarios SET senha = ? WHERE id = ?";
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, senhaHash);
            statement.setInt(2, usuarioId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao atualizar senha", exception);
        }
    }

    public void registrarTentativaLogin(String login, boolean sucesso, String motivo) {
        String sql = "INSERT INTO tentativas_login (login, sucesso, motivo) VALUES (?, ?, ?)";
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setInt(2, sucesso ? 1 : 0);
            statement.setString(3, motivo);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao registrar tentativa de login", exception);
        }
    }

    public void registrarAuditoria(Integer usuarioId, String acao, String detalhes) {
        String sql = "INSERT INTO auditoria_acoes (usuario_id, acao, detalhes) VALUES (?, ?, ?)";
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (usuarioId == null) {
                statement.setNull(1, java.sql.Types.INTEGER);
            } else {
                statement.setInt(1, usuarioId);
            }
            statement.setString(2, acao);
            statement.setString(3, detalhes);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao registrar auditoria", exception);
        }
    }

    private Usuario map(ResultSet resultSet) throws SQLException {
        return new Usuario(
                resultSet.getInt("id"),
                resultSet.getString("nome"),
                resultSet.getString("login"),
                resultSet.getString("senha"),
                resultSet.getString("nivel"),
                resultSet.getString("status"),
                resultSet.getString("criado_em"),
                (Integer) resultSet.getObject("aprovado_por")
        );
    }
}
