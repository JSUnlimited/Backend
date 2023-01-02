package de.hsos.swa.verwaltung.acl;

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

    public double preis;

    public String beschreibung;

    public WareDTO() {
    }

    public WareDTO(long warenId, String name, double preis, String beschreibung) {
        this.warenId = warenId;
        this.name = name;
        this.preis = preis;
        this.beschreibung = beschreibung;
    }
}
