package com.oportunidades.entity;

public enum TipoPlano {
    FREE(1L, "FREE"),
    PREMIUM(2L, "PREMIUM"),
    ENTERPRISE(3L, "ENTERPRISE");

    public final Long id;
    public final String nome;

    TipoPlano(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static TipoPlano fromId(Long id) {
        if (id == null) return FREE;
        
        for (TipoPlano tipo : values()) {
            if (tipo.id.equals(id)) {
                return tipo;
            }
        }
        return FREE;
    }

    public static TipoPlano fromNome(String nome) {
        if (nome == null) return FREE;
        
        for (TipoPlano tipo : values()) {
            if (tipo.nome.equalsIgnoreCase(nome)) {
                return tipo;
            }
        }
        return FREE;
    }

    public boolean temAcessoMunicipal() {
        return this != FREE;
    }

    public boolean temAcessoEstatisticas() {
        return this != FREE;
    }

    public boolean temAcessoTemplates() {
        return this != FREE;
    }
}