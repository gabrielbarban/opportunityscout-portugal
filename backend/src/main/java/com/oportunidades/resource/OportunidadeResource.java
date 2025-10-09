package com.oportunidades.resource;

import com.oportunidades.entity.Oportunidade;
import com.oportunidades.service.OportunidadeService;
import com.oportunidades.service.ScraperService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/api/oportunidades")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "USER"})
public class OportunidadeResource {

    @Inject
    OportunidadeService oportunidadeService;

    @Inject
    ScraperService scraperService;

    @GET
    public List<Oportunidade> listarTodas(
            @QueryParam("fonte") String fonte,
            @QueryParam("categoria") String categoria,
            @QueryParam("search") String search
    ) {
        if (fonte != null && !fonte.isEmpty()) {
            return oportunidadeService.listarPorFonte(fonte);
        }
        if (categoria != null && !categoria.isEmpty()) {
            return oportunidadeService.listarPorCategoria(categoria);
        }
        if (search != null && !search.isEmpty()) {
            return oportunidadeService.buscarPorTitulo(search);
        }
        return oportunidadeService.listarTodas();
    }

    @POST
    @Path("/coletar")
    @RolesAllowed("ADMIN")
    public Response coletar(@QueryParam("fonte") String fonte) {
        String resultado;
        
        if (fonte == null || fonte.isEmpty()) {
            resultado = scraperService.coletarTodasFontes();
        } else if ("PORTUGAL2030".equals(fonte)) {
            int count = scraperService.coletarPortugal2030();
            resultado = "Coletadas " + count + " oportunidades do Portugal2030";
        } else if ("COMPETE2030".equals(fonte)) {
            int count = scraperService.coletarCompete2030();
            resultado = "Coletadas " + count + " oportunidades do Compete2030";
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Fonte inválida"))
                    .build();
        }

        return Response.ok(Map.of("message", resultado)).build();
    }
}