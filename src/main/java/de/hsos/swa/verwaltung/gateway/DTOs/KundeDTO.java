package de.hsos.swa.verwaltung.gateway.DTOs;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
@Entity(name = "Kunde")
@Table(name = "KUNDE")
public class KundeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "kundenSeq", sequenceName = "kunden_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "kundenSeq")
    public long kundenId;

    public String email;

    public String vorname;

    public String nachname;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "plz", column = @Column(name = "adresse_plz")),
            @AttributeOverride(name = "ort", column = @Column(name = "adresse_ort")),
            @AttributeOverride(name = "strasse", column = @Column(name = "adresse_strasse")),
            @AttributeOverride(name = "hausnr", column = @Column(name = "adresse_hausnr"))
    })
    public AdresseDTO adresse = null;

    public KundeDTO() {
    }

    public KundeDTO(String email, String vorname, String nachname) {
        this.email = email;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public void setAdresse(AdresseDTO adresse) {
        this.adresse = adresse;
    }
}
