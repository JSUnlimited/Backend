package de.hsos.swa.verwaltung.entity.warenkorb;

import java.util.ArrayList;

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
public class Warenkorb {

    private long kundenId;

    private ArrayList<Warenkorbelement> warenkorbelemente = new ArrayList<>();

    private boolean complete;

    public Warenkorb(long kundenId) {
        this.kundenId = kundenId;
        this.complete = true;
    }

    public void addElement(Ware ware, long menge) {
        warenkorbelemente.add(new Warenkorbelement(ware, menge));
    }

    public long getKundenId() {
        return this.kundenId;
    }

    public ArrayList<Warenkorbelement> getWarenkorbinhalt() {
        return this.warenkorbelemente;
    }

    public boolean getComplete() {
        return this.complete;
    }

    public void notComplete() {
        this.complete = false;
    }
}
