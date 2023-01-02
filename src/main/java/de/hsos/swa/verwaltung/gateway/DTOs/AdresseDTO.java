package de.hsos.swa.verwaltung.gateway.DTOs;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Embeddable;

/* 
 * Implementierung
 *
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
@Vetoed
@Embeddable
public class AdresseDTO {

    public String plz;

    public String ort;

    public String strasse;

    public String hausnr;

    public AdresseDTO() {
    }

    public AdresseDTO(String plz, String ort, String strasse, String hausnr) {
        this.plz = plz;
        this.ort = ort;
        this.strasse = strasse;
        this.hausnr = hausnr;
    }
}
