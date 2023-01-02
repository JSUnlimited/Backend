package de.hsos.swa.suche.boundary.rs.mobile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.hsos.swa.suche.boundary.rs.DTOs.Converter;
import de.hsos.swa.suche.boundary.rs.DTOs.WareDTO;
import de.hsos.swa.suche.control.SucheService;
import de.hsos.swa.suche.entity.Ware;

@OpenAPIDefinition(tags = {
        @Tag(name = "Search", description = "Search Operations")
}, info = @Info(title = "Search API", version = "1", contact = @Contact(name = "Projektarbeit", url = "hs-osnabrueck.de", email = "julian.schramm@hs-osnabrueck.de, mick.hurrelbrink@hs-osnabrueck.de")))
@Path("/baumarkt-shop")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class SucheResource {

    @Inject
    SucheService service;

    /*
     * Implementierung
     *
     * @author Julian Schramm
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Operation(summary = "Show all articles", description = "Show all articles that are offered")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "204", description = "No Content, no catalog available")
    })
    @GET
    @Path("/katalog")
    @PermitAll
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback1")
    @Counted(name = "searchCounter", description = "How many call for all articles have been performed")
    @Timed(name = "searchTimer", description = "How long it takes to search for all articles")
    public Response warenkatalog() {
        Collection<Ware> waren = service.warenkatalogAnfordern();
        if (waren != null) {
            Collection<WareDTO> dtos = waren.stream().map(Converter::wareToDTO).collect(Collectors.toList());
            return Response.ok().entity(dtos).build();
        }
        return Response.noContent().build();
    }

    /*
     * Implementierung
     *
     * @author Julian Schramm
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Operation(summary = "Show article by id", description = "Show article with given id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "204", description = "No Content, no article with this id available")
    })
    @GET
    @Path("/id/{warenId}")
    @PermitAll
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback2")
    @Counted(name = "searchOfIdCounter", description = "How many searchs for article id have been performed")
    @Timed(name = "searchOfIdTimer", description = "How long it takes to search for article id")
    public Response wareSuchenNachId(@PathParam("warenId") long warenId) {
        Ware ware = service.wareSuchenNachId(warenId);
        if (ware != null) {
            return Response.ok().entity(Converter.wareToDTO(ware)).build();
        }
        return Response.noContent().build();
    }

    /*
     * Implementierung
     *
     * @author Julian Schramm
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Operation(summary = "Show article by name", description = "Show article with given name regardless of the position of the name")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "204", description = "No Content, no article with this name available")
    })
    @GET
    @Path("/name/{warenName}")
    @PermitAll
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback3")
    @Counted(name = "searchForNameCounter", description = "How many searchs for article name have been performed")
    @Timed(name = "searchForNameTimer", description = "How long it takes to search for article name")
    public Response wareSuchenNachName(@PathParam("warenName") String warenName) {
        Ware[] ware = service.wareSuchenNachName(warenName);
        if (ware != null) {
            ArrayList<WareDTO> dtos = new ArrayList<>();
            for (int i = 0; i < ware.length; i++) {
                dtos.add(Converter.wareToDTO(ware[i]));
            }
            return Response.ok().entity(dtos).build();
        }
        return Response.noContent().build();
    }

    /* Fallbackmethoden */
    @SuppressWarnings("unused")
    private Response fallback1() {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback2(long warenId) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback3(String warenName) {
        return Response.status(503, "Service temporarily unavailable").build();
    }
}