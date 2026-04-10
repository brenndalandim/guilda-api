package br.com.guilda.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "participacoes_missao",
        schema = "aventura",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_participacao_missao_aventureiro", columnNames = {"missao_id", "aventureiro_id"})
        }
)
public class ParticipacaoMissao {

    @EmbeddedId
    private ParticipacaoMissaoId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("missaoId")
    @JoinColumn(name = "missao_id", nullable = false)
    private Missao missao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("aventureiroId")
    @JoinColumn(name = "aventureiro_id", nullable = false)
    private Aventureiro aventureiro;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel_missao", nullable = false, length = 30)
    private PapelMissao papelMissao;

    @Column(name = "recompensa_ouro", precision = 10, scale = 2)
    private BigDecimal recompensaOuro;

    @Column(nullable = false)
    private Boolean destaque;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public ParticipacaoMissao() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }

    public ParticipacaoMissaoId getId() {
        return id;
    }

    public void setId(ParticipacaoMissaoId id) {
        this.id = id;
    }

    public Missao getMissao() {
        return missao;
    }

    public void setMissao(Missao missao) {
        this.missao = missao;
    }

    public Aventureiro getAventureiro() {
        return aventureiro;
    }

    public void setAventureiro(Aventureiro aventureiro) {
        this.aventureiro = aventureiro;
    }

    public PapelMissao getPapelMissao() {
        return papelMissao;
    }

    public void setPapelMissao(PapelMissao papelMissao) {
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}