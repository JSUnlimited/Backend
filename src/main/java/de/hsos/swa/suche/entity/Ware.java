package de.hsos.swa.suche.entity;

/* 
 * Implementierung
 *
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class Ware {

    private long warenId;

    private String name;

    private double preis;

    private String beschreibung;

    private long menge;

    private String bild;

    public Ware() {
    }

    public Ware(long warenId, String name, double preis, String beschreibung, long menge, String bild) {
        this.warenId = warenId;
        this.name = name;
        this.preis = preis;
        this.beschreibung = beschreibung;
        this.menge = menge;
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
