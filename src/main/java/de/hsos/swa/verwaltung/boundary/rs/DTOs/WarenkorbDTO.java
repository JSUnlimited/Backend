package de.hsos.swa.verwaltung.boundary.rs.DTOs;

import java.util.ArrayList;

/* 
 * Implementierung
 *
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class WarenkorbDTO {

    public long kundenId;

    public ArrayList<WarenkorbelementDTO> warenkorbelemente = new ArrayList<>();

    public boolean complete;

    public WarenkorbDTO() {
    }

    public WarenkorbDTO(long kundenId, boolean complete) {
        this.kundenId = kundenId;
        this.complete = complete;
    }
}
