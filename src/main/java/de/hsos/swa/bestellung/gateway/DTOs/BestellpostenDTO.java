package de.hsos.swa.bestellung.gateway.DTOs;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/* 
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
@Vetoed
@Entity(name = "Bestellposten")
@Table(name = "BESTELLPOSTEN")
public class BestellpostenDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "bestellungspostenSeq", sequenceName = "bestellungsposten_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "bestellungspostenSeq")
    public long postenId;

    public long warenId;

    public long menge;

    public BestellpostenDTO() {
    }

    public BestellpostenDTO(long warenId, long menge) {
        this.warenId = warenId;
        this.menge = menge;
    }

    public BestellpostenDTO(long postenId, long warenId, long menge) {
        this.postenId = postenId;
        this.warenId = warenId;
        this.menge = menge;
    }
}
