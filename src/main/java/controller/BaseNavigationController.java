package controller;

import app.EstoqueApplication;
import service.LoginService;
import service.SessaoUsuario;
import util.AlertUtil;

import java.io.IOException;

public abstract class BaseNavigationController {

    protected void abrir(String fxml, String titulo) {
        try {
            EstoqueApplication.loadScene(fxml, titulo, 1320, 820);
        } catch (IOException exception) {
            AlertUtil.error("Navegacao", "Nao foi possivel abrir a tela solicitada.");
        }
    }

    public void abrirDashboard() {
        abrir("/view/dashboard.fxml", "Estoque Aluminio - Dashboard");
    }

    public void abrirProdutos() {
        abrir("/view/produtos.fxml", "Estoque Aluminio - Produtos");
    }

    public void abrirEstoque() {
        abrir("/view/estoque.fxml", "Estoque Aluminio - Estoque");
    }

    public void abrirSobras() {
        abrir("/view/sobras.fxml", "Estoque Aluminio - Sobras");
    }

    public void abrirMovimentacoes() {
        abrir("/view/movimentacoes.fxml", "Estoque Aluminio - Movimentacoes");
    }

    public void abrirUsuarios() {
        try {
            SessaoUsuario.exigirAdministrador();
            abrir("/view/usuarios.fxml", "Estoque Aluminio - Solicitacoes de Acesso");
        } catch (RuntimeException exception) {
            AlertUtil.warning("Acesso restrito", exception.getMessage());
        }
    }

    public void sair() {
        try {
            new LoginService().logout();
            EstoqueApplication.showLogin();
        } catch (IOException exception) {
            AlertUtil.error("Logout", "Nao foi possivel encerrar a sessao.");
        }
    }
}
