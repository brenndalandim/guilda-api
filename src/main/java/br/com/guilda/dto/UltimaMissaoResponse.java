package br.com.guilda.dto;

public class UltimaMissaoResponse {

    private Long id;
    private String titulo;
    private String status;
    private String nivelPerigo;

    public UltimaMissaoResponse() {
    }

    public UltimaMissaoResponse(Long id, String titulo, String status, String nivelPerigo) {
        this.id = id;
        this.titulo = titulo;
        this.status = status;
        this.nivelPerigo = nivelPerigo;
    }

    public Long getId() {
        return id;
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
}