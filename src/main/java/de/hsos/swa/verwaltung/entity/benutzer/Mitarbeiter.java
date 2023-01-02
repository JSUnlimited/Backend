package de.hsos.swa.verwaltung.entity.benutzer;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;

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
public class Mitarbeiter implements Serializable {

    private long mitarbeiterId;

    private String email;

    private String passwort;

    private String vorname;

    private String nachname;

    public Mitarbeiter() {
    }

    public Mitarbeiter(String email, String passwort, String vorname, String nachname) {
        this.email = email;
        this.passwort = passwort;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public Mitarbeiter(long mitarbeiterId, String email, String vorname, String nachname) {
        this.mitarbeiterId = mitarbeiterId;
        this.email = email;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public long getId() {
        return this.mitarbeiterId;
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