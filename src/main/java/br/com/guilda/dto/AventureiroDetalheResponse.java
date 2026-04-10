package br.com.guilda.dto;

public class AventureiroDetalheResponse {

    private Long id;
    private String nome;
    private String classe;
    private Integer nivel;
    private Boolean ativo;
    private CompanheiroResponse companheiro;
    private Integer totalParticipacoes;
    private UltimaMissaoResponse ultimaMissao;

    public AventureiroDetalheResponse() {
    }

    public AventureiroDetalheResponse(Long id,
                                      String nome,
                                      String classe,
                                      Integer nivel,
                                      Boolean ativo,
                                      CompanheiroResponse companheiro,
                                      Integer totalParticipacoes,
                                      UltimaMissaoResponse ultimaMissao) {
        this.id = id;
        this.nome = nome;
        this.classe = classe;
        this.nivel = nivel;
        this.ativo = ativo;
        this.companheiro = companheiro;
        this.totalParticipacoes = totalParticipacoes;
        this.ultimaMissao = ultimaMissao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getClasse() {
        return classe;
    }

    public Integer getNivel() {
        return nivel;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public CompanheiroResponse getCompanheiro() {
        return companheiro;
    }

    public Integer getTotalParticipacoes() {
        return totalParticipacoes;
    }

    public UltimaMissaoResponse getUltimaMissao() {
        return ultimaMissao;
    }
}