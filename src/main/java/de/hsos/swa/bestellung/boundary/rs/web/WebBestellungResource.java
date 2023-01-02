package de.hsos.swa.bestellung.boundary.rs.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import de.hsos.swa.bestellung.boundary.rs.DTOs.BestellungDTO;
import de.hsos.swa.bestellung.boundary.rs.DTOs.Converter;
import de.hsos.swa.bestellung.control.BestellungService;
import de.hsos.swa.bestellung.entity.Bestellposten;
import de.hsos.swa.bestellung.entity.Bestellung;
import de.hsos.swa.verwaltung.control.VerwaltungService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

/*
     * Implementierung
     *
     * @author Julian Schramm, Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
@Path("/web.baumarkt-shop")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
@RequestScoped
public class WebBestellungResource {
    @Inject
    BestellungService service;

    @Inject
    VerwaltungService vService;

    @Inject
    Template error;

    @Inject
    Template errorkundemitarbeiter;

    @Inject
    Template bestellungkunde;

    @Inject
    Template bestellungDetail;

    @Inject
    Template mitarbeiter;

    @Inject
    Template bestellungmitarbeiter;

    @Inject
    Template postenBearbeiten;

    @GET
    @Path("/kunde/{kundenId}/bestellung")
    @RolesAllowed("Kunde")
    public TemplateInstance kundenBestellungenAnzeigen(@PathParam("kundenId") long kundenId, @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }
        Collection<Bestellung> alleBestellungen = service.bestellungenAnzeigen(kundenId);
        if(alleBestellungen == null) {
            return bestellungkunde.data("bestellungen", new ArrayList<BestellungDTO>());
        } 
        return bestellungkunde.data("bestellungen", alleBestellungen.stream().map(Converter::bestellungToDTO).collect(Collectors.toList()), "kunde", kundenId);
    }

    @GET
    @Path("/kunde/{kundenId}/bestellung/{bestellId}")
    @RolesAllowed("Kunde")
    public TemplateInstance kundenBestellungenImDetailAnzeigen(@PathParam("kundenId") long kundenId, @PathParam("bestellId") long bestellId, @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }
        Bestellung dieBestellung = service.eineBestellungAnzeigen(kundenId, bestellId);
        if(dieBestellung == null) {
            return bestellungDetail.data("bestellung", new BestellungDTO(), "kunde", kundenId);
        } 
        return bestellungDetail.data("bestellung", Converter.bestellungToDTO(dieBestellung), "kunde", kundenId);
    }

    @GET
    @Path("/kunde/{kundenId}/bestellung/{bestellId}/posten/{postenId}")
    @RolesAllowed("Kunde")
    public TemplateInstance kundenBestellungenImDetailAnzeigen(@PathParam("kundenId") long kundenId, @PathParam("bestellId") long bestellId, 
    @PathParam("postenId") long postenId, @Context SecurityContext context) {

        if (!this.checkCredentialsKunde(kundenId, context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }
        Bestellung dieBestellung = service.eineBestellungAnzeigen(kundenId, bestellId);
        if(dieBestellung == null) {
            return errorkundemitarbeiter.data("fehler", "Bestellung nicht vorhanden", "kunde", kundenId, "mitarbeiter", -1);
        }

        Bestellposten aktPosten = null;
        for(int i=0; i<dieBestellung.getBestellposten().size(); i++) {
            if(dieBestellung.getBestellposten().get(i).getId() == postenId) {
                aktPosten = dieBestellung.getBestellposten().get(i);
                break;
            }
        }

        if(aktPosten == null) {
            return errorkundemitarbeiter.data("fehler", "Bestellposten nicht vorhanden", "kunde", kundenId, "mitarbeiter", -1);
        }
        return postenBearbeiten.data("posten", Converter.bestellpostenToDTO(aktPosten),"bestellung", bestellId, "kunde", kundenId);
    }

    @GET
    @Path("mitarbeiter/{mitarbeiterId}/kunde/{kundenId}/bestellung")
    @RolesAllowed("Mitarbeiter")
    public TemplateInstance mitarbeiterKundenBestellungenAnzeigen(@PathParam("mitarbeiterId") long mitarbeiterId, @PathParam("kundenId") long kundenId, 
        @Context SecurityContext context) {
        if (!this.checkCredentialsMitarbeiter(mitarbeiterId, context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }
        if(vService.kundenAdresseAbfragen(kundenId) == null) {
            return errorkundemitarbeiter.data("fehler", "Kunde existiert nicht", "kunde", -1, "mitarbeiter", mitarbeiterId);
        }
        Collection<Bestellung> alleBestellungen = service.bestellungenAnzeigen(kundenId);
        if(alleBestellungen == null) {
            return bestellungmitarbeiter.data("bestellungen", new ArrayList<BestellungDTO>(), "kunde", kundenId);
        } 
        return bestellungmitarbeiter.data("bestellungen", alleBestellungen.stream().map(Converter::bestellungToDTO).collect(Collectors.toList()),
         "kunde", kundenId, "mitarbeiter", mitarbeiterId);
    }

    private boolean checkCredentialsKunde(long kundenId, SecurityContext securityContext) {
        return service.kundeExistiertUndBerechtigt(kundenId, securityContext.getUserPrincipal().getName());
    }

    private boolean checkCredentialsMitarbeiter(long mitarbeiterId, SecurityContext securityContext) {
        return service.mitarbeiterExistiertUndBerechtigt(mitarbeiterId, securityContext.getUserPrincipal().getName());
    }
}
