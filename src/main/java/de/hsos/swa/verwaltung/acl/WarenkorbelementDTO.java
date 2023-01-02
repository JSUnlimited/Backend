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
public class WarenkorbelementDTO {

    public WareDTO ware;

    public long menge;

    public WarenkorbelementDTO() {
    }

    public WarenkorbelementDTO(WareDTO dto, long menge) {
        this.ware = dto;
        this.menge = menge;
    }
}
