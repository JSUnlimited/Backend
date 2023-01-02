package de.hsos.swa.verwaltung.boundary.rs.mobile;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

import de.hsos.swa.verwaltung.boundary.rs.DTOs.Converter;
import de.hsos.swa.verwaltung.boundary.rs.DTOs.KundeDTO;
import de.hsos.swa.verwaltung.boundary.rs.DTOs.MitarbeiterDTO;
import de.hsos.swa.verwaltung.control.VerwaltungService;
import de.hsos.swa.verwaltung.control.WarenkorbService;
import de.hsos.swa.verwaltung.entity.benutzer.Administrator;
import de.hsos.swa.verwaltung.entity.benutzer.Adresse;
import de.hsos.swa.verwaltung.entity.benutzer.Kunde;
import de.hsos.swa.verwaltung.entity.benutzer.Mitarbeiter;
import de.hsos.swa.verwaltung.boundary.rs.DTOs.AdresseDTO;
import de.hsos.swa.verwaltung.boundary.rs.DTOs.BildDTO;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@OpenAPIDefinition(tags = {
        @Tag(name = "Management", description = "Management Operations")
}, info = @Info(title = "Management API", version = "1", contact = @Contact(name = "Projektarbeit", url = "hs-osnabrueck.de", email = "julian.schramm@hs-osnabrueck.de, mick.hurrelbrink@hs-osnabrueck.de")))
@Path("/baumarkt-shop")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class VerwaltungResource {

    @Inject
    VerwaltungService service;

    @Inject
    WarenkorbService wservice;

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Operation(summary = "Regist new employee", description = "Add new employee in the table Mitarbeiter")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Employee registration failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Admin not authorised"),
    })
    @POST
    @Path("/admin")
    @Transactional
    @RolesAllowed("Admin")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(
        fallbackMethod = "fallback1",
        applyOn = {TimeoutException.class, CircuitBreakerOpenException.class}
    )
    public Response mitarbeiterRegistrieren(@Valid MitarbeiterDTO dto, @Context SecurityContext context) {
        if (!this.checkCredentialsAdmin(context)) {
            return Response.status(401, "Der Administrator ist nicht autorisiert!").build();
        }

        if (service.mitarbeiterRegistrieren(Converter.mitarbeiterDTOToClass(dto))) {
            return Response.ok().build();
        }
        return Response.status(400, "Mitarbeiterregistrierung fehlgeschlagen!").build();
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
    @Operation(summary = "Delete employee by id", description = "Delete employee from the table Mitarbeiter")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Delete employee failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Admin not authorised")
    })
    @DELETE
    @Path("/admin/{mId}")
    @Transactional
    @RolesAllowed("Admin")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback2")
    public Response mitarbeiterLoeschen(@PathParam("mId") long mId, @Context SecurityContext context) {
        if (!this.checkCredentialsAdmin(context)) {
            return Response.status(401, "Der Administrator ist nicht autorisiert!").build();
        }

        if (service.mitarbeiterLoeschen(mId)) {
            return Response.ok().build();
        }
        return Response.status(400, "Mitarbeiter konnte nicht geloescht werden!").build();
    }

    /*
     * Implementierung
     *
     * @author Julian Schramm, Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Operation(summary = "Login user", description = "Login customer or employee")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "401", description = "Unauthorised, email field is Empty"),
            @APIResponse(responseCode = "403", description = "Forbidden, no such customer or employee")
    })
    @GET
    @Path("/user")
    @PermitAll
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback3")
    public Response einloggen(@Context SecurityContext context) {
        if (context.getUserPrincipal().getName() == null) {
            return Response.status(401, "Der Kunde ist nicht autorisiert!").build();
        }
        Kunde kunde = service.kundeAbfragen(context.getUserPrincipal().getName());
        if (kunde != null) {
            return Response.ok().entity(Converter.kundeToDTO(kunde)).build();
        }
        Mitarbeiter mitarbeiter = service.mitarbeiterAbfragen(context.getUserPrincipal().getName());
        if (mitarbeiter != null) {
            return Response.ok().entity(Converter.mitarbeiterToDTO(mitarbeiter)).build();
        }
        Administrator admin = service.adminAbfragen(context.getUserPrincipal().getName());
        if (admin != null) {
            return Response.ok().entity(Converter.administratorToDTO(admin)).build();
        }
        return Response.status(403, "Kein Nutzer mit der Email vorhanden!").build();
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
    @Operation(summary = "Regist new customer", description = "Add new customer in the table Kunde")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Customer registration failed"),
    })
    @POST
    @Path("/user")
    @Transactional
    @PermitAll
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(
        fallbackMethod = "fallback4",
        applyOn = {TimeoutException.class, CircuitBreakerOpenException.class}
    )
    public Response kundeRegistrieren(@Valid KundeDTO dto) {
        if (service.kundeRegistrieren(Converter.kundeDTOToClass(dto))) {
            return Response.ok().build();
        }
        return Response.status(400, "Kundenregistrierung fehlgeschlagen").build();
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
    @Operation(summary = "Add address to customer by id", description = "Add address to a specific customer in the table Kunde")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Add customer address failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Customer not authorised")
    })
    @POST
    @Path("/kunde/{kundenId}/adresse")
    @Transactional
    @RolesAllowed("Kunde")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(
        fallbackMethod = "fallback5",
        applyOn = {TimeoutException.class, CircuitBreakerOpenException.class}
    )
    public Response adresseHinzufuegen(@PathParam("kundenId") long kundenId, @Valid AdresseDTO adresseDTO,
            @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(context)) {
            return Response.status(401, "Der Kunde ist nicht autorisiert!").build();
        }
        if (service.adresseHinzufuegen(kundenId, Converter.adresseDTOToClass(adresseDTO)) == true) {
            return Response.ok().build();
        }
        return Response.status(400, "Adresse hinzufuegen fehlgeschlagen!").build();
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
    @Operation(summary = "Edit address from customer by id", description = "Edit adress from a specific customer in the table Kunde")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Edit customer address failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Customer not authorised")
    })
    @PUT
    @Path("/kunde/{kundenId}/adresse")
    @Transactional
    @RolesAllowed("Kunde")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(
        fallbackMethod = "fallback5",
        applyOn = {TimeoutException.class, CircuitBreakerOpenException.class}
    )
    public Response adresseAendern(@PathParam("kundenId") long kundenId, @Valid AdresseDTO adresseDTO,
            @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(context)) {
            return Response.status(401, "Der Kunde ist nicht autorisiert!").build();
        }
        if (service.adresseAendern(kundenId, Converter.adresseDTOToClass(adresseDTO)) == true) {
            return Response.ok().build();
        }
        return Response.status(400, "Adressaenderung fehlgeschlagen!").build();
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
    @Operation(summary = "Delete customer by id", description = "Delete customer from table Kunde/User_Login")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Delete customer failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Customer not authorised")
    })
    @DELETE
    @Path("/kunde/{kundenId}")
    @Transactional
    @RolesAllowed("Kunde")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback2")
    public Response kundeLoeschen(@PathParam("kundenId") long kundenId, @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(context)) {
            return Response.status(401, "Der Kunde ist nicht autorisiert!").build();
        }
        if (service.kundeLoeschen(kundenId) == true) {
            return Response.ok().build();
        }
        return Response.status(400, "Kundenloeschung fehlgeschlagen!").build();
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
    @Operation(summary = "Fill up article by id", description = "Fill up the number for a specific article")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "400", description = "Bad Request, Fill up article failed"),
            @APIResponse(responseCode = "401", description = "Unauthorised, Employee not authorised")
    })
    @PUT
    @Path("/mitarbeiter/{mitarbeiterId}/waren/{warenId}")
    @Transactional
    @RolesAllowed("Mitarbeiter")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback6")
    public Response wareAuffuellen(@PathParam("warenId") long wId, @Context SecurityContext context,
            @QueryParam("menge") int menge) {
        if (!this.checkCredentialsMitarbeiter(context)) {
            return Response.status(401, "Der Mitarbeiter ist nicht autorisiert!").build();
        }
        if (service.wareAuffuellen(wId, menge)) {
            return Response.ok().build();
        }
        return Response.status(400, "Ware konnte nicht aufgefuellt werden").build();
    }

    /* Ergänzung für Webanwendung */

    @GET
    @RolesAllowed("Admin")
    @Path("/admin")
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.25)
    @Fallback(fallbackMethod = "fallback7")
    public Response mitarbeiterAnzeigen(@Context SecurityContext context) {
        if (!this.checkCredentialsAdmin(context)) {
            return Response.status(401,
                    "Der Administrator ist nicht autorisiert!").build();
        }
        Collection<Mitarbeiter> mitarbeiter = service.alleMitarbeiter();
        if (mitarbeiter != null) {
            Collection<MitarbeiterDTO> mitarbeiterDTOs = mitarbeiter.stream().map(Converter::mitarbeiterToDTO)
                    .collect(Collectors.toList());
            return Response.ok().entity(mitarbeiterDTOs).build();
        }
        return Response.status(400, "Mitarbeiter anzeigen fehlgeschlagen!").build();
    }

    @GET
    @Path("/kunde/{kundenId}/adresse")
    @RolesAllowed("Kunde")
    public Response kundenEinstellung(@PathParam("kundenId") long kundenId, @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return Response.status(401, "Der Administrator ist nicht autorisiert!").build();
        }
        Adresse kundenAdr = service.kundenAdresseAbfragen(kundenId);
        AdresseDTO dtoKundenAdr;
        if (kundenAdr == null) {
            dtoKundenAdr = new AdresseDTO("", "", "", "");
        } else {
            dtoKundenAdr = Converter.adresseToDTO(kundenAdr);
        }
        return Response.ok().entity(dtoKundenAdr).build();
    }

    private boolean checkCredentialsKunde(long kundenId, SecurityContext securityContext) {
        return wservice.kundeExistiertUndBerechtigt(kundenId, securityContext.getUserPrincipal().getName());
    }

    @GET
    @Path("/mitarbeiter/{mitarbeiterId}/waren/{warenId}/bild") 
    @RolesAllowed("Mitarbeiter")
    public Response bildVonWare(@PathParam("mitarbeiterId") long mitarbeiterId, @PathParam("warenId") long warenId, @Context SecurityContext context) {
        if (!this.checkCredentialsMitarbeiter(context)) {
            return Response.status(401, "Der Mitarbeiter ist nicht autorisiert!").build();
        }
        if (service.bildVonWare(warenId) != null) {
            BildDTO bild = new BildDTO();
            bild.bild = service.bildVonWare(warenId).getBild();
            return Response.ok().entity(bild).build();
        }
        return Response.status(400, "Bild konnte nicht gefunden werden!").build();
    }

    @PUT
    @Path("/mitarbeiter/{mitarbeiterId}/waren/{warenId}/bild") 
    @RolesAllowed("Mitarbeiter")
    @Transactional
    public Response bildZuWare(@PathParam("mitarbeiterId") long mitarbeiterId, @PathParam("warenId") long warenId, BildDTO bild, @Context SecurityContext context) {
        if (!this.checkCredentialsMitarbeiter(context)) {
            return Response.status(401, "Der Mitarbeiter ist nicht autorisiert!").build();
        }
        if (service.bildZuWare(warenId, bild.bild)) {
            return Response.ok().build();
        }
        return Response.status(400, "Bild konnte nicht hinzugefuegt werden!").build();
    }

    @DELETE
    @Path("/mitarbeiter/{mitarbeiterId}/waren/{warenId}/bild") 
    @RolesAllowed("Mitarbeiter")
    @Transactional
    public Response bildloeschen(@PathParam("mitarbeiterId") long mitarbeiterId, @PathParam("warenId") long warenId, @Context SecurityContext context){
        if (!this.checkCredentialsMitarbeiter(context)) {
            return Response.status(401, "Der Mitarbeiter ist nicht autorisiert!").build();
        }
        if (service.bildLoeschen(warenId)) {
            return Response.ok().build();
        }
        return Response.status(400, "Bild konnte nicht gelöscht werden!").build();
    }

    /* ------------------------------------------------------------------------------------------------------------------ */

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    private boolean checkCredentialsAdmin(SecurityContext securityContext) {
        return service.adminExistiert(securityContext.getUserPrincipal().getName());
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
    private boolean checkCredentialsMitarbeiter(SecurityContext securityContext) {
        return service.mitarbeiterExistiert(securityContext.getUserPrincipal().getName());
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
    private boolean checkCredentialsKunde(SecurityContext securityContext) {
        return service.kundeExistiert(securityContext.getUserPrincipal().getName());
    }

    /* Fallbackmethoden */
    @SuppressWarnings("unused")
    private Response fallback1(MitarbeiterDTO dto, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback2(long temp, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback3(SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback4(KundeDTO dto) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback5(long kundenId, AdresseDTO adresseDTO, SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback6(long wId, SecurityContext context, int menge) {
        return Response.status(503, "Service temporarily unavailable").build();
    }

    @SuppressWarnings("unused")
    private Response fallback7(SecurityContext context) {
        return Response.status(503, "Service temporarily unavailable").build();
    }
}