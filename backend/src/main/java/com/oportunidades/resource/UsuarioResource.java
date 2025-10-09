package com.oportunidades.resource;

import com.oportunidades.dto.UsuarioRequest;
import com.oportunidades.dto.UsuarioResponse;
import com.oportunidades.service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    @GET
    public List<UsuarioResponse> listarTodos() {
        return usuarioService.listarTodos();
    }

    @POST
    public Response criar(UsuarioRequest request) {
        UsuarioResponse response = usuarioService.criar(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    public UsuarioResponse atualizar(@PathParam("id") Long id, UsuarioRequest request) {
        return usuarioService.atualizar(id, request);
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        usuarioService.deletar(id);
        return Response.noContent().build();
    }
}