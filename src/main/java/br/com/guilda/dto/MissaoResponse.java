package br.com.guilda.dto;

public class MissaoResponse {

    private Long id;
    private Long organizacaoId;
    private String titulo;
    private String nivelPerigo;
    private String status;
    private String createdAt;
    private String dataInicio;
    private String dataFim;

    public MissaoResponse() {
    }

    public MissaoResponse(Long id,
                          Long organizacaoId,
                          String titulo,
                          String nivelPerigo,
                          String status,
                          String createdAt,
                          String dataInicio,
                          String dataFim) {
        this.id = id;
        this.organizacaoId = organizacaoId;
        this.titulo = titulo;
        this.nivelPerigo = nivelPerigo;
        this.status = status;
        this.createdAt = createdAt;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Long getId() {
        return id;
    }

    public Long getOrganizacaoId() {
        return organizacaoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNivelPerigo() {
        return nivelPerigo;
    }

    public String getStatus() {
        return status;
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