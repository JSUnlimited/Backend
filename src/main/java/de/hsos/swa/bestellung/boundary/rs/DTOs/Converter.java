package de.hsos.swa.bestellung.boundary.rs.DTOs;

import de.hsos.swa.bestellung.entity.Bestellposten;
import de.hsos.swa.bestellung.entity.Bestellung;

/* 
 * Implementierung
 *
 * @author Julian Schramm, Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class Converter {

    public static BestellungDTO bestellungToDTO(Bestellung bestellung) {
        return new BestellungDTO(bestellung.getId(), bestellung.getStatus(), bestellung.getLiefertermin(),
                bestellung.getBestellposten());
    }

    public static Bestellposten bestellpostenToClass(BestellpostenDTO dto) {
        return new Bestellposten(dto.postenId, dto.warenId, dto.menge);
    }

    public static BestellpostenDTO bestellpostenToDTO(Bestellposten posten) {
        return new BestellpostenDTO(posten.getId(), posten.getWarenId(), posten.getMenge());
    }
}
