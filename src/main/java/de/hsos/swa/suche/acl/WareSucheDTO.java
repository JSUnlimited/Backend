package de.hsos.swa.suche.acl;

/* 
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class WareSucheDTO {

    public long warenId;

    public String name;

    public double preis;

    public String beschreibung;

    public long menge;

    public String bild;

    public WareSucheDTO() {

    }

    public WareSucheDTO(long warenId, String name, double preis, String beschreibung, long menge, String bild) {
        this.warenId = warenId;
        this.name = name;
        this.preis = preis;
        this.beschreibung = beschreibung;
        this.menge = menge;
        this.bild = bild;
    }
}
