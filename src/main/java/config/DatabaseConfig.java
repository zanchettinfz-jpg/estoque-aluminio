package config;

import util.ConexaoSQLite;
import util.SenhaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {

    private DatabaseConfig() {
    }

    public static void initializeDatabase() {
        try (Connection connection = ConexaoSQLite.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS produtos (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        codigo TEXT NOT NULL UNIQUE,
                        descricao TEXT NOT NULL,
                        cor TEXT,
                        linha TEXT,
                        tamanho REAL,
                        quantidade INTEGER DEFAULT 0,
                        unidade TEXT,
                        localizacao TEXT,
                        observacoes TEXT
                    )
                    """);
            statement.execute("CREATE INDEX IF NOT EXISTS idx_produtos_descricao ON produtos(descricao)");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_produtos_codigo ON produtos(codigo)");
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS usuarios (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nome TEXT NOT NULL,
                        login TEXT NOT NULL UNIQUE,
                        senha TEXT NOT NULL,
                        nivel TEXT DEFAULT 'OPERADOR',
                        status TEXT DEFAULT 'PENDENTE',
                        criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,
                        aprovado_por INTEGER,
                        FOREIGN KEY(aprovado_por) REFERENCES usuarios(id)
                    )
                    """);
            addColumnIfMissing(connection, "usuarios", "status", "TEXT DEFAULT 'PENDENTE'");
            addColumnIfMissing(connection, "usuarios", "criado_em", "DATETIME");
            addColumnIfMissing(connection, "usuarios", "aprovado_por", "INTEGER");
            statement.executeUpdate("UPDATE usuarios SET status = 'ATIVO' WHERE status IS NULL");
            statement.executeUpdate("UPDATE usuarios SET nivel = 'OPERADOR' WHERE nivel IS NULL");
            statement.executeUpdate("UPDATE usuarios SET criado_em = CURRENT_TIMESTAMP WHERE criado_em IS NULL");
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS movimentacoes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        produto_id INTEGER NOT NULL,
                        usuario_id INTEGER NOT NULL,
                        tipo TEXT NOT NULL,
                        quantidade INTEGER NOT NULL,
                        destino TEXT,
                        observacao TEXT,
                        data_movimentacao DATETIME DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY(produto_id) REFERENCES produtos(id),
                        FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
                    )
                    """);
            addColumnIfMissing(connection, "movimentacoes", "destino", "TEXT");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_movimentacoes_data ON movimentacoes(data_movimentacao)");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_movimentacoes_produto ON movimentacoes(produto_id)");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_movimentacoes_usuario ON movimentacoes(usuario_id)");
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS sobras (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        produto_id INTEGER,
                        tamanho_original REAL,
                        tamanho_sobra REAL,
                        localizacao TEXT,
                        status TEXT,
                        FOREIGN KEY(produto_id) REFERENCES produtos(id)
                    )
                    """);
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS tentativas_login (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        login TEXT NOT NULL,
                        sucesso INTEGER NOT NULL,
                        motivo TEXT,
                        data_tentativa DATETIME DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS auditoria_acoes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        usuario_id INTEGER,
                        acao TEXT NOT NULL,
                        detalhes TEXT,
                        data_acao DATETIME DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
                    )
                    """);
            criarOuMigrarAdmin(connection);
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao inicializar banco de dados", exception);
        }
    }

    private static void addColumnIfMissing(Connection connection, String table, String column, String definition) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("PRAGMA table_info(" + table + ")")) {
            while (resultSet.next()) {
                if (column.equalsIgnoreCase(resultSet.getString("name"))) {
                    return;
                }
            }
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute("ALTER TABLE " + table + " ADD COLUMN " + column + " " + definition);
        }
    }

    private static void criarOuMigrarAdmin(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    INSERT OR IGNORE INTO usuarios (id, nome, login, senha, nivel, status)
                    VALUES (1, 'Administrador', 'admin', 'admin', 'ADMINISTRADOR', 'ATIVO')
                    """);
            statement.executeUpdate("""
                    UPDATE usuarios
                    SET nivel = 'ADMINISTRADOR', status = 'ATIVO'
                    WHERE id = 1
                    """);
        }

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT senha FROM usuarios WHERE id = 1")) {
            if (resultSet.next() && !SenhaUtil.pareceBCrypt(resultSet.getString("senha"))) {
                try (PreparedStatement update = connection.prepareStatement("UPDATE usuarios SET senha = ? WHERE id = 1")) {
                    update.setString(1, SenhaUtil.gerarHash("admin"));
                    update.executeUpdate();
                }
            }
        }
    }
}
