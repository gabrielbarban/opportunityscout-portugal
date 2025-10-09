package com.oportunidades.dto;

public class LoginResponse {
    public String token;
    public String email;
    public String nome;
    public String role;

    public LoginResponse(String token, String email, String nome, String role) {
        this.token = token;
        this.email = email;
        this.nome = nome;
        this.role = role;
    }
}