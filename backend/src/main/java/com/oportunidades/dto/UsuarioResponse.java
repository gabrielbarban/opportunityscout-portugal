package com.oportunidades.dto;

public class UsuarioResponse {
    public Long id;
    public String email;
    public String nome;
    public String role;

    public UsuarioResponse(Long id, String email, String nome, String role) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.role = role;
    }
}