package de.hsos.swa.verwaltung.entity.warenkorb;

import de.hsos.swa.verwaltung.entity.Ware;

/* 
 * Implementierung
 *
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class Warenkorbelement {

    private Ware ware;

    private long menge;

    public Warenkorbelement(Ware ware, long menge) {
        this.ware = ware;
        this.menge = menge;
    }

    public Ware getWare() {
        return this.ware;
    }

    public long getMenge() {
        return this.menge;
    }

    public void addMenge(long menge) {
        this.menge += menge;
    }
}
