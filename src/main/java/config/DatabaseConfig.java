package config;

import util.ConexaoSQLite;

import java.sql.Connection;
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
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS usuarios (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nome TEXT,
                        login TEXT UNIQUE,
                        senha TEXT,
                        nivel TEXT
                    )
                    """);
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS movimentacoes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        produto_id INTEGER,
                        tipo TEXT,
                        quantidade INTEGER,
                        observacao TEXT,
                        data_movimentacao DATETIME DEFAULT CURRENT_TIMESTAMP,
                        usuario_id INTEGER,
                        FOREIGN KEY(produto_id) REFERENCES produtos(id),
                        FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
                    )
                    """);
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
                    INSERT OR IGNORE INTO usuarios (id, nome, login, senha, nivel)
                    VALUES (1, 'Administrador', 'admin', 'admin', 'administrador')
                    """);
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao inicializar banco de dados", exception);
        }
    }
}
