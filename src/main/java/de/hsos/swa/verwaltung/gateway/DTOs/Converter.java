package de.hsos.swa.verwaltung.gateway.DTOs;

import de.hsos.swa.verwaltung.entity.Ware;
import de.hsos.swa.verwaltung.entity.benutzer.Administrator;
import de.hsos.swa.verwaltung.entity.benutzer.Adresse;
import de.hsos.swa.verwaltung.entity.benutzer.Kunde;
import de.hsos.swa.verwaltung.entity.benutzer.Mitarbeiter;

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
    public static MitarbeiterDTO mitarbeiterToDTO(Mitarbeiter mitarbeiter) {
        return new MitarbeiterDTO(mitarbeiter.getEmail(), mitarbeiter.getVorname(), mitarbeiter.getNachname());
    }

    public static Mitarbeiter mitarbeiterDTOToClass(MitarbeiterDTO dto) {
        return new Mitarbeiter(dto.mitarbeiterId, dto.email, dto.vorname, dto.nachname);
    }

    public static KundeDTO kundeToDTO(Kunde kunde) {
        return new KundeDTO(kunde.getEmail(), kunde.getVorname(), kunde.getNachname());
    }

    public static Kunde kundeDTOToClass(KundeDTO dto) {
        return new Kunde(dto.kundenId, dto.email, dto.vorname, dto.nachname);
    }

    public static Adresse adresseDTOToClass(AdresseDTO dto) {
        return new Adresse(dto.plz, dto.ort, dto.strasse, dto.hausnr);
    }

    public static AdresseDTO adresseToDTO(Adresse adresse) {
        return new AdresseDTO(adresse.getPlz(), adresse.getOrt(), adresse.getStrasse(), adresse.getHausnr());
    }

    public static Ware wareDTOToClass(WareDTO dto) {
        return new Ware(dto.warenId, dto.name, dto.menge, dto.preis, dto.beschreibung, dto.bild);
    }

    public static Administrator administratorDTOToClass(AdministratorDTO dto) {
        return new Administrator(dto.id, dto.email, dto.vorname, dto.nachname);
    }
}
