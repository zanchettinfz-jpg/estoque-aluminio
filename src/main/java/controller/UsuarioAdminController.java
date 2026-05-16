package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Usuario;
import model.NivelUsuario;
import model.StatusUsuario;
import service.LoginService;
import service.SessaoUsuario;
import util.AlertUtil;

public class UsuarioAdminController extends BaseNavigationController {
    @FXML private TableView<Usuario> usuariosTable;
    @FXML private TableColumn<Usuario, String> nomeColumn;
    @FXML private TableColumn<Usuario, String> loginColumn;
    @FXML private TableColumn<Usuario, String> nivelColumn;
    @FXML private TableColumn<Usuario, String> statusColumn;
    @FXML private TableColumn<Usuario, String> criadoEmColumn;
    @FXML private ComboBox<String> nivelCombo;
    @FXML private ComboBox<String> statusCombo;

    private final LoginService loginService = new LoginService();

    @FXML
    private void initialize() {
        try {
            SessaoUsuario.exigirAdministrador();
            nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
            loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
            nivelColumn.setCellValueFactory(new PropertyValueFactory<>("nivel"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            criadoEmColumn.setCellValueFactory(new PropertyValueFactory<>("criadoEm"));
            nivelCombo.setItems(FXCollections.observableArrayList(
                    NivelUsuario.ADMINISTRADOR.name(), NivelUsuario.OPERADOR.name(), NivelUsuario.CONSULTA.name()
            ));
            statusCombo.setItems(FXCollections.observableArrayList(
                    StatusUsuario.PENDENTE.name(), StatusUsuario.ATIVO.name(), StatusUsuario.BLOQUEADO.name()
            ));
            usuariosTable.getSelectionModel().selectedItemProperty().addListener((obs, old, usuario) -> selecionar(usuario));
            carregar();
        } catch (RuntimeException exception) {
            AlertUtil.warning("Administracao", exception.getMessage());
            abrirDashboard();
        }
    }

    @FXML
    private void aprovar() {
        statusCombo.setValue(StatusUsuario.ATIVO.name());
        salvarAlteracoes();
    }

    @FXML
    private void recusar() {
        statusCombo.setValue(StatusUsuario.BLOQUEADO.name());
        salvarAlteracoes();
    }

    @FXML
    private void bloquear() {
        statusCombo.setValue(StatusUsuario.BLOQUEADO.name());
        salvarAlteracoes();
    }

    @FXML
    private void salvarAlteracoes() {
        try {
            Usuario usuario = usuariosTable.getSelectionModel().getSelectedItem();
            loginService.atualizarUsuario(usuario, statusCombo.getValue(), nivelCombo.getValue());
            carregar();
            AlertUtil.info("Usuarios", "Usuario atualizado com sucesso.");
        } catch (RuntimeException exception) {
            AlertUtil.warning("Usuarios", exception.getMessage());
        }
    }

    private void carregar() {
        usuariosTable.setItems(FXCollections.observableArrayList(loginService.listarUsuarios()));
    }

    private void selecionar(Usuario usuario) {
        if (usuario == null) {
            return;
        }
        nivelCombo.setValue(usuario.getNivel());
        statusCombo.setValue(usuario.getStatus());
    }
}
