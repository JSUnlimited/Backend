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
public class WarenkorbImportElementDTO {

    public long warenId;

    public long menge;

    public WarenkorbImportElementDTO() {
    }

    public WarenkorbImportElementDTO(long warenId, long menge) {
        this.warenId = warenId;
        this.menge = menge;
    }
}
