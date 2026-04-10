package br.com.guilda.dto;

public class MissaoResumoResponse {

    private Long id;
    private String titulo;
    private String status;
    private String nivelPerigo;
    private String createdAt;
    private String dataInicio;
    private String dataFim;

    public MissaoResumoResponse() {
    }

    public MissaoResumoResponse(Long id,
                                String titulo,
                                String status,
                                String nivelPerigo,
                                String createdAt,
                                String dataInicio,
                                String dataFim) {
        this.id = id;
        this.titulo = titulo;
        this.status = status;
        this.nivelPerigo = nivelPerigo;
        this.createdAt = createdAt;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }
}