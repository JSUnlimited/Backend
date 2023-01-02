package de.hsos.swa.verwaltung.entity.benutzer;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;

/* 
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
@Vetoed
public class Kunde implements Serializable {

    private long kundenId;

    private String email;

    private String passwort;

    private String vorname;

    private String nachname;

    public Kunde() {
    }

    public Kunde(String email, String passwort, String vorname, String nachname) {
        this.email = email;
        this.passwort = passwort;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public Kunde(long kundenId, String email, String vorname, String nachname) {
        this.kundenId = kundenId;
        this.email = email;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public long getId() {
        return this.kundenId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPasswort() {
        return this.passwort;
    }

    public String getVorname() {
        return this.vorname;
    }

    public String getNachname() {
        return this.nachname;
    }
}
