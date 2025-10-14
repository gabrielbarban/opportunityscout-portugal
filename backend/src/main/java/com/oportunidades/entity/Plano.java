package com.oportunidades.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "planos")
public class Plano extends PanacheEntity {

    @Column(unique = true, nullable = false, length = 50)
    public String nome;

    @Column(name = "preco_mensal", precision = 10, scale = 2)
    public BigDecimal precoMensal;

    @Column(name = "preco_anual", precision = 10, scale = 2)
    public BigDecimal precoAnual;

    @Column(name = "max_alertas")
    public int maxAlertas = 0;

    @Column(name = "max_municipios")
    public int maxMunicipios = 0;

    @Column(name = "acesso_templates")
    public boolean acessoTemplates = false;

    @Column(name = "acesso_estatisticas")
    public boolean acessoEstatisticas = false;

    @Column(columnDefinition = "TEXT")
    public String descricao;

    // Métodos helper
    public boolean isFree() {
        return this.id != null && this.id == 1;
    }

    public boolean isPremium() {
        return this.id != null && this.id == 2;
    }

    public boolean isEnterprise() {
        return this.id != null && this.id == 3;
    }

    // Para facilitar comparações
    public int getNivel() {
        if (this.id == null) return 0;
        return this.id.intValue();
    }
}