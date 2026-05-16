package controller;

import app.EstoqueApplication;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.LoginService;
import util.AlertUtil;

import java.io.IOException;

public class LoginController {
    @FXML private TextField loginField;
    @FXML private PasswordField senhaField;

    private final LoginService loginService = new LoginService();

    @FXML
    private void entrar() {
        try {
            loginService.autenticar(loginField.getText(), senhaField.getText());
            EstoqueApplication.showDashboard();
        } catch (IllegalArgumentException | IOException exception) {
            AlertUtil.warning("Acesso negado", exception.getMessage());
        }
    }

    @FXML
    private void abrirCadastro() {
        try {
            EstoqueApplication.showCadastro();
        } catch (IOException exception) {
            AlertUtil.error("Cadastro", "Nao foi possivel abrir a tela de cadastro.");
        }
    }
}
