package service;

import model.Movimentacao;
import model.Produto;
import model.TipoMovimentacao;
import repository.MovimentacaoRepository;
import repository.ProdutoRepository;
import util.ConexaoSQLite;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EstoqueService {
    public static final List<String> TIPOS = TipoMovimentacao.descricoes();

    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private final MovimentacaoRepository movimentacaoRepository = new MovimentacaoRepository();

    public void movimentar(Produto produto, String tipo, int quantidade, String destino, String observacao) {
        SessaoUsuario.exigirOperacao();
        if (produto == null) {
            throw new IllegalArgumentException("Selecione um produto.");
        }
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Selecione o tipo de movimentacao.");
        }
        if (!TipoMovimentacao.existe(tipo)) {
            throw new IllegalArgumentException("Tipo de movimentacao invalido.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        if (TipoMovimentacao.exigeDestino(tipo) && (destino == null || destino.isBlank())) {
            throw new IllegalArgumentException("Toda saida deve possuir destino/responsavel.");
        }

        int novaQuantidade = calcularNovaQuantidade(produto, tipo, quantidade);
        try (Connection connection = ConexaoSQLite.getConnection()) {
            connection.setAutoCommit(false);
            try {
                produtoRepository.updateQuantidade(connection, produto.getId(), novaQuantidade);

                Movimentacao movimentacao = new Movimentacao();
                movimentacao.setProdutoId(produto.getId());
                movimentacao.setTipo(tipo);
                movimentacao.setQuantidade(quantidade);
                movimentacao.setDestino(destino == null ? null : destino.trim());
                movimentacao.setObservacao(observacao);
                movimentacao.setUsuarioId(SessaoUsuario.getUsuarioLogado().getId());
                movimentacaoRepository.save(connection, movimentacao);

                connection.commit();
                produto.setQuantidade(novaQuantidade);
            } catch (SQLException | RuntimeException exception) {
                connection.rollback();
                throw exception;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Erro ao registrar movimentacao", exception);
        }
    }

    public List<Movimentacao> historico(String pesquisa, String usuario, String tipo, LocalDate data) {
        SessaoUsuario.exigirLogin();
        return movimentacaoRepository.findAll(pesquisa, usuario, tipo, data);
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
        if (TipoMovimentacao.exigeDestino(tipo)) {
            if (produto.getQuantidade() < quantidade) {
                throw new IllegalArgumentException("Estoque insuficiente para esta saida.");
            }
            return produto.getQuantidade() - quantidade;
        }
        return produto.getQuantidade() + quantidade;
    }
}
