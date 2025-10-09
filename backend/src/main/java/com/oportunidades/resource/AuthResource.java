package com.oportunidades.resource;

import com.oportunidades.dto.LoginRequest;
import com.oportunidades.dto.LoginResponse;
import com.oportunidades.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

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
}