package br.com.guilda.dto;

import java.math.BigDecimal;

public class ParticipacaoMissaoRequest {

    private Long aventureiroId;
    private String papelMissao;
    private BigDecimal recompensaOuro;
    private Boolean destaque;

    public ParticipacaoMissaoRequest() {
    }

    public Long getAventureiroId() {
        return aventureiroId;
    }

    public void setAventureiroId(Long aventureiroId) {
        this.aventureiroId = aventureiroId;
    }

    public String getPapelMissao() {
        return papelMissao;
    }

    public void setPapelMissao(String papelMissao) {
        this.papelMissao = papelMissao;
    }

    public BigDecimal getRecompensaOuro() {
        return recompensaOuro;
    }

    public void setRecompensaOuro(BigDecimal recompensaOuro) {
        this.recompensaOuro = recompensaOuro;
    }

    public Boolean getDestaque() {
        return destaque;
    }

    public void setDestaque(Boolean destaque) {
        this.destaque = destaque;
    }
}