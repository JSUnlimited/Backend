package de.hsos.swa.bestellung.entity;

import java.io.Serializable;

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
public class Bestellposten implements Serializable {

    private long postenId;

    private long warenId;

    private long menge;

    public Bestellposten() {
    }

    public Bestellposten(long warenId, long menge) {
        this.warenId = warenId;
        this.menge = menge;
    }

    public Bestellposten(long postenId, long warenId, long menge) {
        this.postenId = postenId;
        this.warenId = warenId;
        this.menge = menge;
    }

    public long getId() {
        return this.postenId;
    }

    public long getWarenId() {
        return this.warenId;
    }

    public long getMenge() {
        return this.menge;
    }
}
