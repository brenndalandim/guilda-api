package br.com.guilda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "companheiros", schema = "aventura")
public class Companheiro {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "aventureiro_id")
    private Aventureiro aventureiro;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EspecieCompanheiro especie;

    @Column(nullable = false)
    @jakarta.validation.constraints.Min(0)
    @jakarta.validation.constraints.Max(100)
    private Integer lealdade;

    public Companheiro() {
    }

    public Companheiro(String nome, EspecieCompanheiro especie, Integer lealdade) {
        this.nome = nome;
        this.especie = especie;
        this.lealdade = lealdade;
    }

    public Long getId() {
        return id;
    }

    public Aventureiro getAventureiro() {
        return aventureiro;
    }

    public void setAventureiro(Aventureiro aventureiro) {
        this.aventureiro = aventureiro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EspecieCompanheiro getEspecie() {
        return especie;
    }

    public void setEspecie(EspecieCompanheiro especie) {
        this.especie = especie;
    }

    public Integer getLealdade() {
        return lealdade;
    }

    public void setLealdade(Integer lealdade) {
        this.lealdade = lealdade;
    }
}