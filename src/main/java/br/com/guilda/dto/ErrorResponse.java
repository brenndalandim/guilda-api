package br.com.guilda.dto;

import java.util.List;

public class ErrorResponse {
    private String mensagem;
    private List<String> detalhes;

    public ErrorResponse(String mensagem, List<String> detalhes) {
        this.mensagem = mensagem;
        this.detalhes = detalhes;
    }

    public String getMensagem() { return mensagem; }
    public List<String> getDetalhes() { return detalhes; }
}
