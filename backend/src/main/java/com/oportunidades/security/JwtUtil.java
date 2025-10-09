package com.oportunidades.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.util.Set;

@ApplicationScoped
public class JwtUtil {

    @ConfigProperty(name = "jwt.duration")
    Long duration;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    public String generateToken(String email, String role) {
        return Jwt.issuer(issuer)
                .subject(email)
                .groups(Set.of(role))
                .expiresIn(duration)
                .sign();
    }
}