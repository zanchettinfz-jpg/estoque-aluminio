package service;

import model.Produto;
import repository.ProdutoRepository;

import java.util.List;

public class ProdutoService {
    private final ProdutoRepository produtoRepository = new ProdutoRepository();

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public List<Produto> pesquisar(String termo) {
        if (termo == null || termo.isBlank()) {
            return listar();
        }
        return produtoRepository.search(termo);
    }

    public Produto salvar(Produto produto) {
        validar(produto);
        if (produtoRepository.existsByCodigo(produto.getCodigo(), produto.getId())) {
            throw new IllegalArgumentException("Ja existe produto cadastrado com este codigo.");
        }
        return produtoRepository.save(produto);
    }

    public void excluir(Produto produto) {
        if (produto == null || produto.getId() == 0) {
            throw new IllegalArgumentException("Selecione um produto para excluir.");
        }
        produtoRepository.delete(produto.getId());
    }

    public int totalProdutos() {
        return produtoRepository.count();
    }

    public int quantidadeTotal() {
        return produtoRepository.sumQuantidade();
    }

    public List<String> ocupacaoLocalizacoes() {
        return produtoRepository.ocupacaoLocalizacoes();
    }

    private void validar(Produto produto) {
        if (isBlank(produto.getCodigo()) || isBlank(produto.getDescricao())) {
            throw new IllegalArgumentException("Codigo e descricao sao obrigatorios.");
        }
        if (produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade nao pode ser negativa.");
        }
        if (produto.getTamanho() < 0) {
            throw new IllegalArgumentException("Tamanho nao pode ser negativo.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
