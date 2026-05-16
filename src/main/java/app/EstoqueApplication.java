package app;

import config.DatabaseConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.SessaoUsuario;

import java.io.IOException;

public class EstoqueApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        DatabaseConfig.initializeDatabase();
        showLogin();
    }

    public static void showLogin() throws IOException {
        loadScene("/view/login.fxml", "Estoque Aluminio - Login", 980, 640);
    }

    public static void showDashboard() throws IOException {
        loadScene("/view/dashboard.fxml", "Estoque Aluminio - ERP Industrial", 1320, 820);
    }

    public static void showCadastro() throws IOException {
        loadScene("/view/cadastro.fxml", "Estoque Aluminio - Criar Conta", 980, 640);
    }

    public static void loadScene(String fxmlPath, String title, double width, double height) throws IOException {
        if (!isTelaPublica(fxmlPath) && !SessaoUsuario.isAutenticado()) {
            fxmlPath = "/view/login.fxml";
            title = "Estoque Aluminio - Login";
            width = 980;
            height = 640;
        }
        FXMLLoader loader = new FXMLLoader(EstoqueApplication.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load(), width, height);
        scene.getStylesheets().add(EstoqueApplication.class.getResource("/css/style.css").toExternalForm());
        primaryStage.setTitle(title);
        primaryStage.setMinWidth(Math.min(width, 1100));
        primaryStage.setMinHeight(Math.min(height, 700));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static boolean isTelaPublica(String fxmlPath) {
        return "/view/login.fxml".equals(fxmlPath) || "/view/cadastro.fxml".equals(fxmlPath);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
