package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.Produto;
import model.Sobra;
import service.ProdutoService;
import service.SobraService;
import util.AlertUtil;

public class SobraController extends BaseNavigationController {
    @FXML private TextField buscaField;
    @FXML private ComboBox<Produto> produtoCombo;
    @FXML private TextField tamanhoOriginalField;
    @FXML private TextField tamanhoSobraField;
    @FXML private TextField localizacaoField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TableView<Sobra> sobrasTable;
    @FXML private TableColumn<Sobra, String> produtoColumn;
    @FXML private TableColumn<Sobra, Double> tamanhoOriginalColumn;
    @FXML private TableColumn<Sobra, Double> tamanhoSobraColumn;
    @FXML private TableColumn<Sobra, String> localizacaoColumn;
    @FXML private TableColumn<Sobra, String> statusColumn;

    private final ProdutoService produtoService = new ProdutoService();
    private final SobraService sobraService = new SobraService();

    @FXML
    private void initialize() {
        produtoColumn.setCellValueFactory(new PropertyValueFactory<>("produtoCodigo"));
        tamanhoOriginalColumn.setCellValueFactory(new PropertyValueFactory<>("tamanhoOriginal"));
        tamanhoSobraColumn.setCellValueFactory(new PropertyValueFactory<>("tamanhoSobra"));
        localizacaoColumn.setCellValueFactory(new PropertyValueFactory<>("localizacao"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCombo.setItems(FXCollections.observableArrayList("Disponivel", "Reutilizada", "Perda"));
        statusCombo.setValue("Disponivel");
        produtoCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Produto produto) {
                return produto == null ? "" : produto.getCodigo() + " - " + produto.getDescricao();
            }

            @Override
            public Produto fromString(String string) {
                return null;
            }
        });
        produtoCombo.setItems(FXCollections.observableArrayList(produtoService.listar()));
        buscaField.textProperty().addListener((obs, old, termo) -> carregar());
        carregar();
    }

    @FXML
    private void salvar() {
        try {
            Produto produto = produtoCombo.getValue();
            Sobra sobra = new Sobra();
            sobra.setProdutoId(produto == null ? 0 : produto.getId());
            sobra.setTamanhoOriginal(parseDouble(tamanhoOriginalField.getText()));
            sobra.setTamanhoSobra(parseDouble(tamanhoSobraField.getText()));
            sobra.setLocalizacao(localizacaoField.getText() == null ? "" : localizacaoField.getText().trim());
            sobra.setStatus(statusCombo.getValue());
            sobraService.salvar(sobra);
            limpar();
            carregar();
            AlertUtil.info("Sobras", "Sobra cadastrada com sucesso.");
        } catch (RuntimeException exception) {
            AlertUtil.warning("Sobras", exception.getMessage());
        }
    }

    @FXML
    private void reutilizar() {
        atualizarStatus("reutilizar");
    }

    @FXML
    private void perda() {
        atualizarStatus("perda");
    }

    private void atualizarStatus(String acao) {
        try {
            if ("reutilizar".equals(acao)) {
                sobraService.reutilizar(sobrasTable.getSelectionModel().getSelectedItem());
            } else {
                sobraService.perda(sobrasTable.getSelectionModel().getSelectedItem());
            }
            carregar();
        } catch (RuntimeException exception) {
            AlertUtil.warning("Sobras", exception.getMessage());
        }
    }

    private void carregar() {
        sobrasTable.setItems(FXCollections.observableArrayList(sobraService.pesquisar(buscaField.getText())));
    }

    private void limpar() {
        produtoCombo.getSelectionModel().clearSelection();
        tamanhoOriginalField.clear();
        tamanhoSobraField.clear();
        localizacaoField.clear();
        statusCombo.setValue("Disponivel");
    }

    private double parseDouble(String value) {
        try {
            return value == null || value.isBlank() ? 0 : Double.parseDouble(value.trim().replace(",", "."));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Informe um tamanho valido.");
        }
    }
}
