package com.oportunidades.service;

import com.oportunidades.dto.LoginRequest;
import com.oportunidades.dto.LoginResponse;
import com.oportunidades.entity.Usuario;
import com.oportunidades.repository.UsuarioRepository;
import com.oportunidades.security.JwtUtil;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AuthService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email)
                .orElseThrow(() -> new WebApplicationException("Credenciais inválidas", Response.Status.UNAUTHORIZED));

        if (!BcryptUtil.matches(request.password, usuario.password)) {
            throw new WebApplicationException("Credenciais inválidas", Response.Status.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(usuario.email, usuario.role);
        return new LoginResponse(token, usuario.email, usuario.nome, usuario.role);
    }
}