package br.com.guilda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "aventureiros", schema = "aventura")
public class Aventureiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_cadastro_id", nullable = false)
    private Usuario usuarioCadastro;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ClasseAventureiro classe;

    @Column(nullable = false)
    private Integer nivel;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToOne(mappedBy = "aventureiro", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Companheiro companheiro;

    @OneToMany(mappedBy = "aventureiro", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private java.util.Set<ParticipacaoMissao> participacoes = new java.util.LinkedHashSet<>();

    public java.util.Set<ParticipacaoMissao> getParticipacoes() {
        return participacoes;
    }

    public Aventureiro() {
    }

    public Aventureiro(Long id, String nome, ClasseAventureiro classe, Integer nivel, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.classe = classe;
        this.nivel = nivel;
        this.ativo = ativo;
    }

    @PrePersist
    public void prePersist() {
        OffsetDateTime agora = OffsetDateTime.now();
        this.createdAt = agora;
        this.updatedAt = agora;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
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

    public Usuario getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(Usuario usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ClasseAventureiro getClasse() {
        return classe;
    }

    public void setClasse(ClasseAventureiro classe) {
        this.classe = classe;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Companheiro getCompanheiro() {
        return companheiro;
    }

    public void setCompanheiro(Companheiro companheiro) {
        this.companheiro = companheiro;

        if (companheiro != null) {
            companheiro.setAventureiro(this);
        }
    }
}