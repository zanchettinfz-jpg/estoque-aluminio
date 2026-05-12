package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Movimentacao;
import service.EstoqueService;

import java.time.LocalDateTime;

public class MovimentacaoController extends BaseNavigationController {
    @FXML private TextField produtoFiltroField;
    @FXML private ComboBox<String> tipoFiltroCombo;
    @FXML private DatePicker dataFiltroPicker;
    @FXML private TableView<Movimentacao> movimentacoesTable;
    @FXML private TableColumn<Movimentacao, String> produtoColumn;
    @FXML private TableColumn<Movimentacao, String> tipoColumn;
    @FXML private TableColumn<Movimentacao, Integer> quantidadeColumn;
    @FXML private TableColumn<Movimentacao, LocalDateTime> dataColumn;
    @FXML private TableColumn<Movimentacao, String> observacaoColumn;
    @FXML private TableColumn<Movimentacao, String> usuarioColumn;

    private final EstoqueService estoqueService = new EstoqueService();

    @FXML
    private void initialize() {
        produtoColumn.setCellValueFactory(new PropertyValueFactory<>("produtoCodigo"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("dataMovimentacao"));
        observacaoColumn.setCellValueFactory(new PropertyValueFactory<>("observacao"));
        usuarioColumn.setCellValueFactory(new PropertyValueFactory<>("usuarioNome"));
        tipoFiltroCombo.setItems(FXCollections.observableArrayList("Todos"));
        tipoFiltroCombo.getItems().addAll(EstoqueService.TIPOS);
        tipoFiltroCombo.setValue("Todos");
        produtoFiltroField.textProperty().addListener((obs, old, value) -> carregar());
        tipoFiltroCombo.valueProperty().addListener((obs, old, value) -> carregar());
        dataFiltroPicker.valueProperty().addListener((obs, old, value) -> carregar());
        carregar();
    }

    @FXML
    private void limparFiltros() {
        produtoFiltroField.clear();
        tipoFiltroCombo.setValue("Todos");
        dataFiltroPicker.setValue(null);
        carregar();
    }

    private void carregar() {
        movimentacoesTable.setItems(FXCollections.observableArrayList(
                estoqueService.historico(produtoFiltroField.getText(), tipoFiltroCombo.getValue(), dataFiltroPicker.getValue())
        ));
    }
}
