package br.com.guilda.dto;

import java.math.BigDecimal;

public class ParticipanteMissaoResponse {

    private Long aventureiroId;
    private String nome;
    private String classe;
    private Integer nivel;
    private String papelMissao;
    private BigDecimal recompensaOuro;
    private Boolean destaque;

    public ParticipanteMissaoResponse() {
    }

    public ParticipanteMissaoResponse(Long aventureiroId,
                                      String nome,
                                      String classe,
                                      Integer nivel,
                                      String papelMissao,
                                      BigDecimal recompensaOuro,
                                      Boolean destaque) {
        this.aventureiroId = aventureiroId;
        this.nome = nome;
        this.classe = classe;
        this.nivel = nivel;
        this.papelMissao = papelMissao;
        this.recompensaOuro = recompensaOuro;
        this.destaque = destaque;
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

    public Integer getNivel() {
        return nivel;
    }

    public String getPapelMissao() {
        return papelMissao;
    }

    public BigDecimal getRecompensaOuro() {
        return recompensaOuro;
    }

    public Boolean getDestaque() {
        return destaque;
    }
}