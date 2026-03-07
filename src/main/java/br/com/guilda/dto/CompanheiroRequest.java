package br.com.guilda.dto;

public class CompanheiroRequest {
    private String nome;
    private String especie;
    private Integer lealdade;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public Integer getLealdade() { return lealdade; }
    public void setLealdade(Integer lealdade) { this.lealdade = lealdade; }
}
