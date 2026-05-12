package repository;

import model.Usuario;
import util.ConexaoSQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsuarioRepository {

    public Optional<Usuario> findByLoginAndSenha(String login, String senha) {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, senha);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Usuario(
                            resultSet.getInt("id"),
                            resultSet.getString("nome"),
                            resultSet.getString("login"),
                            resultSet.getString("senha"),
                            resultSet.getString("nivel")
                    ));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao autenticar usuario", exception);
        }
        return Optional.empty();
    }
}
