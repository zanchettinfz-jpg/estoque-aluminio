package model;

public class Produto {
    private int id;
    private String codigo;
    private String descricao;
    private String cor;
    private String linha;
    private double tamanho;
    private int quantidade;
    private String unidade;
    private String localizacao;
    private String observacoes;

    public Produto() {
    }

    public Produto(int id, String codigo, String descricao, String cor, String linha, double tamanho,
                   int quantidade, String unidade, String localizacao, String observacoes) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.cor = cor;
        this.linha = linha;
        this.tamanho = tamanho;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.localizacao = localizacao;
        this.observacoes = observacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public double getTamanho() {
        return tamanho;
    }

    public void setTamanho(double tamanho) {
        this.tamanho = tamanho;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
