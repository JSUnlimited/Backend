package de.hsos.swa.verwaltung.boundary.rs.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
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

import de.hsos.swa.verwaltung.boundary.rs.DTOs.AdresseDTO;
import de.hsos.swa.verwaltung.boundary.rs.DTOs.Converter;
import de.hsos.swa.verwaltung.boundary.rs.DTOs.MitarbeiterDTO;
import de.hsos.swa.verwaltung.boundary.rs.DTOs.WareDTO;
import de.hsos.swa.verwaltung.control.VerwaltungService;
import de.hsos.swa.verwaltung.control.WarenkorbService;
import de.hsos.swa.verwaltung.entity.Ware;
import de.hsos.swa.verwaltung.entity.benutzer.Adresse;
import de.hsos.swa.verwaltung.entity.benutzer.Kunde;
import de.hsos.swa.verwaltung.entity.benutzer.Mitarbeiter;
import de.hsos.swa.verwaltung.entity.warenkorb.Warenkorb;
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
public class WebVerwaltungWarenkorbResource {
    @Inject
    VerwaltungService vservice;

    @Inject
    WarenkorbService wservice;

    @Inject
    Template kunde;

    @Inject
    Template adresse;

    @Inject
    Template warenkorb;

    @Inject
    Template admin;

    @Inject
    Template mitarbeiter;

    @Inject
    Template login;

    @Inject
    Template adresseBearbeiten;

    @Inject
    Template bestandAuffuellen;

    @Inject
    Template mitarbeiterHinzufuegen;

    @Inject
    Template registrieren;

    @Inject
    Template error;

    @Inject
    Template adresseHinzufuegen;

    @Inject
    Template errorkundemitarbeiter;

    @GET
    @Path("/kunde/{kundenId}")
    @RolesAllowed("Kunde")
    public TemplateInstance kundeStartAnsicht(@PathParam("kundenId") long kundenId, @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }

        Collection<Ware> alleWaren = vservice.alleWarenAnzeigen();
        if (alleWaren == null) {
            return kunde.data("waren", new ArrayList<WareDTO>());
        }

        return kunde.data("waren", alleWaren.stream().map(Converter::wareToDTO).collect(Collectors.toList()));
    }

    @GET
    @Path("/kunde/{kundenId}/einstellung")
    @RolesAllowed("Kunde")
    public TemplateInstance kundenEinstellung(@PathParam("kundenId") long kundenId, @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }

        Kunde derKunde = vservice.kundeAbfragen(context.getUserPrincipal().getName());
        Adresse kundenAdr = vservice.kundenAdresseAbfragen(kundenId);

        AdresseDTO dtoKundenAdr;

        if (kunde == null) {
            return errorkundemitarbeiter.data("fehler", "Es ist ein interner Konflikt aufgetreten", "kunde", kundenId,
                    "mitarbeiter", -1);
        }

        if (kundenAdr == null) {
            dtoKundenAdr = new AdresseDTO("", "", "", "");
        } else {
            dtoKundenAdr = Converter.adresseToDTO(kundenAdr);
        }

        return adresse.data("kunde", Converter.kundeToDTO(derKunde), "adresse", dtoKundenAdr);
    }

    @GET
    @Path("/kunde/{kundenId}/einstellung/adresse/edit")
    @RolesAllowed("Kunde")
    public TemplateInstance adresseAendern(@PathParam("kundenId") long kundenId, @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }
        Adresse adr = vservice.kundenAdresseAbfragen(kundenId);
        if (adr == null) {
            return errorkundemitarbeiter.data("fehler", "Keine Adresse vorhanden", "kunde", kundenId, "mitarbeiter",
                    -1);
        }
        return adresseBearbeiten.data("adresse", Converter.adresseToDTO(adr), "kunde", kundenId);
    }

    @GET
    @Path("/kunde/{kundenId}/einstellung/adresse/add")
    @RolesAllowed("Kunde")
    public TemplateInstance adresseHinzu(@PathParam("kundenId") long kundenId, @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }
        return adresseHinzufuegen.data("kunde", kundenId);
    }

    @GET
    @Path("/kunde/{kundenId}/warenkorb")
    @RolesAllowed("Kunde")
    public TemplateInstance warenkorbAnzeigen(@PathParam("kundenId") long kundenId, @Context SecurityContext context) {
        if (!this.checkCredentialsKunde(kundenId, context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }
        Warenkorb kundenWarenkorb = wservice.warenkorbAnzeigen(kundenId);
        if (kundenWarenkorb != null) {
            if (vservice.kundenAdresseAbfragen(kundenId) == null) {
                return warenkorb.data("warenkorb", Converter.warenkorbToDTO(kundenWarenkorb), "fehler",
                        "Keine Adresse hinterlegt. Bitte in den Einstellungen" +
                                " hinzufügen");
            }
            return warenkorb.data("warenkorb", Converter.warenkorbToDTO(kundenWarenkorb), "fehler", "");
        }
        return errorkundemitarbeiter.data("fehler", "Kein warenkorb vorhanden", "kunde", kundenId, "mitarbeiter", -1);
    }

    @GET
    @Path("/admin")
    @RolesAllowed("Admin")
    public TemplateInstance adminAnsicht(@Context SecurityContext context) {
        if (!this.checkCredentialsAdmin(context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }

        Collection<Mitarbeiter> alleMitarbeiter = vservice.alleMitarbeiter();
        if (alleMitarbeiter == null) {
            return admin.data("mitarbeiter", new ArrayList<MitarbeiterDTO>());
        }

        return admin.data("mitarbeiter",
                alleMitarbeiter.stream().map(Converter::mitarbeiterToDTO).collect(Collectors.toList()));
    }

    @GET
    @Path("/admin/mitarbeiter")
    @RolesAllowed("Admin")
    public TemplateInstance mitarbeiterHinzufuegen(@Context SecurityContext context) {
        if (!this.checkCredentialsAdmin(context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }

        return mitarbeiterHinzufuegen.instance();
    }

    @GET
    @Path("/mitarbeiter")
    @RolesAllowed("Mitarbeiter")
    public TemplateInstance mitarbeiterStartAnsicht(@Context SecurityContext context) {
        if (!this.checkCredentialsMitarbeiter(context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }
        return mitarbeiter.instance();
    }

    @GET
    @Path("/mitarbeiter/{mitarbeiterId}/auffuellen")
    @RolesAllowed("Mitarbeiter")
    public TemplateInstance mitarbeiterWareAuffuellen(@PathParam("mitarbeiterId") long mitarbeiterId,
            @Context SecurityContext context) {
        if (!this.checkCredentialsMitarbeiter(context)) {
            return error.data("fehler", "Zugriff verweigert!");
        }
        return bestandAuffuellen.data("mitarbeiter", mitarbeiterId);
    }

    @GET
    @Path("/user")
    @PermitAll
    public TemplateInstance loginAnzeigen() {
        return login.instance();
    }

    @GET
    @Path("/user/regist")
    @PermitAll
    public TemplateInstance registAnzeigen() {
        return registrieren.instance();
    }

    private boolean checkCredentialsKunde(long kundenId, SecurityContext securityContext) {
        return wservice.kundeExistiertUndBerechtigt(kundenId, securityContext.getUserPrincipal().getName());
    }

    private boolean checkCredentialsMitarbeiter(SecurityContext securityContext) {
        return vservice.mitarbeiterExistiert(securityContext.getUserPrincipal().getName());
    }

    private boolean checkCredentialsAdmin(SecurityContext securityContext) {
        return vservice.adminExistiert(securityContext.getUserPrincipal().getName());
    }
}
