package com.oportunidades.service;

import com.oportunidades.entity.Plano;
import com.oportunidades.repository.PlanoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class PlanoService {

    @Inject
    PlanoRepository planoRepository;

    /**
     * Lista todos os planos disponíveis
     */
    public List<Plano> listarTodos() {
        return planoRepository.findAllAtivos();
    }

    /**
     * Lista apenas planos compráveis (exclui FREE)
     */
    public List<Plano> listarPlanosCompraveis() {
        return planoRepository.findPlanosCompraveis();
    }

    /**
     * Busca plano por ID
     */
    public Plano buscarPorId(Long id) {
        return planoRepository.findByIdOptional(id)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado: " + id));
    }

    /**
     * Busca plano por nome
     */
    public Plano buscarPorNome(String nome) {
        return planoRepository.findByNome(nome)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado: " + nome));
    }

    /**
     * Retorna plano FREE
     */
    public Plano getPlanoFree() {
        return planoRepository.findFree();
    }

    /**
     * Retorna plano PREMIUM
     */
    public Plano getPlanoPremium() {
        return planoRepository.findPremium();
    }

    /**
     * Retorna plano ENTERPRISE
     */
    public Plano getPlanoEnterprise() {
        return planoRepository.findEnterprise();
    }

    /**
     * Calcula economia anual (%)
     */
    public double calcularEconomiaAnual(Plano plano) {
        if (plano.precoMensal == null || plano.precoAnual == null) {
            return 0;
        }
        
        double custoMensal12x = plano.precoMensal.doubleValue() * 12;
        double custoAnual = plano.precoAnual.doubleValue();
        
        if (custoMensal12x == 0) return 0;
        
        double economia = ((custoMensal12x - custoAnual) / custoMensal12x) * 100;
        return Math.round(economia * 100.0) / 100.0; // Arredondar 2 casas
    }
}