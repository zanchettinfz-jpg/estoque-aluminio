package app;

import config.DatabaseConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    public static void loadScene(String fxmlPath, String title, double width, double height) throws IOException {
        FXMLLoader loader = new FXMLLoader(EstoqueApplication.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load(), width, height);
        scene.getStylesheets().add(EstoqueApplication.class.getResource("/css/style.css").toExternalForm());
        primaryStage.setTitle(title);
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
