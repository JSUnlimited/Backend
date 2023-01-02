package de.hsos.swa.bestellung.boundary.rs.DTOs;

/* 
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class BestellpostenDTO {

    public long postenId;

    public long warenId;

    public long menge;

    public BestellpostenDTO() {
    }

    public BestellpostenDTO(long postenId, long warenId, long menge) {
        this.postenId = postenId;
        this.warenId = warenId;
        this.menge = menge;
    }
}
