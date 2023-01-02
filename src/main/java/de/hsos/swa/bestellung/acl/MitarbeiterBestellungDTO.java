package de.hsos.swa.bestellung.acl;

/* 
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class MitarbeiterBestellungDTO {

    public long mitarbeiterId;

    public String email;

    public MitarbeiterBestellungDTO() {
    }

    public MitarbeiterBestellungDTO(long mitarbeiterId, String email) {
        this.mitarbeiterId = mitarbeiterId;
        this.email = email;
    }
}
