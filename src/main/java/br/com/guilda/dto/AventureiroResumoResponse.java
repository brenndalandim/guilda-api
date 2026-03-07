package br.com.guilda.dto;

public class AventureiroResumoResponse {
    private Long id;
    private String nome;
    private String classe;
    private Integer nivel;
    private Boolean ativo;

    public AventureiroResumoResponse(Long id, String nome, String classe, Integer nivel, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.classe = classe;
        this.nivel = nivel;
        this.ativo = ativo;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getClasse() { return classe; }
    public Integer getNivel() { return nivel; }
    public Boolean getAtivo() { return ativo; }
}
