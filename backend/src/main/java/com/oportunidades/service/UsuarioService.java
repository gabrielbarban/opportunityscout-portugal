package com.oportunidades.service;

import com.oportunidades.dto.UsuarioRequest;
import com.oportunidades.dto.UsuarioResponse;
import com.oportunidades.entity.Usuario;
import com.oportunidades.repository.UsuarioRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.listAll().stream()
                .map(u -> new UsuarioResponse(u.id, u.email, u.nome, u.role))
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email)) {
            throw new WebApplicationException("Email já cadastrado", Response.Status.CONFLICT);
        }

        Usuario usuario = new Usuario();
        usuario.email = request.email;
        usuario.password = BcryptUtil.bcryptHash(request.password);
        usuario.nome = request.nome;
        usuario.role = request.role;
        
        usuarioRepository.persist(usuario);
        return new UsuarioResponse(usuario.id, usuario.email, usuario.nome, usuario.role);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND));

        if (!usuario.email.equals(request.email) && usuarioRepository.existsByEmail(request.email)) {
            throw new WebApplicationException("Email já cadastrado", Response.Status.CONFLICT);
        }

        usuario.email = request.email;
        usuario.nome = request.nome;
        usuario.role = request.role;
        
        if (request.password != null && !request.password.isEmpty()) {
            usuario.password = BcryptUtil.bcryptHash(request.password);
        }

        usuarioRepository.persist(usuario);
        return new UsuarioResponse(usuario.id, usuario.email, usuario.nome, usuario.role);
    }

    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.deleteById(id)) {
            throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
        }
    }
}