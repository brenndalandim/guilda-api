package br.com.guilda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "missoes", schema = "aventura")
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_perigo", nullable = false, length = 20)
    private NivelPerigo nivelPerigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMissao status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "data_inicio")
    private OffsetDateTime dataInicio;

    @Column(name = "data_fim")
    private OffsetDateTime dataFim;

    @OneToMany(mappedBy = "missao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.Set<ParticipacaoMissao> participacoes = new java.util.LinkedHashSet<>();

    public java.util.Set<ParticipacaoMissao> getParticipacoes() {
        return participacoes;
    }

    public Missao() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Organizacao getOrganizacao() {
        return organizacao;
    }

    public void setOrganizacao(Organizacao organizacao) {
        this.organizacao = organizacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public NivelPerigo getNivelPerigo() {
        return nivelPerigo;
    }

    public void setNivelPerigo(NivelPerigo nivelPerigo) {
        this.nivelPerigo = nivelPerigo;
    }

    public StatusMissao getStatus() {
        return status;
    }

    public void setStatus(StatusMissao status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(OffsetDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public OffsetDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(OffsetDateTime dataFim) {
        this.dataFim = dataFim;
    }
}