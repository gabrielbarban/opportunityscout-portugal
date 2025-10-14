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

    @Roles
    @Column(nullable = false)
    public String role;

    // Relacionamento com Plano
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plano_id")
    public Plano plano;

    @Column(name = "data_assinatura")
    public LocalDateTime dataAssinatura;

    @Column(name = "data_expiracao")
    public LocalDateTime dataExpiracao;

    @Column(name = "stripe_customer_id")
    public String stripeCustomerId;

    @Column(name = "stripe_subscription_id")
    public String stripeSubscriptionId;

    // Enum de roles
    public enum Role {
        ADMIN, USER
    }

    // ============================================
    // MÉTODOS HELPER - VERIFICAÇÃO DE PLANO
    // ============================================

    /**
     * Verifica se usuário tem plano PREMIUM ou superior
     */
    public boolean isPremium() {
        if (plano == null) return false;
        
        // Verifica se tem plano ativo (não expirado)
        if (dataExpiracao != null && dataExpiracao.isBefore(LocalDateTime.now())) {
            return false; // Assinatura expirada
        }
        
        return plano.getNivel() >= 2;
    }

    /**
     * Verifica se usuário tem plano ENTERPRISE
     */
    public boolean isEnterprise() {
        if (plano == null) return false;
        
        if (dataExpiracao != null && dataExpiracao.isBefore(LocalDateTime.now())) {
            return false;
        }
        
        return plano.getNivel() >= 3;
    }

    /**
     * Verifica se usuário é FREE
     */
    public boolean isFree() {
        return !isPremium();
    }

    /**
     * Verifica se assinatura está ativa (não expirou)
     */
    public boolean isAssinaturaAtiva() {
        if (plano == null || plano.isFree()) {
            return true; // FREE nunca expira
        }
        
        if (dataExpiracao == null) {
            return true; // Sem data de expiração = ativo
        }
        
        return dataExpiracao.isAfter(LocalDateTime.now());
    }

    /**
     * Retorna o tipo do plano atual
     */
    public TipoPlano getTipoPlano() {
        if (plano == null) return TipoPlano.FREE;
        return TipoPlano.fromId(plano.id);
    }

    /**
     * Verifica se pode acessar funcionalidade municipal
     */
    public boolean podeAcessarMunicipios() {
        return isPremium();
    }

    /**
     * Verifica se pode criar alertas ilimitados
     */
    public boolean podeAcessarAlertasIlimitados() {
        return isPremium();
    }

    /**
     * Retorna quantidade máxima de alertas permitidos
     */
    public int getMaxAlertas() {
        if (plano == null) return 1;
        return plano.maxAlertas;
    }

    /**
     * Retorna quantidade máxima de municípios permitidos
     */
    public int getMaxMunicipios() {
        if (plano == null) return 0;
        return plano.maxMunicipios;
    }

    /**
     * Verifica se é administrador
     */
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }

    /**
     * Retorna nome do plano para exibição
     */
    public String getNomePlano() {
        if (plano == null) return "FREE";
        return plano.nome;
    }
}