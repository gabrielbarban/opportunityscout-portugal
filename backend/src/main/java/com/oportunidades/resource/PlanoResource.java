package com.oportunidades.resource;

import com.oportunidades.dto.PlanoResponse;
import com.oportunidades.entity.Plano;
import com.oportunidades.service.PlanoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/planos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlanoResource {

    @Inject
    PlanoService planoService;

    /**
     * Lista todos os planos (PÚBLICO - sem autenticação)
     * Usado na página /pricing
     */
    @GET
    public List<PlanoResponse> listarTodos() {
        return planoService.listarTodos().stream()
                .map(plano -> {
                    double economia = planoService.calcularEconomiaAnual(plano);
                    return PlanoResponse.from(plano, economia);
                })
                .collect(Collectors.toList());
    }

    /**
     * Lista apenas planos compráveis (sem FREE)
     */
    @GET
    @Path("/compraveis")
    public List<PlanoResponse> listarCompraveis() {
        return planoService.listarPlanosCompraveis().stream()
                .map(plano -> {
                    double economia = planoService.calcularEconomiaAnual(plano);
                    return PlanoResponse.from(plano, economia);
                })
                .collect(Collectors.toList());
    }

    /**
     * Busca plano específico por ID
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            Plano plano = planoService.buscarPorId(id);
            double economia = planoService.calcularEconomiaAnual(plano);
            return Response.ok(PlanoResponse.from(plano, economia)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    /**
     * Classe interna para resposta de erro
     */
    public static class ErrorResponse {
        public String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}