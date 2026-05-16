package repository;

import model.Produto;
import util.ConexaoSQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoRepository {

    public List<Produto> findAll() {
        return query("""
                SELECT * FROM produtos
                ORDER BY descricao
                """);
    }

    public List<Produto> search(String termo) {
        String like = "%" + termo.toLowerCase() + "%";
        List<Produto> produtos = new ArrayList<>();
        String sql = """
                SELECT * FROM produtos
                WHERE lower(codigo) LIKE ?
                   OR lower(descricao) LIKE ?
                   OR lower(localizacao) LIKE ?
                   OR lower(linha) LIKE ?
                   OR lower(cor) LIKE ?
                ORDER BY descricao
                """;
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                statement.setString(i, like);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    produtos.add(map(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao pesquisar produtos", exception);
        }
        return produtos;
    }

    public Optional<Produto> findById(int id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(map(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao localizar produto", exception);
        }
        return Optional.empty();
    }

    public boolean existsByCodigo(String codigo, int ignoredId) {
        String sql = "SELECT id FROM produtos WHERE codigo = ? AND id <> ?";
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codigo);
            statement.setInt(2, ignoredId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao verificar codigo do produto", exception);
        }
    }

    public Produto save(Produto produto) {
        if (produto.getId() == 0) {
            return insert(produto);
        }
        update(produto);
        return produto;
    }

    public void delete(int id) {
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM produtos WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao excluir produto", exception);
        }
    }

    public boolean hasMovimentacoes(int produtoId) {
        String sql = "SELECT 1 FROM movimentacoes WHERE produto_id = ? LIMIT 1";
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, produtoId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao verificar historico do produto", exception);
        }
    }

    public void updateQuantidade(Connection connection, int produtoId, int novaQuantidade) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE produtos SET quantidade = ? WHERE id = ?")) {
            statement.setInt(1, novaQuantidade);
            statement.setInt(2, produtoId);
            statement.executeUpdate();
        }
    }

    public int count() {
        return scalarInt("SELECT COUNT(*) FROM produtos");
    }

    public int sumQuantidade() {
        return scalarInt("SELECT COALESCE(SUM(quantidade), 0) FROM produtos");
    }

    public List<String> ocupacaoLocalizacoes() {
        List<String> ocupacao = new ArrayList<>();
        String sql = """
                SELECT localizacao, COUNT(*) total, SUM(quantidade) quantidade
                FROM produtos
                WHERE localizacao IS NOT NULL AND trim(localizacao) <> ''
                GROUP BY localizacao
                ORDER BY localizacao
                """;
        try (Connection connection = ConexaoSQLite.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                ocupacao.add(resultSet.getString("localizacao") + " - " + resultSet.getInt("total")
                        + " itens / " + resultSet.getInt("quantidade") + " un.");
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao carregar ocupacao", exception);
        }
        return ocupacao;
    }

    private Produto insert(Produto produto) {
        String sql = """
                INSERT INTO produtos (codigo, descricao, cor, linha, tamanho, quantidade, unidade, localizacao, observacoes)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(statement, produto);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    produto.setId(keys.getInt(1));
                }
            }
            return produto;
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao salvar produto", exception);
        }
    }

    private void update(Produto produto) {
        String sql = """
                UPDATE produtos
                SET codigo = ?, descricao = ?, cor = ?, linha = ?, tamanho = ?,
                    unidade = ?, localizacao = ?, observacoes = ?
                WHERE id = ?
                """;
        try (Connection connection = ConexaoSQLite.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, produto.getCodigo());
            statement.setString(2, produto.getDescricao());
            statement.setString(3, produto.getCor());
            statement.setString(4, produto.getLinha());
            statement.setDouble(5, produto.getTamanho());
            statement.setString(6, produto.getUnidade());
            statement.setString(7, produto.getLocalizacao());
            statement.setString(8, produto.getObservacoes());
            statement.setInt(9, produto.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao atualizar produto", exception);
        }
    }

    private List<Produto> query(String sql) {
        List<Produto> produtos = new ArrayList<>();
        try (Connection connection = ConexaoSQLite.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                produtos.add(map(resultSet));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao listar produtos", exception);
        }
        return produtos;
    }

    private int scalarInt(String sql) {
        try (Connection connection = ConexaoSQLite.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao consultar indicador", exception);
        }
    }

    private void fillStatement(PreparedStatement statement, Produto produto) throws SQLException {
        statement.setString(1, produto.getCodigo());
        statement.setString(2, produto.getDescricao());
        statement.setString(3, produto.getCor());
        statement.setString(4, produto.getLinha());
        statement.setDouble(5, produto.getTamanho());
        statement.setInt(6, produto.getQuantidade());
        statement.setString(7, produto.getUnidade());
        statement.setString(8, produto.getLocalizacao());
        statement.setString(9, produto.getObservacoes());
    }

    private Produto map(ResultSet resultSet) throws SQLException {
        return new Produto(
                resultSet.getInt("id"),
                resultSet.getString("codigo"),
                resultSet.getString("descricao"),
                resultSet.getString("cor"),
                resultSet.getString("linha"),
                resultSet.getDouble("tamanho"),
                resultSet.getInt("quantidade"),
                resultSet.getString("unidade"),
                resultSet.getString("localizacao"),
                resultSet.getString("observacoes")
        );
    }
}
