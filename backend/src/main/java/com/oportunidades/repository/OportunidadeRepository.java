package com.oportunidades.repository;

import com.oportunidades.entity.Oportunidade;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OportunidadeRepository implements PanacheRepository<Oportunidade> {

    public List<Oportunidade> findAllOrdered() {
        return listAll(Sort.descending("criadoEm"));
    }

    public List<Oportunidade> findByFonte(Oportunidade.Fonte fonte) {
        return list("fonte", Sort.descending("criadoEm"), fonte);
    }

    public List<Oportunidade> findByCategoria(String categoria) {
        return list("categoria", Sort.descending("criadoEm"), categoria);
    }

    public List<Oportunidade> searchByTitulo(String keyword) {
        return list("LOWER(titulo) LIKE LOWER(?1)", Sort.descending("criadoEm"), "%" + keyword + "%");
    }

    public Optional<Oportunidade> findByLink(String link) {
        return find("link", link).firstResultOptional();
    }

    public boolean existsByLink(String link) {
        return count("link", link) > 0;
    }
}