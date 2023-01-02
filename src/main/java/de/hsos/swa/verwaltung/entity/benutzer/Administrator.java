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
public class Administrator implements Serializable {

    private long id;

    private String email;

    private String vorname;

    private String nachname;

    public Administrator() {
    }

    public Administrator(String email, String vorname, String nachname) {
        this.email = email;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public Administrator(long id, String email, String vorname, String nachname) {
        this.id = id;
        this.email = email;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getVorname() {
        return this.vorname;
    }

    public String getNachname() {
        return this.nachname;
    }
}
