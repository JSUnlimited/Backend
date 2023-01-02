package de.hsos.swa.verwaltung.gateway.DTOs;

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
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
@Vetoed
@Entity(name = "Administrator")
@Table(name = "ADMINISTRATOR")
public class AdministratorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "adminSeq", sequenceName = "admin_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "adminSeq")
    public long id;

    public String email;

    public String vorname;

    public String nachname;

    public AdministratorDTO() {
    }

    public AdministratorDTO(String email, String vorname, String nachname) {
        this.email = email;
        this.vorname = vorname;
        this.nachname = nachname;
    }
}
