package br.com.guilda.dto;

import java.math.BigDecimal;

public class ProdutoLojaResponse {

    private String nome;
    private String descricao;
    private String categoria;
    private String raridade;
    private BigDecimal preco;

    public ProdutoLojaResponse() {
    }

    public ProdutoLojaResponse(String nome, String descricao, String categoria, String raridade, BigDecimal preco) {
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.raridade = raridade;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getRaridade() {
        return raridade;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}