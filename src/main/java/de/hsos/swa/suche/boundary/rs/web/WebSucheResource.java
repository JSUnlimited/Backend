package de.hsos.swa.suche.boundary.rs.web;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.hsos.swa.suche.boundary.rs.DTOs.Converter;
import de.hsos.swa.suche.boundary.rs.DTOs.WareDTO;
import de.hsos.swa.suche.control.SucheService;
import de.hsos.swa.suche.entity.Ware;
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
public class WebSucheResource {
    @Inject
    SucheService service;

    @Inject
    Template index;

    @Inject
    Template suche;

    @Inject
    Template wareDetail;

    @Inject
    Template error;

    @GET
    @PermitAll
    public TemplateInstance load() {
        Collection<Ware> alleWaren = service.warenkatalogAnfordern();
        return index.data("waren", alleWaren.stream().map(Converter::wareToDTO).collect(Collectors.toList()));
    }

    @GET
    @Path("/suche")
    @PermitAll
    public TemplateInstance warenSuchen(@QueryParam("such") String zuSuchen) {
        ArrayList<WareDTO> dtos = new ArrayList<>();

        try {
            Long warenId = Long.parseLong(zuSuchen);
            Ware ware = service.wareSuchenNachId(warenId);
            if(ware != null) {
                dtos.add(Converter.wareToDTO(ware));
            }
        } 
        catch(NumberFormatException ex) {
            Ware[] gesuchteWaren = service.wareSuchenNachName(zuSuchen);
            if(gesuchteWaren != null) {
                for(int i=0; i<gesuchteWaren.length; i++) {
                    dtos.add(Converter.wareToDTO(gesuchteWaren[i]));
                }
            } else {
                error.data("fehler", "Keine Ware mit dem Suchbegriff gefunden");
            }
        }
        return suche.data("waren", dtos);
    }

    @GET
    @Path("/ware/{warenId}")
    @PermitAll
    public TemplateInstance eineWareAnzeigen(@PathParam("warenId") long warenId) {
        Ware ware = service.wareSuchenNachId(warenId);
        if(ware == null) {
            error.data("fehler", "Die Ware mit der Id existiert nicht");
        }
        return wareDetail.data("ware", Converter.wareToDTO(ware));
    }
}
