package controller;

import app.EstoqueApplication;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.LoginService;
import util.AlertUtil;

import java.io.IOException;

public class CadastroController {
    @FXML private TextField nomeField;
    @FXML private TextField loginField;
    @FXML private PasswordField senhaField;
    @FXML private PasswordField confirmarSenhaField;

    private final LoginService loginService = new LoginService();

    @FXML
    private void cadastrar() {
        try {
            loginService.cadastrar(nomeField.getText(), loginField.getText(), senhaField.getText(), confirmarSenhaField.getText());
            AlertUtil.info("Cadastro", "Conta criada como PENDENTE. Aguarde aprovacao do administrador.");
            voltarLogin();
        } catch (RuntimeException exception) {
            AlertUtil.warning("Cadastro", exception.getMessage());
        }
    }

    @FXML
    private void voltarLogin() {
        try {
            EstoqueApplication.showLogin();
        } catch (IOException exception) {
            AlertUtil.error("Login", "Nao foi possivel voltar para o login.");
        }
    }
}
