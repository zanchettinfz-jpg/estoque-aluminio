package util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoSQLite {

    private static final Path DATABASE_PATH = Path.of("src", "main", "resources", "database", "estoque.db");
    private static final String URL = "jdbc:sqlite:" + DATABASE_PATH;

    private ConexaoSQLite() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Files.createDirectories(DATABASE_PATH.getParent());
        } catch (Exception exception) {
            throw new SQLException("Nao foi possivel criar o diretorio do banco de dados", exception);
        }
        return DriverManager.getConnection(URL);
    }
}
