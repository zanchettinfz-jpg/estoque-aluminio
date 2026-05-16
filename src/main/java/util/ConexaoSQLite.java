package util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoSQLite {

    private static final Path LEGACY_DATABASE_PATH = Path.of("src", "main", "resources", "database", "estoque.db");
    private static final Path DATABASE_PATH = Path.of(System.getProperty("estoque.db.path", "data/estoque.db"));
    private static final String URL = "jdbc:sqlite:" + DATABASE_PATH;

    private ConexaoSQLite() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            if (DATABASE_PATH.getParent() != null) {
                Files.createDirectories(DATABASE_PATH.getParent());
            }
            migrarBancoLegadoSeNecessario();
        } catch (ClassNotFoundException exception) {
            throw new SQLException("Driver SQLite nao encontrado no classpath", exception);
        } catch (Exception exception) {
            throw new SQLException("Nao foi possivel criar o diretorio do banco de dados", exception);
        }
        Connection connection = DriverManager.getConnection(URL);
        configurarConexao(connection);
        return connection;
    }

    private static void migrarBancoLegadoSeNecessario() throws Exception {
        if (!Files.exists(DATABASE_PATH) && Files.exists(LEGACY_DATABASE_PATH)) {
            Files.copy(LEGACY_DATABASE_PATH, DATABASE_PATH);
        }
    }

    private static void configurarConexao(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute("PRAGMA busy_timeout = 5000");
            statement.execute("PRAGMA journal_mode = WAL");
        }
    }
}
