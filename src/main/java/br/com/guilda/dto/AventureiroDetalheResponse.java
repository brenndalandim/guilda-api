package br.com.guilda.dto;

public class AventureiroDetalheResponse {
    private Long id;
    private String nome;
    private String classe;
    private Integer nivel;
    private Boolean ativo;
    private CompanheiroResponse companheiro;

    public AventureiroDetalheResponse(Long id, String nome, String classe, Integer nivel, Boolean ativo, CompanheiroResponse companheiro) {
        this.id = id;
        this.nome = nome;
        this.classe = classe;
        this.nivel = nivel;
        this.ativo = ativo;
        this.companheiro = companheiro;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getClasse() { return classe; }
    public Integer getNivel() { return nivel; }
    public Boolean getAtivo() { return ativo; }
    public CompanheiroResponse getCompanheiro() { return companheiro; }
}
