package de.hsos.swa.bestellung.acl;

public class WareBestellungDTO {
    public long id;
    public long menge;

    public WareBestellungDTO() {}
    public WareBestellungDTO(long id, long menge) {
        this.id = id;
        this.menge = menge;
    }
}
