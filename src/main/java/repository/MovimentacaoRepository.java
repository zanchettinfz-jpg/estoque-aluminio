package repository;

import model.Movimentacao;
import util.ConexaoSQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoRepository {

    public void save(Connection connection, Movimentacao movimentacao) throws SQLException {
        String sql = """
                INSERT INTO movimentacoes (produto_id, tipo, quantidade, observacao, usuario_id)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, movimentacao.getProdutoId());
            statement.setString(2, movimentacao.getTipo());
            statement.setInt(3, movimentacao.getQuantidade());
            statement.setString(4, movimentacao.getObservacao());
            statement.setInt(5, movimentacao.getUsuarioId());
            statement.executeUpdate();
        }
    }

    public List<Movimentacao> findAll(String produto, String tipo, LocalDate data) {
        List<Movimentacao> movimentacoes = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT m.*, p.codigo produto_codigo, p.descricao produto_descricao, u.nome usuario_nome
                FROM movimentacoes m
                LEFT JOIN produtos p ON p.id = m.produto_id
                LEFT JOIN usuarios u ON u.id = m.usuario_id
                WHERE 1 = 1
                """);
        List<Object> params = new ArrayList<>();
        if (produto != null && !produto.isBlank()) {
            sql.append(" AND (lower(p.codigo) LIKE ? OR lower(p.descricao) LIKE ?)");
            String like = "%" + produto.toLowerCase() + "%";
            params.add(like);
            params.add(like);
        }
        if (tipo != null && !tipo.isBlank() && !"Todos".equals(tipo)) {
            sql.append(" AND m.tipo = ?");
            params.add(tipo);
        }
        if (data != null) {
            sql.append(" AND date(m.data_movimentacao) = date(?)");
            params.add(data.toString());
        }
        sql.append(" ORDER BY m.data_movimentacao DESC");

        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    movimentacoes.add(map(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao consultar movimentacoes", exception);
        }
        return movimentacoes;
    }

    public List<Movimentacao> findLast(int limit) {
        String sql = """
                SELECT m.*, p.codigo produto_codigo, p.descricao produto_descricao, u.nome usuario_nome
                FROM movimentacoes m
                LEFT JOIN produtos p ON p.id = m.produto_id
                LEFT JOIN usuarios u ON u.id = m.usuario_id
                ORDER BY m.data_movimentacao DESC
                LIMIT ?
                """;
        List<Movimentacao> movimentacoes = new ArrayList<>();
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    movimentacoes.add(map(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao carregar ultimas movimentacoes", exception);
        }
        return movimentacoes;
    }

    public int countToday() {
        String sql = "SELECT COUNT(*) FROM movimentacoes WHERE date(data_movimentacao) = date('now', 'localtime')";
        try (Connection connection = ConexaoSQLite.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao consultar movimentacoes do dia", exception);
        }
    }

    public int countByTipoLike(String tipo) {
        String sql = "SELECT COALESCE(SUM(quantidade), 0) FROM movimentacoes WHERE tipo LIKE ?";
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tipo + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao consultar grafico", exception);
        }
    }

    private Movimentacao map(ResultSet resultSet) throws SQLException {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(resultSet.getInt("id"));
        movimentacao.setProdutoId(resultSet.getInt("produto_id"));
        movimentacao.setProdutoCodigo(resultSet.getString("produto_codigo"));
        movimentacao.setProdutoDescricao(resultSet.getString("produto_descricao"));
        movimentacao.setTipo(resultSet.getString("tipo"));
        movimentacao.setQuantidade(resultSet.getInt("quantidade"));
        movimentacao.setObservacao(resultSet.getString("observacao"));
        Timestamp timestamp = resultSet.getTimestamp("data_movimentacao");
        if (timestamp != null) {
            movimentacao.setDataMovimentacao(timestamp.toLocalDateTime());
        }
        movimentacao.setUsuarioId(resultSet.getInt("usuario_id"));
        movimentacao.setUsuarioNome(resultSet.getString("usuario_nome"));
        return movimentacao;
    }
}
