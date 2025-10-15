package com.oportunidades.resource;

import com.oportunidades.dto.LoginRequest;
import com.oportunidades.dto.LoginResponse;
import com.oportunidades.dto.SignupRequest;
import com.oportunidades.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public LoginResponse login(LoginRequest request) {
        return authService.login(request);
    }

    @POST
    @Path("/signup")
    public Response signup(
            SignupRequest request,
            @HeaderParam("X-Forwarded-For") String xForwardedFor,
            @HeaderParam("X-Real-IP") String xRealIp,
            @HeaderParam("CF-Connecting-IP") String cfConnectingIp
    ) {
        String ipAddress = extractIpAddress(xForwardedFor, xRealIp, cfConnectingIp);
        authService.signup(request, ipAddress);
        return Response.status(Response.Status.CREATED)
                .entity(Map.of("message", "Cadastro realizado com sucesso"))
                .build();
    }

    private String extractIpAddress(String xForwardedFor, String xRealIp, String cfConnectingIp) {
        if (cfConnectingIp != null && !cfConnectingIp.isEmpty()) {
            return cfConnectingIp;
        }
        
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        return "unknown";
    }
}