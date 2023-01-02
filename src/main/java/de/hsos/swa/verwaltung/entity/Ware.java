package de.hsos.swa.verwaltung.entity;

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
public class Ware {

    private long warenId;

    private String name;

    private double preis;

    private String beschreibung;

    private long menge;

    private String bild;

    public Ware() {
    }

    public Ware(long warenId, String name, long menge, double preis, String beschreibung, String bild) {
        this.warenId = warenId;
        this.name = name;
        this.menge = menge;
        this.preis = preis;
        this.beschreibung = beschreibung;
        this.bild = bild;
    }

    public long getId() {
        return warenId;
    }

    public String getName() {
        return name;
    }

    public double getPreis() {
        return preis;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public long getMenge() {
        return menge;
    }

    public String getBild() {
        return bild;
    }
}