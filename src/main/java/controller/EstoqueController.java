package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Produto;
import service.EstoqueService;
import service.ProdutoService;
import util.AlertUtil;

public class EstoqueController extends BaseNavigationController {
    @FXML private TextField buscaField;
    @FXML private ComboBox<String> tipoCombo;
    @FXML private TextField quantidadeField;
    @FXML private TextArea observacaoArea;
    @FXML private Label produtoSelecionadoLabel;
    @FXML private TableView<Produto> produtosTable;
    @FXML private TableColumn<Produto, String> codigoColumn;
    @FXML private TableColumn<Produto, String> descricaoColumn;
    @FXML private TableColumn<Produto, Integer> quantidadeColumn;
    @FXML private TableColumn<Produto, String> localizacaoColumn;
    @FXML private ListView<String> ocupacaoList;

    private final ProdutoService produtoService = new ProdutoService();
    private final EstoqueService estoqueService = new EstoqueService();
    private Produto selecionado;

    @FXML
    private void initialize() {
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        localizacaoColumn.setCellValueFactory(new PropertyValueFactory<>("localizacao"));
        tipoCombo.setItems(FXCollections.observableArrayList(EstoqueService.TIPOS));
        produtosTable.getSelectionModel().selectedItemProperty().addListener((obs, old, produto) -> selecionar(produto));
        buscaField.textProperty().addListener((obs, old, termo) -> carregar());
        carregar();
    }

    @FXML
    private void registrarMovimentacao() {
        try {
            estoqueService.movimentar(selecionado, tipoCombo.getValue(), Integer.parseInt(quantidadeField.getText().trim()), observacaoArea.getText());
            quantidadeField.clear();
            observacaoArea.clear();
            carregar();
            AlertUtil.info("Estoque", "Movimentacao registrada e estoque atualizado.");
        } catch (RuntimeException exception) {
            AlertUtil.warning("Estoque", exception.getMessage());
        }
    }

    private void carregar() {
        produtosTable.setItems(FXCollections.observableArrayList(produtoService.pesquisar(buscaField.getText())));
        ocupacaoList.setItems(FXCollections.observableArrayList(produtoService.ocupacaoLocalizacoes()));
    }

    private void selecionar(Produto produto) {
        selecionado = produto;
        if (produto == null) {
            produtoSelecionadoLabel.setText("Nenhum produto selecionado");
            return;
        }
        produtoSelecionadoLabel.setText(produto.getCodigo() + " - " + produto.getDescricao() + " | Saldo: " + produto.getQuantidade());
    }
}
