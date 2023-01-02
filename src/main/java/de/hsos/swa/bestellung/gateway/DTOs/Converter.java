package de.hsos.swa.bestellung.gateway.DTOs;

import de.hsos.swa.bestellung.entity.Bestellposten;
import de.hsos.swa.bestellung.entity.Bestellung;

/* 
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class Converter {
    public static BestellungDTO bestellungToDTO(Bestellung bestellung) {
        BestellungDTO dto = new BestellungDTO(bestellung.getKundenId(), bestellung.getStatus());
        for (int i = 0; i < bestellung.getBestellposten().size(); i++) {
            dto.posten.add(new BestellpostenDTO(bestellung.getBestellposten().get(i).getWarenId(),
                    bestellung.getBestellposten().get(i).getMenge()));
        }
        return dto;
    }

    public static BestellungDTO bestellungToExistingDTO(Bestellung bestellung) {
        BestellungDTO dto = new BestellungDTO(bestellung.getId(), bestellung.getKundenId(), bestellung.getStatus());
        for (int i = 0; i < bestellung.getBestellposten().size(); i++) {
            dto.posten.add(new BestellpostenDTO(bestellung.getBestellposten().get(i).getId(),
                    bestellung.getBestellposten().get(i).getWarenId(),
                    bestellung.getBestellposten().get(i).getMenge()));
        }
        return dto;
    }

    public static Bestellung bestellungDTOToClass(BestellungDTO dto) {
        Bestellung bestellung = new Bestellung(dto.bestellId, dto.kundenId, dto.status, dto.liefertermin);
        for (int i = 0; i < dto.posten.size(); i++) {
            bestellung.addBestellposten(
                    new Bestellposten(dto.posten.get(i).postenId, dto.posten.get(i).warenId, dto.posten.get(i).menge));
        }
        return bestellung;
    }
}
