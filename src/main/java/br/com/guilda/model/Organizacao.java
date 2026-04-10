package br.com.guilda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizacoes", schema = "audit")
public class Organizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private List<Usuario> usuarios = new ArrayList<>();

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    public Organizacao() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}