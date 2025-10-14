package com.oportunidades.dto;

import com.oportunidades.entity.Plano;
import java.math.BigDecimal;

public class PlanoResponse {
    public Long id;
    public String nome;
    public BigDecimal precoMensal;
    public BigDecimal precoAnual;
    public int maxAlertas;
    public int maxMunicipios;
    public boolean acessoTemplates;
    public boolean acessoEstatisticas;
    public String descricao;
    public double economiaAnual; // Percentual de economia anual

    public PlanoResponse() {
    }

    public PlanoResponse(Plano plano, double economiaAnual) {
        this.id = plano.id;
        this.nome = plano.nome;
        this.precoMensal = plano.precoMensal;
        this.precoAnual = plano.precoAnual;
        this.maxAlertas = plano.maxAlertas;
        this.maxMunicipios = plano.maxMunicipios;
        this.acessoTemplates = plano.acessoTemplates;
        this.acessoEstatisticas = plano.acessoEstatisticas;
        this.descricao = plano.descricao;
        this.economiaAnual = economiaAnual;
    }

    public static PlanoResponse from(Plano plano, double economiaAnual) {
        return new PlanoResponse(plano, economiaAnual);
    }
}