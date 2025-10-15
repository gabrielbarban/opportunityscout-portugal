package com.oportunidades.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @Column(length = 20)
    public String telefone;

    @Roles
    @Column(nullable = false)
    public String role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plano_id")
    public Plano plano;

    @Column(name = "criado_em", nullable = false, updatable = false)
    public LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "ip_criacao", length = 50)
    public String ipCriacao;

    @Column(name = "data_assinatura")
    public LocalDateTime dataAssinatura;

    @Column(name = "data_expiracao")
    public LocalDateTime dataExpiracao;

    @Column(name = "stripe_customer_id")
    public String stripeCustomerId;

    @Column(name = "stripe_subscription_id")
    public String stripeSubscriptionId;

    public enum Role {
        ADMIN, USER
    }

    @PrePersist
    public void prePersist() {
        if (this.criadoEm == null) {
            this.criadoEm = LocalDateTime.now();
        }
    }

    public boolean isPremium() {
        if (plano == null) return false;
        if (dataExpiracao != null && dataExpiracao.isBefore(LocalDateTime.now())) {
            return false;
        }
        return plano.getNivel() >= 2;
    }

    public boolean isEnterprise() {
        if (plano == null) return false;
        if (dataExpiracao != null && dataExpiracao.isBefore(LocalDateTime.now())) {
            return false;
        }
        return plano.getNivel() >= 3;
    }

    public boolean isFree() {
        return !isPremium();
    }

    public boolean isAssinaturaAtiva() {
        if (plano == null || plano.isFree()) {
            return true;
        }
        if (dataExpiracao == null) {
            return true;
        }
        return dataExpiracao.isAfter(LocalDateTime.now());
    }

    public TipoPlano getTipoPlano() {
        if (plano == null) return TipoPlano.FREE;
        return TipoPlano.fromId(plano.id);
    }

    public boolean podeAcessarMunicipios() {
        return isPremium();
    }

    public boolean podeAcessarAlertasIlimitados() {
        return isPremium();
    }

    public int getMaxAlertas() {
        if (plano == null) return 1;
        return plano.maxAlertas;
    }

    public int getMaxMunicipios() {
        if (plano == null) return 0;
        return plano.maxMunicipios;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }

    public String getNomePlano() {
        if (plano == null) return "FREE";
        return plano.nome;
    }
}