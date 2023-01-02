package de.hsos.swa.verwaltung.boundary.rs.DTOs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/* 
 * Implementierung
 *
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class AdresseDTO {

    @NotBlank
    @Size(min=1, max=10)
    public String plz;

    @NotBlank
    @Size(min=1, max=25)
    public String ort;

    @NotBlank
    @Size(min=1, max=40)
    public String strasse;

    @NotBlank
    @Size(min=1, max=6)
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
