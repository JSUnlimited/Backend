package de.hsos.swa.bestellung.gateway.DTOs;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Vetoed;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.hsos.swa.bestellung.entity.BestellStatus;

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
@Entity(name = "Bestellung")
@Table(name = "BESTELLUNG")
public class BestellungDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "bestellpostenSeq", sequenceName = "bestellposten_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "bestellpostenSeq")
    public long bestellId;

    public long kundenId;
    public BestellStatus status;
    public LocalDate liefertermin;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bestellung_id")
    public List<BestellpostenDTO> posten = new ArrayList<>();

    public BestellungDTO() {
    }

    public BestellungDTO(long kundenId, BestellStatus status) {
        this.kundenId = kundenId;
        this.status = status;
    }

    public BestellungDTO(long bestellId, long kundenId, BestellStatus status) {
        this.bestellId = bestellId;
        this.kundenId = kundenId;
        this.status = status;
    }
}
