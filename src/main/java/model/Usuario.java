package model;

public class Usuario {
    private int id;
    private String nome;
    private String login;
    private String senha;
    private String nivel;
    private String status;
    private String criadoEm;
    private Integer aprovadoPor;

    public Usuario() {
    }

    public Usuario(int id, String nome, String login, String senha, String nivel) {
        this(id, nome, login, senha, nivel, "ATIVO", null, null);
    }

    public Usuario(int id, String nome, String login, String senha, String nivel, String status, String criadoEm, Integer aprovadoPor) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.nivel = nivel;
        this.status = status;
        this.criadoEm = criadoEm;
        this.aprovadoPor = aprovadoPor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(String criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Integer getAprovadoPor() {
        return aprovadoPor;
    }

    public void setAprovadoPor(Integer aprovadoPor) {
        this.aprovadoPor = aprovadoPor;
    }
}
