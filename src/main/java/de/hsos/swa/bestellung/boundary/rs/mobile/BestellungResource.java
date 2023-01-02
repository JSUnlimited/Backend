package de.hsos.swa.bestellung.boundary.rs.mobile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.hsos.swa.bestellung.boundary.rs.DTOs.BestellpostenDTO;
import de.hsos.swa.bestellung.boundary.rs.DTOs.BestellungDTO;
import de.hsos.swa.bestellung.boundary.rs.DTOs.Converter;
import de.hsos.swa.bestellung.control.BestellungService;
import de.hsos.swa.bestellung.entity.Bestellung;

@OpenAPIDefinition(tags = {
        @Tag(name = "Order", description = "Order Operations")
}, info = @Info(title = "Order API", version = "1", contact = @Contact(name = "Projektarbeit", url = "hs-osnabrueck.de", email = "julian.schramm@hs-osnabrueck.de, mick.hurrelbrink@hs-osnabrueck.de")))
@Path("/baumarkt-shop")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class BestellungResource {

    @Inject
    BestellungService bservice;

    /*
     * Implementierung
     *
     * @author Julian Schramm
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Operation(summary = "Show one order from customer by id", description = "Show one order from a specific customer")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Order does not exist"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Employee not authorised")
    })
    @GET
    @RolesAllowed("Mitarbeiter")
    @Path("/mitarbeiter/{mitarbeiterId}/kunde/{kundenId}/bestellung/{bestellungId}")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback1")
    public Response bestellungAnzeigen(@PathParam("mitarbeiterId") long mitarbeiterId,
            @PathParam("kundenId") long kundenId, @PathParam("bestellungId") long bestellungId,
            @Context SecurityContext context) {

        if (!this.checkCredentialsMitarbeiter(mitarbeiterId, context)) {
            return Response.status(401,
                    "Der Mitarbeiter ist nicht autorisiert!").build();
        }

        Bestellung bestellung = bservice.bestellungAnzeigen(bestellungId);
        if (bestellung != null) {
            BestellungDTO bestellungDTO = Converter.bestellungToDTO(bestellung);
            return Response.ok().entity(bestellungDTO).build();
        }
        return Response.status(400, "Die Bestellung existiert nicht!").build();
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
    @Operation(summary = "Show all orders from customer by id", description = "Show all order from a specific customer")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Show customer orders failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Employee not authorised")
    })
    @GET
    @RolesAllowed("Mitarbeiter")
    @Path("/mitarbeiter/{mitarbeiterId}/kunde/{kundenId}/bestellung")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback2")
    public Response bestellungenAnzeigen(@PathParam("mitarbeiterId") long mitarbeiterId,
            @PathParam("kundenId") long kundenId, @Context SecurityContext context) {

        if (!this.checkCredentialsMitarbeiter(mitarbeiterId, context)) {
            return Response.status(401,
                    "Der Mitarbeiter ist nicht autorisiert!").build();
        }

        Collection<Bestellung> bestellungen = bservice.bestellungenAnzeigen(kundenId);
        if (bestellungen != null) {
            Collection<BestellungDTO> bestellungDTOs = bestellungen.stream().map(Converter::bestellungToDTO)
                    .collect(Collectors.toList());
            return Response.ok().entity(bestellungDTOs).build();
        }
        return Response.status(400, "Kundenbestellungen anzeigen fehlgeschlagen!").build();
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
    @Operation(summary = "Change date of delivery from order by id", description = "Change date of delivery from a specific order")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Delivery date change failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Employee not authorised"),
            @APIResponse(responseCode = "409", description = "Conflict, Order does not exist")
    })
    @PUT
    @Transactional
    @RolesAllowed("Mitarbeiter")
    @Path("/mitarbeiter/{mitarbeiterId}/kunde/{kundenId}/bestellung/{bestellungId}/liefertermin")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback4", applyOn = { TimeoutException.class, CircuitBreakerOpenException.class })
    public Response lieferterminAendern(@PathParam("mitarbeiterId") long mitarbeiterId,
            @PathParam("kundenId") long kundenId, @PathParam("bestellungId") long bestellungId,
            @QueryParam("liefertermin") String liefertermin, @Context SecurityContext context) {

        if (!this.checkCredentialsMitarbeiter(mitarbeiterId, context)) {
            return Response.status(401,
                    "Der Mitarbeiter ist nicht autorisiert!").build();
        }

        Bestellung bestellung = bservice.bestellungAnzeigen(bestellungId);
        if (bestellung != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                if (bservice.lieferterminAendern(kundenId, bestellungId,
                        LocalDate.parse(liefertermin, formatter)) == true) {
                    return Response.ok().build();
                }
            } catch (DateTimeParseException e) {
                return Response.status(400, "Lieferterminaenderung fehlgeschlagen!").build();
            }
            return Response.status(400, "Lieferterminaenderung fehlgeschlagen!").build();
        }
        return Response.status(409, "Die Bestellung existiert nicht!").build();
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
    @Operation(summary = "Change state to VERSENDET from order by id", description = "Change state to VERSENDET from a specific order")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Order does not exist"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Employee not authorised"),
            @APIResponse(responseCode = "409", description = "Conflict, No date entered")

    })
    @PUT
    @Transactional
    @RolesAllowed("Mitarbeiter")
    @Path("/mitarbeiter/{mitarbeiterId}/kunde/{kundenId}/bestellung/{bestellungId}/versendet")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback3")
    public Response bestellungVersendet(@PathParam("mitarbeiterId") long mitarbeiterId,
            @PathParam("kundenId") long kundenId, @PathParam("bestellungId") long bestellungId,
            @Context SecurityContext context) {

        if (!this.checkCredentialsMitarbeiter(mitarbeiterId, context)) {
            return Response.status(401,
                    "Der Mitarbeiter ist nicht autorisiert!").build();
        }

        Bestellung bestellung = bservice.bestellungAnzeigen(bestellungId);
        if (bestellung != null) {
            if (bservice.bestellungVersendet(kundenId, bestellungId)) {
                return Response.ok().build();
            }
            return Response.status(409, "Kein Datum eingetragen!").build();
        }
        return Response.status(400, "Die Bestellung existiert nicht!").build();
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
    @Operation(summary = "Show all orders from customer by id", description = "Show all orders from a specific customer")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Show all orders from one customer fails"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Customer not authorised")
    })
    @GET
    @Path("kunde/{kundenId}/bestellung")
    @RolesAllowed("Kunde")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback5")
    public Response alleBestellungenEinesKunden(@PathParam("kundenId") long kundenId,
            @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return Response.status(401, "Der Kunde ist nicht autorisiert!").build();
        }
        Collection<Bestellung> bestellungen = bservice.bestellungenEinesKundenAnzeigen(kundenId);
        if (bestellungen != null) {
            Collection<BestellungDTO> dtos = bestellungen.stream().map(Converter::bestellungToDTO)
                    .collect(Collectors.toList());
            return Response.ok().entity(dtos).build();
        }
        return Response.status(400, "Anzeigen aller Bestellungen eines Kundens fehlgeschlagen!").build();
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
    @Operation(summary = "Show one order from customer by id", description = "Show a specific order from a specific customer")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Show one orders from one customer fails"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Customer not authorised"),
    })
    @GET
    @Path("kunde/{kundenId}/bestellung/{bestellId}")
    @RolesAllowed("Kunde")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback6")
    public Response eineBestellungEinesKunden(@PathParam("kundenId") long kundenId,
            @PathParam("bestellId") long bestellId, @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return Response.status(401, "Der Kunde ist nicht autorisiert!").build();
        }
        Bestellung bestellung = bservice.eineBestellungAnzeigen(kundenId, bestellId);
        if (bestellung != null) {
            return Response.ok().entity(Converter.bestellungToDTO(bestellung)).build();
        }
        return Response.ok().build();
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
    @Operation(summary = "Delete one order for customer by id", description = "Delete an order from the customer")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, No such order or customer"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Customer not authorised"),
            @APIResponse(responseCode = "409", description = "Conflict, State of order not NEU"),
    })
    @DELETE
    @Path("kunde/{kundenId}/bestellung/{bestellId}")
    @RolesAllowed("Kunde")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback7")
    @Transactional
    public Response bestellungLoeschen(@PathParam("kundenId") long kundenId,
            @PathParam("bestellId") long bestellId, @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return Response.status(401, "Der Kunde ist nicht autorisiert!").build();
        }
        Bestellung bestellung = bservice.eineBestellungAnzeigen(kundenId, bestellId);
        if (bestellung == null) {
            return Response.status(400, "Loeschen der Bestellung fehlgeschlagen!").build();
        }
        if (bservice.bestellungLoeschen(kundenId, bestellId)) {
            return Response.ok().build();
        } else {
            return Response.status(409, "Bestellung bereits bearbeitet durch Mitarbeiter!").build();
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
    @Operation(summary = "Edit an order item from order by id", description = "Edit an order item from a specific order")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Change the order item failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Customer not authorised")
    })
    @PUT
    @Path("kunde/{kundenId}/bestellung/{bestellId}/posten")
    @RolesAllowed("Kunde")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback8")
    @Transactional
    public Response bestellpostenBearbeiten(@PathParam("kundenId") long kundenId,
            @PathParam("bestellId") long bestellId,
            BestellpostenDTO dto, @Context SecurityContext context) {

        if (!this.checkCredentialsKunde(kundenId, context)) {
            return Response.status(401, "Der Kunde ist nicht autorisiert!").build();
        }
        if (bservice.bestellpostenBearbeiten(kundenId, bestellId, Converter.bestellpostenToClass(dto))) {
            return Response.ok().build();
        }

        return Response.status(400, "Aenderung des Bestellpostens fehlgeschlagen!").build();
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
    @Operation(summary = "Delete an order item from order by id", description = "Delete an order item from a specific order")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Delete order item failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Customer not authorised")
    })
    @DELETE
    @Path("kunde/{kundenId}/bestellung/{bestellId}/posten/{postenId}")
    @RolesAllowed("Kunde")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback9")
    @Transactional
    public Response bestellpostenEntfernen(@PathParam("kundenId") long kundenId, @PathParam("bestellId") long bestellId,
            @PathParam("postenId") long postenId, @Context SecurityContext context) {

        if (!this.checkCredentialsKunde(kundenId, context)) {
            return Response.status(401, "Der Kunde ist nicht autorisiert!").build();
        }
        if (bservice.bestellpostenLoeschen(kundenId, bestellId, postenId)) {
            return Response.ok().build();
        }

        return Response.status(400, "Entfernen des Bestellpostens fehlgeschlagen!").build();
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
    private boolean checkCredentialsMitarbeiter(long mitarbeiterId, SecurityContext securityContext) {
        return bservice.mitarbeiterExistiertUndBerechtigt(mitarbeiterId, securityContext.getUserPrincipal().getName());
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
        return bservice.kundeExistiertUndBerechtigt(kundenId, securityContext.getUserPrincipal().getName());
    }

    @SuppressWarnings("unused")
    private Response fallback1(long mitarbeiterId, long kundenId, long bestellungId, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback2(long mitarbeiterId, long kundenId, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback3(long mitarbeiterId, long kundenId, long bestellungId, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback4(long mitarbeiterId, long kundenId, long bestellungId, String liefertermin,
            SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback5(long kundenId, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback6(long kundenId, long bestellId, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback7(long kundenId, long bestellId, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback8(long kundenId, long bestellId, BestellpostenDTO dto, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback9(long kundenId, long bestellId, long postenId, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }
}