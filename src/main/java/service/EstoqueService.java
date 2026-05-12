package service;

import model.Movimentacao;
import model.Produto;
import repository.MovimentacaoRepository;
import repository.ProdutoRepository;
import util.ConexaoSQLite;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class EstoqueService {
    public static final List<String> TIPOS = List.of(
            "Entrada - Compra", "Entrada - Reposicao", "Entrada - Devolucao",
            "Saida - Producao", "Saida - Perda", "Saida - Venda", "Saida - Descarte"
    );

    private static final Set<String> SAIDAS = Set.of(
            "Saida - Producao", "Saida - Perda", "Saida - Venda", "Saida - Descarte"
    );

    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private final MovimentacaoRepository movimentacaoRepository = new MovimentacaoRepository();

    public void movimentar(Produto produto, String tipo, int quantidade, String observacao) {
        if (produto == null) {
            throw new IllegalArgumentException("Selecione um produto.");
        }
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Selecione o tipo de movimentacao.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }

        int novaQuantidade = calcularNovaQuantidade(produto, tipo, quantidade);
        try (Connection connection = ConexaoSQLite.getConnection()) {
            connection.setAutoCommit(false);
            produtoRepository.updateQuantidade(connection, produto.getId(), novaQuantidade);

            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setProdutoId(produto.getId());
            movimentacao.setTipo(tipo);
            movimentacao.setQuantidade(quantidade);
            movimentacao.setObservacao(observacao);
            movimentacao.setUsuarioId(SessaoUsuario.getUsuarioLogado() == null ? 1 : SessaoUsuario.getUsuarioLogado().getId());
            movimentacaoRepository.save(connection, movimentacao);

            connection.commit();
            produto.setQuantidade(novaQuantidade);
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao registrar movimentacao", exception);
        }
    }

    public List<Movimentacao> historico(String produto, String tipo, LocalDate data) {
        return movimentacaoRepository.findAll(produto, tipo, data);
    }

    public List<Movimentacao> ultimas(int limite) {
        return movimentacaoRepository.findLast(limite);
    }

    public int movimentacoesHoje() {
        return movimentacaoRepository.countToday();
    }

    public int totalEntradas() {
        return movimentacaoRepository.countByTipoLike("Entrada");
    }

    public int totalSaidas() {
        return movimentacaoRepository.countByTipoLike("Saida");
    }

    private int calcularNovaQuantidade(Produto produto, String tipo, int quantidade) {
        if (SAIDAS.contains(tipo)) {
            if (produto.getQuantidade() < quantidade) {
                throw new IllegalArgumentException("Estoque insuficiente para esta saida.");
            }
            return produto.getQuantidade() - quantidade;
        }
        return produto.getQuantidade() + quantidade;
    }
}
