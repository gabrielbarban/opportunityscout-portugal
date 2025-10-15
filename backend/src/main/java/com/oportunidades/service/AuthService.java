package com.oportunidades.service;

import com.oportunidades.dto.LoginRequest;
import com.oportunidades.dto.LoginResponse;
import com.oportunidades.dto.SignupRequest;
import com.oportunidades.entity.Usuario;
import com.oportunidades.repository.PlanoRepository;
import com.oportunidades.repository.UsuarioRepository;
import com.oportunidades.security.JwtUtil;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.regex.Pattern;

@ApplicationScoped
public class AuthService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PlanoRepository planoRepository;

    @Inject
    JwtUtil jwtUtil;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email)
                .orElseThrow(() -> new WebApplicationException("Credenciais inválidas", Response.Status.UNAUTHORIZED));

        if (!BcryptUtil.matches(request.password, usuario.password)) {
            throw new WebApplicationException("Credenciais inválidas", Response.Status.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(usuario.email, usuario.role);
        return new LoginResponse(token, usuario.email, usuario.nome, usuario.role);
    }

    @Transactional
    public void signup(SignupRequest request, String ipAddress) {
        if (request.nome == null || request.nome.trim().isEmpty()) {
            throw new WebApplicationException("Nome é obrigatório", Response.Status.BAD_REQUEST);
        }

        if (request.email == null || !EMAIL_PATTERN.matcher(request.email).matches()) {
            throw new WebApplicationException("Email inválido", Response.Status.BAD_REQUEST);
        }

        if (request.password == null || request.password.length() < 6) {
            throw new WebApplicationException("Senha deve ter no mínimo 6 caracteres", Response.Status.BAD_REQUEST);
        }

        if (!request.password.equals(request.confirmPassword)) {
            throw new WebApplicationException("Senhas não coincidem", Response.Status.BAD_REQUEST);
        }

        if (usuarioRepository.existsByEmail(request.email)) {
            throw new WebApplicationException("Email já cadastrado", Response.Status.CONFLICT);
        }

        Usuario usuario = new Usuario();
        usuario.nome = request.nome.trim();
        usuario.email = request.email.toLowerCase().trim();
        usuario.telefone = request.telefone != null ? request.telefone.trim() : null;
        usuario.password = BcryptUtil.bcryptHash(request.password);
        usuario.role = "USER";
        usuario.plano = planoRepository.findFree();
        usuario.ipCriacao = ipAddress;

        usuarioRepository.persist(usuario);
    }
}