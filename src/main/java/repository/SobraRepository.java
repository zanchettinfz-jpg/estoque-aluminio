package repository;

import model.Sobra;
import util.ConexaoSQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SobraRepository {

    public List<Sobra> findAll(String termo) {
        List<Sobra> sobras = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT s.*, p.codigo produto_codigo, p.descricao produto_descricao
                FROM sobras s
                LEFT JOIN produtos p ON p.id = s.produto_id
                WHERE 1 = 1
                """);
        if (termo != null && !termo.isBlank()) {
            sql.append("""
                     AND (lower(p.codigo) LIKE ? OR lower(p.descricao) LIKE ?
                          OR lower(s.localizacao) LIKE ? OR lower(s.status) LIKE ?)
                    """);
        }
        sql.append(" ORDER BY s.id DESC");
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            if (termo != null && !termo.isBlank()) {
                String like = "%" + termo.toLowerCase() + "%";
                for (int i = 1; i <= 4; i++) {
                    statement.setString(i, like);
                }
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    sobras.add(map(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao listar sobras", exception);
        }
        return sobras;
    }

    public void save(Sobra sobra) {
        String sql = """
                INSERT INTO sobras (produto_id, tamanho_original, tamanho_sobra, localizacao, status)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, sobra.getProdutoId());
            statement.setDouble(2, sobra.getTamanhoOriginal());
            statement.setDouble(3, sobra.getTamanhoSobra());
            statement.setString(4, sobra.getLocalizacao());
            statement.setString(5, sobra.getStatus());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao salvar sobra", exception);
        }
    }

    public void updateStatus(int id, String status) {
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE sobras SET status = ? WHERE id = ?")) {
            statement.setString(1, status);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao atualizar sobra", exception);
        }
    }

    public int countDisponiveis() {
        String sql = "SELECT COUNT(*) FROM sobras WHERE status = 'Disponivel'";
        try (Connection connection = ConexaoSQLite.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao contar sobras", exception);
        }
    }

    private Sobra map(ResultSet resultSet) throws SQLException {
        Sobra sobra = new Sobra();
        sobra.setId(resultSet.getInt("id"));
        sobra.setProdutoId(resultSet.getInt("produto_id"));
        sobra.setProdutoCodigo(resultSet.getString("produto_codigo"));
        sobra.setProdutoDescricao(resultSet.getString("produto_descricao"));
        sobra.setTamanhoOriginal(resultSet.getDouble("tamanho_original"));
        sobra.setTamanhoSobra(resultSet.getDouble("tamanho_sobra"));
        sobra.setLocalizacao(resultSet.getString("localizacao"));
        sobra.setStatus(resultSet.getString("status"));
        return sobra;
    }
}
