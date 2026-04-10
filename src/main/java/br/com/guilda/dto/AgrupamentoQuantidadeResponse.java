package br.com.guilda.dto;

public class AgrupamentoQuantidadeResponse {

    private String chave;
    private Long quantidade;

    public AgrupamentoQuantidadeResponse() {
    }

    public AgrupamentoQuantidadeResponse(String chave, Long quantidade) {
        this.chave = chave;
        this.quantidade = quantidade;
    }

    public String getChave() {
        return chave;
    }

    public Long getQuantidade() {
        return quantidade;
    }
}