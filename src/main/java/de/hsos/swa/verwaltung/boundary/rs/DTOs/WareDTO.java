package de.hsos.swa.verwaltung.boundary.rs.DTOs;

/* 
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class WareDTO {

    public long warenId;

    public String name;

    public long menge;

    public double preis;

    public String beschreibung;

    public String bild;

    public WareDTO() {
    }

    public WareDTO(long warenId, String name, long menge, double preis, String beschreibung, String bild) {
        this.warenId = warenId;
        this.name = name;
        this.menge = menge;
        this.preis = preis;
        this.beschreibung = beschreibung;
        this.bild = bild;
    }
}
