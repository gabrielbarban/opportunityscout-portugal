package com.oportunidades.repository;

import com.oportunidades.entity.Plano;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PlanoRepository implements PanacheRepository<Plano> {

    public Optional<Plano> findByNome(String nome) {
        return find("UPPER(nome) = UPPER(?1)", nome).firstResultOptional();
    }

    public List<Plano> findAllAtivos() {
        // Por enquanto todos são ativos, mas podemos adicionar coluna "ativo" depois
        return listAll();
    }

    public Plano findFree() {
        return findById(1L);
    }

    public Plano findPremium() {
        return findById(2L);
    }

    public Plano findEnterprise() {
        return findById(3L);
    }

    public List<Plano> findPlanosCompraveis() {
        // Retorna apenas Premium e Enterprise (FREE não se "compra")
        return list("id > 1 ORDER BY id ASC");
    }
}