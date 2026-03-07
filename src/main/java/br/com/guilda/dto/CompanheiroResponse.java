package br.com.guilda.dto;

public class CompanheiroResponse {
    private String nome;
    private String especie;
    private Integer lealdade;

    public CompanheiroResponse(String nome, String especie, Integer lealdade) {
        this.nome = nome;
        this.especie = especie;
        this.lealdade = lealdade;
    }

    public String getNome() { return nome; }
    public String getEspecie() { return especie; }
    public Integer getLealdade() { return lealdade; }
}
