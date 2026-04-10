package br.com.guilda.dto;

public class FaixaPrecoResponse {

    private String faixa;
    private Long quantidade;

    public FaixaPrecoResponse() {
    }

    public FaixaPrecoResponse(String faixa, Long quantidade) {
        this.faixa = faixa;
        this.quantidade = quantidade;
    }

    public String getFaixa() {
        return faixa;
    }

    public Long getQuantidade() {
        return quantidade;
    }
}