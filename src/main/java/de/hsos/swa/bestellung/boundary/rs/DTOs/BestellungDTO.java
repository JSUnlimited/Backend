package de.hsos.swa.bestellung.boundary.rs.DTOs;

import java.time.LocalDate;
import java.util.List;

import de.hsos.swa.bestellung.entity.BestellStatus;
import de.hsos.swa.bestellung.entity.Bestellposten;

/* 
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class BestellungDTO {

    public long bestellungId;

    public BestellStatus state;

    public LocalDate liefertermin;

    public List<Bestellposten> bestellposten;

    public BestellungDTO() {
    }

    public BestellungDTO(long bestellungId, BestellStatus state, LocalDate liefertermin,
            List<Bestellposten> bestellposten) {
        this.bestellungId = bestellungId;
        this.state = state;
        this.liefertermin = liefertermin;
        this.bestellposten = bestellposten;
    }
}
