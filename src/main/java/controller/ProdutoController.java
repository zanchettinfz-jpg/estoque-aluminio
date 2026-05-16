package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Produto;
import service.ProdutoService;
import util.AlertUtil;

public class ProdutoController extends BaseNavigationController {
    @FXML private TextField buscaField;
    @FXML private TextField codigoField;
    @FXML private TextField descricaoField;
    @FXML private TextField corField;
    @FXML private TextField linhaField;
    @FXML private TextField tamanhoField;
    @FXML private TextField quantidadeField;
    @FXML private TextField unidadeField;
    @FXML private TextField localizacaoField;
    @FXML private TextArea observacoesArea;
    @FXML private TableView<Produto> produtosTable;
    @FXML private TableColumn<Produto, String> codigoColumn;
    @FXML private TableColumn<Produto, String> descricaoColumn;
    @FXML private TableColumn<Produto, String> corColumn;
    @FXML private TableColumn<Produto, String> linhaColumn;
    @FXML private TableColumn<Produto, Double> tamanhoColumn;
    @FXML private TableColumn<Produto, Integer> quantidadeColumn;
    @FXML private TableColumn<Produto, String> unidadeColumn;
    @FXML private TableColumn<Produto, String> localizacaoColumn;

    private final ProdutoService produtoService = new ProdutoService();
    private Produto selecionado;

    @FXML
    private void initialize() {
        configurarTabela();
        produtosTable.getSelectionModel().selectedItemProperty().addListener((obs, old, produto) -> preencherFormulario(produto));
        buscaField.textProperty().addListener((obs, old, termo) -> carregarProdutos());
        carregarProdutos();
    }

    @FXML
    private void salvar() {
        try {
            Produto produto = selecionado == null ? new Produto() : selecionado;
            produto.setCodigo(texto(codigoField));
            produto.setDescricao(texto(descricaoField));
            produto.setCor(texto(corField));
            produto.setLinha(texto(linhaField));
            produto.setTamanho(parseDouble(tamanhoField.getText()));
            if (produto.getId() == 0) {
                produto.setQuantidade(parseInt(quantidadeField.getText()));
            }
            produto.setUnidade(texto(unidadeField));
            produto.setLocalizacao(texto(localizacaoField));
            produto.setObservacoes(observacoesArea.getText());
            produtoService.salvar(produto);
            limpar();
            carregarProdutos();
            AlertUtil.info("Produto", "Produto salvo com sucesso.");
        } catch (RuntimeException exception) {
            AlertUtil.warning("Produto", exception.getMessage());
        }
    }

    @FXML
    private void excluir() {
        try {
            produtoService.excluir(selecionado);
            limpar();
            carregarProdutos();
            AlertUtil.info("Produto", "Produto excluido com sucesso.");
        } catch (RuntimeException exception) {
            AlertUtil.warning("Produto", exception.getMessage());
        }
    }

    @FXML
    private void limpar() {
        selecionado = null;
        produtosTable.getSelectionModel().clearSelection();
        codigoField.clear();
        descricaoField.clear();
        corField.clear();
        linhaField.clear();
        tamanhoField.clear();
        quantidadeField.clear();
        unidadeField.clear();
        localizacaoField.clear();
        observacoesArea.clear();
    }

    private void configurarTabela() {
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        corColumn.setCellValueFactory(new PropertyValueFactory<>("cor"));
        linhaColumn.setCellValueFactory(new PropertyValueFactory<>("linha"));
        tamanhoColumn.setCellValueFactory(new PropertyValueFactory<>("tamanho"));
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        unidadeColumn.setCellValueFactory(new PropertyValueFactory<>("unidade"));
        localizacaoColumn.setCellValueFactory(new PropertyValueFactory<>("localizacao"));
    }

    private void carregarProdutos() {
        produtosTable.setItems(FXCollections.observableArrayList(produtoService.pesquisar(buscaField.getText())));
    }

    private void preencherFormulario(Produto produto) {
        selecionado = produto;
        if (produto == null) {
            return;
        }
        codigoField.setText(produto.getCodigo());
        descricaoField.setText(produto.getDescricao());
        corField.setText(produto.getCor());
        linhaField.setText(produto.getLinha());
        tamanhoField.setText(String.valueOf(produto.getTamanho()));
        quantidadeField.setText(String.valueOf(produto.getQuantidade()));
        unidadeField.setText(produto.getUnidade());
        localizacaoField.setText(produto.getLocalizacao());
        observacoesArea.setText(produto.getObservacoes());
    }

    private int parseInt(String value) {
        try {
            return value == null || value.isBlank() ? 0 : Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Informe uma quantidade valida.");
        }
    }

    private double parseDouble(String value) {
        try {
            return value == null || value.isBlank() ? 0 : Double.parseDouble(value.trim().replace(",", "."));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Informe um tamanho valido.");
        }
    }

    private String texto(TextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }
}
