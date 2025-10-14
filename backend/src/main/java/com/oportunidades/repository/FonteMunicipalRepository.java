package com.oportunidades.repository;

import com.oportunidades.entity.FonteMunicipal;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FonteMunicipalRepository implements PanacheRepository<FonteMunicipal> {

    public List<FonteMunicipal> findAllAtivas() {
        return list("ativo = true", Sort.by("municipio"));
    }

    public Optional<FonteMunicipal> findByCodigo(String codigoFonte) {
        return find("codigoFonte", codigoFonte).firstResultOptional();
    }

    public List<FonteMunicipal> findByDistrito(String distrito) {
        return list("distrito = ?1 AND ativo = true", Sort.by("municipio"), distrito);
    }

    public List<FonteMunicipal> findDisponiveisFree() {
        return list("disponivelFree = true AND ativo = true", Sort.by("municipio"));
    }

    public List<FonteMunicipal> findTop5Populacao() {
        return findAll(Sort.by("populacao").descending())
                .page(0, 5)
                .list();
    }
}