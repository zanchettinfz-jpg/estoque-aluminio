package model;

public class Sobra {
    private int id;
    private int produtoId;
    private String produtoCodigo;
    private String produtoDescricao;
    private double tamanhoOriginal;
    private double tamanhoSobra;
    private String localizacao;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoCodigo() {
        return produtoCodigo;
    }

    public void setProdutoCodigo(String produtoCodigo) {
        this.produtoCodigo = produtoCodigo;
    }

    public String getProdutoDescricao() {
        return produtoDescricao;
    }

    public void setProdutoDescricao(String produtoDescricao) {
        this.produtoDescricao = produtoDescricao;
    }

    public double getTamanhoOriginal() {
        return tamanhoOriginal;
    }

    public void setTamanhoOriginal(double tamanhoOriginal) {
        this.tamanhoOriginal = tamanhoOriginal;
    }

    public double getTamanhoSobra() {
        return tamanhoSobra;
    }

    public void setTamanhoSobra(double tamanhoSobra) {
        this.tamanhoSobra = tamanhoSobra;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
