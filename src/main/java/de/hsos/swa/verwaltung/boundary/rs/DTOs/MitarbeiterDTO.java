package de.hsos.swa.verwaltung.boundary.rs.DTOs;

import javax.validation.constraints.Email;
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
public class MitarbeiterDTO {

    public long mitarbeiterId;

    @Email
    @NotBlank
    @Size(min = 6)
    @Size(max = 30)
    public String email;

    @NotBlank
    @Size(min = 8)
    @Size(max = 16)
    public String passwort;

    @NotBlank
    @Size(min = 2)
    @Size(max = 50)
    public String vorname;

    @NotBlank
    @Size(min = 1)
    @Size(max = 50)
    public String nachname;

    public MitarbeiterDTO() {
    }

    public MitarbeiterDTO(String email, String passwort, String vorname, String nachname) {
        this.email = email;
        this.passwort = passwort;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public MitarbeiterDTO(long mitarbeiterId, String email, String vorname, String nachname) {
        this.mitarbeiterId = mitarbeiterId;
        this.email = email;
        this.vorname = vorname;
        this.nachname = nachname;
    }
}
