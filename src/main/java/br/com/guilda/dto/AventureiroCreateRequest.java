package br.com.guilda.dto;

public class AventureiroCreateRequest {
    private String nome;
    private String classe;
    private Integer nivel;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getClasse() { return classe; }
    public void setClasse(String classe) { this.classe = classe; }

    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }
}
