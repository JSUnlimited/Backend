package de.hsos.swa.verwaltung.gateway.DTOs;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*
 * Implementierung
 *
 * @author Julian Schramm, Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
@Vetoed
@Entity(name = "Ware")
@Table(name = "WARE")
public class WareDTO {

    @Id
    @SequenceGenerator(name = "wareSeq", sequenceName = "ware_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "wareSeq")
    @Column(name = "W_ID")
    public long warenId;

    public String name;

    public double preis;

    public String beschreibung;

    public long menge;

    @Column(length=10485760)
    public String bild;

    public WareDTO() {

    }

    public WareDTO(long warenId, String name, double preis, String beschreibung, long menge, String bild) {
        this.warenId = warenId;
        this.name = name;
        this.preis = preis;
        this.beschreibung = beschreibung;
        this.menge = menge;
        this.bild = bild;
    }

    public void auffuellen(long menge) {
        this.menge += menge;
    }
}
