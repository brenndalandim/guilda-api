package br.com.guilda.dto;

import java.util.List;

public class MissaoDetalheResponse {

    private Long id;
    private String titulo;
    private String status;
    private String nivelPerigo;
    private String createdAt;
    private String dataInicio;
    private String dataFim;
    private List<ParticipanteMissaoResponse> participantes;

    public MissaoDetalheResponse() {
    }

    public MissaoDetalheResponse(Long id,
                                 String titulo,
                                 String status,
                                 String nivelPerigo,
                                 String createdAt,
                                 String dataInicio,
                                 String dataFim,
                                 List<ParticipanteMissaoResponse> participantes) {
        this.id = id;
        this.titulo = titulo;
        this.status = status;
        this.nivelPerigo = nivelPerigo;
        this.createdAt = createdAt;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.participantes = participantes;
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

    public List<ParticipanteMissaoResponse> getParticipantes() {
        return participantes;
    }
}