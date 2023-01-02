package de.hsos.swa.verwaltung.gateway;

import de.hsos.swa.bestellung.acl.KundeBestellungDTO;
import de.hsos.swa.bestellung.acl.MitarbeiterBestellungDTO;
import de.hsos.swa.suche.acl.WareSucheDTO;
import de.hsos.swa.verwaltung.gateway.DTOs.KundeDTO;
import de.hsos.swa.verwaltung.gateway.DTOs.MitarbeiterDTO;
import de.hsos.swa.verwaltung.gateway.DTOs.WareDTO;

/*
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class ACLConverter {
    public static WareSucheDTO wareDTOVerwaltungToWareDTOSuche(WareDTO verwaltungdto) {
        return new WareSucheDTO(verwaltungdto.warenId, verwaltungdto.name, verwaltungdto.preis,
                verwaltungdto.beschreibung, verwaltungdto.menge,verwaltungdto.bild);
    }

    public static KundeBestellungDTO kundeDTOToKundeBestellungDTO(KundeDTO verwaltungdto) {
        if (verwaltungdto.adresse != null) {
            return new KundeBestellungDTO(verwaltungdto.kundenId, verwaltungdto.email, true);
        } else {
            return new KundeBestellungDTO(verwaltungdto.kundenId, verwaltungdto.email, false);
        }
    }

    public static MitarbeiterBestellungDTO mitarbeiterDTOToMitarbeiterBestellungDTO(MitarbeiterDTO verwaltungdto) {
        return new MitarbeiterBestellungDTO(verwaltungdto.mitarbeiterId, verwaltungdto.email);
    }
}
