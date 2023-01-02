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
public class Adresse implements Serializable {

    private String plz;

    private String ort;

    private String strasse;

    private String hausnr;

    public Adresse() {
    }

    public Adresse(String plz, String ort, String strasse, String hausnr) {
        this.plz = plz;
        this.ort = ort;
        this.strasse = strasse;
        this.hausnr = hausnr;
    }

    public String getPlz() {
        return plz;
    }

    public String getOrt() {
        return ort;
    }

    public String getStrasse() {
        return strasse;
    }

    public String getHausnr() {
        return hausnr;
    }
}
