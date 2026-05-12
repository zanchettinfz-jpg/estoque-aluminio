package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Movimentacao;
import service.EstoqueService;
import service.ProdutoService;
import service.SessaoUsuario;
import service.SobraService;

import java.time.LocalDateTime;

public class DashboardController extends BaseNavigationController {
    @FXML private Label usuarioLabel;
    @FXML private Label totalProdutosLabel;
    @FXML private Label quantidadeEstoqueLabel;
    @FXML private Label movimentacoesHojeLabel;
    @FXML private Label sobrasDisponiveisLabel;
    @FXML private BarChart<String, Number> fluxoChart;
    @FXML private TableView<Movimentacao> ultimasTable;
    @FXML private TableColumn<Movimentacao, String> produtoColumn;
    @FXML private TableColumn<Movimentacao, String> tipoColumn;
    @FXML private TableColumn<Movimentacao, Integer> quantidadeColumn;
    @FXML private TableColumn<Movimentacao, LocalDateTime> dataColumn;

    private final ProdutoService produtoService = new ProdutoService();
    private final EstoqueService estoqueService = new EstoqueService();
    private final SobraService sobraService = new SobraService();

    @FXML
    private void initialize() {
        produtoColumn.setCellValueFactory(new PropertyValueFactory<>("produtoCodigo"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("dataMovimentacao"));
        carregar();
    }

    private void carregar() {
        String nome = SessaoUsuario.getUsuarioLogado() == null ? "Administrador" : SessaoUsuario.getUsuarioLogado().getNome();
        usuarioLabel.setText(nome);
        totalProdutosLabel.setText(String.valueOf(produtoService.totalProdutos()));
        quantidadeEstoqueLabel.setText(String.valueOf(produtoService.quantidadeTotal()));
        movimentacoesHojeLabel.setText(String.valueOf(estoqueService.movimentacoesHoje()));
        sobrasDisponiveisLabel.setText(String.valueOf(sobraService.disponiveis()));
        ultimasTable.setItems(FXCollections.observableArrayList(estoqueService.ultimas(12)));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Movimentacoes");
        series.getData().add(new XYChart.Data<>("Entradas", estoqueService.totalEntradas()));
        series.getData().add(new XYChart.Data<>("Saidas", estoqueService.totalSaidas()));
        fluxoChart.getData().setAll(series);
        fluxoChart.setLegendVisible(false);
    }
}
