package br.com.guilda.dto;

import java.math.BigDecimal;

public class RankingParticipacaoResponse {

    private Long aventureiroId;
    private String nome;
    private String classe;
    private Long totalParticipacoes;
    private BigDecimal totalRecompensas;
    private Long totalDestaques;

    public RankingParticipacaoResponse(Long aventureiroId,
                                       String nome,
                                       String classe,
                                       Long totalParticipacoes,
                                       BigDecimal totalRecompensas,
                                       Long totalDestaques) {
        this.aventureiroId = aventureiroId;
        this.nome = nome;
        this.classe = classe;
        this.totalParticipacoes = totalParticipacoes;
        this.totalRecompensas = totalRecompensas;
        this.totalDestaques = totalDestaques;
    }

    public Long getAventureiroId() {
        return aventureiroId;
    }

    public String getNome() {
        return nome;
    }

    public String getClasse() {
        return classe;
    }

    public Long getTotalParticipacoes() {
        return totalParticipacoes;
    }

    public BigDecimal getTotalRecompensas() {
        return totalRecompensas;
    }

    public Long getTotalDestaques() {
        return totalDestaques;
    }
}