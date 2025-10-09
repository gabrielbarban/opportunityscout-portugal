package com.oportunidades.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "oportunidades")
public class Oportunidade extends PanacheEntity {

    @Column(nullable = false)
    public String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Fonte fonte;

    @Column(nullable = false)
    public String entidade;

    public LocalDate dataInicio;

    public LocalDate dataFim;

    @Column(nullable = false, length = 1000)
    public String link;

    public String categoria;

    public String codigo;

    @Column(length = 2000)
    public String descricao;

    @Column(length = 500)
    public String objetivos;

    public String valorFinanciamento;

    public String tipoApoio;

    public String beneficiarios;

    @Column(nullable = false, updatable = false)
    public LocalDateTime criadoEm = LocalDateTime.now();

    public LocalDateTime atualizadoEm = LocalDateTime.now();

    public enum Fonte {
        PORTUGAL2030, COMPETE2030
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}