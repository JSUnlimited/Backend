package de.hsos.swa.verwaltung.boundary.rs.mobile;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.hsos.swa.verwaltung.boundary.rs.DTOs.Converter;
import de.hsos.swa.verwaltung.boundary.rs.DTOs.WarenkorbImportElementDTO;
import de.hsos.swa.verwaltung.control.WarenkorbService;
import de.hsos.swa.verwaltung.entity.Ware;
import de.hsos.swa.verwaltung.entity.warenkorb.Warenkorb;

@OpenAPIDefinition(tags = {
        @Tag(name = "Shopping Cart", description = "Shopping Cart Operations")
}, info = @Info(title = "Shopping Cart API", version = "1", contact = @Contact(name = "Projektarbeit", url = "hs-osnabrueck.de", email = "julian.schramm@hs-osnabrueck.de, mick.hurrelbrink@hs-osnabrueck.de")))
@Path("/baumarkt-shop/kunde/{kundenId}/warenkorb")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class WarenkorbResource {

    @Inject
    WarenkorbService service;

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Operation(summary = "Show shopping cart from customer by id", description = "Show shopping cart from a specific customer")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Show shopping cart failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Customer not authorised")
    })
    @GET
    @RolesAllowed("Kunde")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback1")
    public Response warenkorbAnzeigen(@PathParam("kundenId") long kundenId, @Context SecurityContext context) {
        Warenkorb warenkorb = service.warenkorbAnzeigen(kundenId);
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return Response.status(401, "Der Kunde ist nicht autorisiert!").build();
        }

        if (warenkorb != null) {
            return Response.ok().entity(Converter.warenkorbToDTO(warenkorb)).build();
        }
        return Response.status(400, "Anzeigen vom Warenkorb fehlgeschalgen!").build();
    }

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Operation(summary = "Add article to a shopping cart from customer by id", description = "Add article to a shopping cart from a specific customer")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Add item to shopping cart failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Customer not authorised")
    })
    @PUT
    @RolesAllowed("Kunde")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback2")
    public Response wareHinzufuegen(@PathParam("kundenId") long kundenId, WarenkorbImportElementDTO dto,
            @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return Response.status(401, "Der Kunde ist nicht autorisiert!").build();
        }

        if (service.wareHinzufuegen(kundenId, dto.warenId, dto.menge)) {
            return Response.ok().build();
        }
        return Response.status(400, "Element zum Warenkorb hinzufuegen fehlgeschlagen!").build();
    }

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Operation(summary = "Order shopping cart from customer by id", description = "Order shopping cart from a specific customer")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Order shopping cart failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Customer not authorised"),
            @APIResponse(responseCode = "409", description = "Conflict, Article/s not available")
    })
    @POST
    @RolesAllowed("Kunde")
    @Transactional
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback1")
    public Response warenkorbBestellen(@PathParam("kundenId") long kundenId, @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return Response.status(401, "Der Kunde ist nicht autorisiert!").build();
        }

        List<Ware> fehlen = service.warenkorbBestellen(kundenId);
        if (fehlen == null) {
            return Response.status(400, "Warenkorb bestellen fehlgeschalgen!").build();
        }

        if (fehlen.isEmpty()) {
            return Response.ok().build();
        } else {
            return Response.status(409, "Ware/-n nicht verfuegbar!")
                    .entity(fehlen.stream().map(Converter::wareToDTO).collect(Collectors.toList())).build();
        }
    }

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    private boolean checkCredentialsKunde(long kundenId, SecurityContext securityContext) {
        return service.kundeExistiertUndBerechtigt(kundenId, securityContext.getUserPrincipal().getName());
    }

    /* Fallbackmethoden */
    @SuppressWarnings("unused")
    private Response fallback1(long kundenId, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback2(long kundenId, WarenkorbImportElementDTO dto, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }
}
