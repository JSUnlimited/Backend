package de.hsos.swa.bestellung.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Vetoed;

/* 
 * Implementierung
 *
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
@Vetoed
public class Bestellung implements Serializable {

    private long bestellId;

    private long kundenId;

    private BestellStatus status;

    private LocalDate liefertermin;

    private List<Bestellposten> posten = new ArrayList<>();

    public Bestellung(long kundenId) {
        this.kundenId = kundenId;
        this.status = BestellStatus.NEU;
    }

    public Bestellung(long bestellId, long kundenId, BestellStatus status,
        LocalDate liefertermin) {
        this.bestellId = bestellId;
        this.kundenId = kundenId;
        this.status = status;
        this.liefertermin = liefertermin;
    }

    public long getId() {
        return this.bestellId;
    }

    public long getKundenId() {
        return this.kundenId;
    }

    public BestellStatus getStatus() {
        return this.status;
    }

    public void setStatus(BestellStatus newState) {
        this.status = newState;
    }

    public LocalDate getLiefertermin() {
        return this.liefertermin;
    }

    public void setLiefertermin(LocalDate newLiefertermin) {
        this.liefertermin = newLiefertermin;
    }

    public void addBestellposten(Bestellposten posten) {
        this.posten.add(posten);
    }

    public void editBestellposten(Bestellposten posten, int index) {
        this.posten.set(index, posten);
    }

    public void deleteBestellposten(Bestellposten posten) {
        this.posten.remove(posten);
    }

    public List<Bestellposten> getBestellposten() {
        return this.posten;
    }

}
