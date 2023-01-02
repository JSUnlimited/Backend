package de.hsos.swa.verwaltung.boundary.rs.DTOs;

import de.hsos.swa.verwaltung.entity.Ware;
import de.hsos.swa.verwaltung.entity.benutzer.Administrator;
import de.hsos.swa.verwaltung.entity.benutzer.Adresse;
import de.hsos.swa.verwaltung.entity.benutzer.Kunde;
import de.hsos.swa.verwaltung.entity.benutzer.Mitarbeiter;
import de.hsos.swa.verwaltung.entity.warenkorb.Warenkorb;

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
    public static Mitarbeiter mitarbeiterDTOToClass(MitarbeiterDTO dto) {
        return new Mitarbeiter(dto.email, dto.passwort, dto.vorname, dto.nachname);
    }

    public static MitarbeiterDTO mitarbeiterToDTO(Mitarbeiter mitarbeiter) {
        return new MitarbeiterDTO(mitarbeiter.getId(), mitarbeiter.getEmail(), mitarbeiter.getVorname(), mitarbeiter.getNachname());
    }

    public static Kunde kundeDTOToClass(KundeDTO dto) {
        return new Kunde(dto.email, dto.passwort, dto.vorname, dto.nachname);
    }

    public static KundeDTO kundeToDTO(Kunde kunde) {
        return new KundeDTO(kunde.getId(), kunde.getEmail(), kunde.getVorname(), kunde.getNachname());
    }

    public static Adresse adresseDTOToClass(AdresseDTO adresseDTO) {
        return new Adresse(adresseDTO.plz, adresseDTO.ort, adresseDTO.strasse, adresseDTO.hausnr);
    }

    public static AdresseDTO adresseToDTO(Adresse adresse) {
        return new AdresseDTO(adresse.getPlz(), adresse.getOrt(), adresse.getStrasse(), adresse.getHausnr());
    }

    public static WarenkorbDTO warenkorbToDTO(Warenkorb warenkorb) {
        WarenkorbDTO dto = new WarenkorbDTO(warenkorb.getKundenId(), warenkorb.getComplete());

        for (int i = 0; i < warenkorb.getWarenkorbinhalt().size(); i++) {
            WareDTO ware = wareToDTO(warenkorb.getWarenkorbinhalt().get(i).getWare());
            dto.warenkorbelemente.add(new WarenkorbelementDTO(ware, warenkorb.getWarenkorbinhalt().get(i).getMenge()));
        }

        return dto;
    }

    public static WareDTO wareToDTO(Ware ware) {
        return new WareDTO(ware.getId(), ware.getName(), ware.getMenge(), ware.getPreis(), ware.getBeschreibung(), ware.getBild());
    }

    public static AdministratorDTO administratorToDTO(Administrator admin) {
        return new AdministratorDTO(admin.getId(), admin.getEmail(), admin.getVorname(), admin.getNachname());
    }
}
