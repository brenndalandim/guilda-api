package br.com.guilda.dto;

import java.math.BigDecimal;

public class RelatorioMissaoResponse {

    private Long missaoId;
    private String titulo;
    private String status;
    private String nivelPerigo;
    private Long quantidadeParticipantes;
    private BigDecimal totalRecompensas;

    public RelatorioMissaoResponse(Long missaoId,
                                   String titulo,
                                   String status,
                                   String nivelPerigo,
                                   Long quantidadeParticipantes,
                                   BigDecimal totalRecompensas) {
        this.missaoId = missaoId;
        this.titulo = titulo;
        this.status = status;
        this.nivelPerigo = nivelPerigo;
        this.quantidadeParticipantes = quantidadeParticipantes;
        this.totalRecompensas = totalRecompensas;
    }

    public Long getMissaoId() {
        return missaoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getStatus() {
        return status;
    }

    public String getNivelPerigo() {
        return nivelPerigo;
    }

    public Long getQuantidadeParticipantes() {
        return quantidadeParticipantes;
    }

    public BigDecimal getTotalRecompensas() {
        return totalRecompensas;
    }
}