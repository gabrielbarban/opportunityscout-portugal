package com.oportunidades.service;

import com.oportunidades.entity.Oportunidade;
import com.oportunidades.repository.OportunidadeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class OportunidadeService {

    @Inject
    OportunidadeRepository oportunidadeRepository;

    public List<Oportunidade> listarTodas() {
        return oportunidadeRepository.findAllOrdered();
    }

    public List<Oportunidade> listarPorFonte(String fonte) {
        return oportunidadeRepository.findByFonte(Oportunidade.Fonte.valueOf(fonte));
    }

    public List<Oportunidade> listarPorCategoria(String categoria) {
        return oportunidadeRepository.findByCategoria(categoria);
    }

    public List<Oportunidade> buscarPorTitulo(String keyword) {
        return oportunidadeRepository.searchByTitulo(keyword);
    }
}