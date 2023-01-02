package de.hsos.swa.suche.boundary.rs.DTOs;

/* 
 * Implementierung
 *
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class WareDTO {

    public long warenId;

    public String name;

    public double preis;

    public String beschreibung;

    public long menge;

    public String bild;

    public WareDTO() {
    }

    public WareDTO(long warenId, String name, double preis, String beschreibung, long menge, String bild) {
        this.warenId = warenId;
        this.name = name;
        this.preis = preis;
        this.beschreibung = beschreibung;
        this.menge = menge;
        this.bild = bild;
    }
}
