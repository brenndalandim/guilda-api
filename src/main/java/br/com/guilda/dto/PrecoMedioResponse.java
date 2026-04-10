package br.com.guilda.dto;

import java.math.BigDecimal;

public class PrecoMedioResponse {

    private BigDecimal precoMedio;

    public PrecoMedioResponse() {
    }

    public PrecoMedioResponse(BigDecimal precoMedio) {
        this.precoMedio = precoMedio;
    }

    public BigDecimal getPrecoMedio() {
        return precoMedio;
    }
}