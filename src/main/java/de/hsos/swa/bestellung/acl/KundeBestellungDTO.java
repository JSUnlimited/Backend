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
public class KundeBestellungDTO {

    public long kundenId;

    public String email;

    public boolean adresseVorhanden;

    public KundeBestellungDTO() {
    }

    public KundeBestellungDTO(long kundenId, String email, boolean adresseVorhanden) {
        this.kundenId = kundenId;
        this.email = email;
        this.adresseVorhanden = adresseVorhanden;
    }
}
