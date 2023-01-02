package de.hsos.swa.verwaltung.gateway;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.hsos.swa.bestellung.acl.KundeBestellungDTO;
import de.hsos.swa.bestellung.acl.MitarbeiterBestellungDTO;
import de.hsos.swa.bestellung.acl.NutzerFuerBestellung;
import de.hsos.swa.bestellung.acl.WareAnpassen;
import de.hsos.swa.bestellung.acl.WareBestellungDTO;
import de.hsos.swa.suche.acl.WareSucheDTO;
import de.hsos.swa.suche.acl.WarenFuerSuche;
import de.hsos.swa.verwaltung.gateway.DTOs.KundeDTO;
import de.hsos.swa.verwaltung.gateway.DTOs.MitarbeiterDTO;
import de.hsos.swa.verwaltung.gateway.DTOs.WareDTO;

@ApplicationScoped
public class ACLRepository implements WarenFuerSuche, NutzerFuerBestellung, WareAnpassen {

    @Inject
    EntityManager em;

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<WareSucheDTO> warenkatalogAnfordern() {
        Collection<WareDTO> dtos = em.createQuery("SELECT w FROM Ware w ORDER BY w_id").getResultList();
        if (dtos != null) {
            Collection<WareSucheDTO> result = dtos.stream().map(ACLConverter::wareDTOVerwaltungToWareDTOSuche)
                    .collect(Collectors.toList());
            return result;
        }
        return null;
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
    @Override
    public WareSucheDTO wareSuchenNachId(long warenId) {
        WareDTO dto = this.em.find(WareDTO.class, warenId);
        if (dto != null) {
            return ACLConverter.wareDTOVerwaltungToWareDTOSuche(dto);
        }
        return null;
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
    @Override
    @SuppressWarnings("unchecked")
    public Collection<WareSucheDTO> wareSuchenNachName(String suchbegriff) {
        Collection<WareDTO> dtos = em.createQuery("SELECT w FROM Ware w WHERE name LIKE '%" + suchbegriff + "%' ORDER BY w_id")
                .getResultList();
        if (dtos != null) {
            Collection<WareSucheDTO> result = dtos.stream().map(ACLConverter::wareDTOVerwaltungToWareDTOSuche)
                    .collect(Collectors.toList());
            return result;
        }
        return null;
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
    @Override
    public KundeBestellungDTO sucheKundeNachId(long kundenId) {
        KundeDTO dto = this.em.find(KundeDTO.class, kundenId);
        if (dto != null) {
            return ACLConverter.kundeDTOToKundeBestellungDTO(dto);
        }
        return null;
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
    @Override
    public MitarbeiterBestellungDTO sucheMitarbeiterNachId(long mitarbeiterId) {
        MitarbeiterDTO dto = this.em.find(MitarbeiterDTO.class, mitarbeiterId);
        if (dto != null) {
            return ACLConverter.mitarbeiterDTOToMitarbeiterBestellungDTO(dto);
        }
        return null;
    }

    @Override
    public boolean warenbestandAnpassen(WareBestellungDTO dto) {
        WareDTO wareDTO = this.em.find(WareDTO.class, dto.id);
        if(wareDTO != null) {
            if(wareDTO.menge >= dto.menge) {
                if(dto.menge < 0) {
                    // Holen, negativ
                    wareDTO.auffuellen(dto.menge);
                    em.merge(wareDTO);
                    return true;
                }
                if(dto.menge > 0) {
                    // Freigeben, positiv
                    wareDTO.auffuellen(dto.menge);
                    em.merge(wareDTO);
                    return true;
                }
            }
        }
        return false;
    }
}
