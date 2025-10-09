package com.oportunidades.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
@UserDefinition
public class Usuario extends PanacheEntity {

    @Username
    @Column(unique = true, nullable = false)
    public String email;

    @Password
    @Column(nullable = false)
    public String password;

    @Column(nullable = false)
    public String nome;

    @Roles
    @Column(nullable = false)
    public String role;

    public enum Role {
        ADMIN, USER
    }
}