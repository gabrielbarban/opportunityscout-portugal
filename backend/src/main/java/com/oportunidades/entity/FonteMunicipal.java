package com.oportunidades.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "fontes_municipais")
public class FonteMunicipal extends PanacheEntity {

    @Column(nullable = false, length = 100)
    public String municipio;

    @Column(length = 100)
    public String distrito;

    @Column(length = 500)
    public String url;

    @Column(name = "codigo_fonte", unique = true, nullable = false, length = 50)
    public String codigoFonte;

    @Column(nullable = false)
    public boolean ativo = true;

    @Column(name = "disponivel_free")
    public boolean disponivelFree = false;

    public Integer populacao;

    // Método helper para nome exibição
    public String getNomeCompleto() {
        if (distrito != null && !distrito.equals(municipio)) {
            return municipio + " (" + distrito + ")";
        }
        return municipio;
    }
}