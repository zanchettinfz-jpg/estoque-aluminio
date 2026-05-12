package util;

import javafx.scene.control.Alert;

public class AlertUtil {

    private AlertUtil() {
    }

    public static void info(String titulo, String mensagem) {
        show(Alert.AlertType.INFORMATION, titulo, mensagem);
    }

    public static void warning(String titulo, String mensagem) {
        show(Alert.AlertType.WARNING, titulo, mensagem);
    }

    public static void error(String titulo, String mensagem) {
        show(Alert.AlertType.ERROR, titulo, mensagem);
    }

    private static void show(Alert.AlertType type, String titulo, String mensagem) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
