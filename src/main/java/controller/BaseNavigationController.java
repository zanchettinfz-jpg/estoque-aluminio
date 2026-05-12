package controller;

import app.EstoqueApplication;
import service.LoginService;
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
        abrir("/view/dashboard.fxml", "Estoque Alumínio - Dashboard");
    }

    public void abrirProdutos() {
        abrir("/view/produtos.fxml", "Estoque Alumínio - Produtos");
    }

    public void abrirEstoque() {
        abrir("/view/estoque.fxml", "Estoque Alumínio - Estoque");
    }

    public void abrirSobras() {
        abrir("/view/sobras.fxml", "Estoque Alumínio - Sobras");
    }

    public void abrirMovimentacoes() {
        abrir("/view/movimentacoes.fxml", "Estoque Alumínio - Movimentações");
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
