package de.hsos.swa.verwaltung.gateway;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.hsos.swa.verwaltung.acl.WarenkorbVerwaltungZuBestellung;
import de.hsos.swa.verwaltung.control.VerwaltungService;
import de.hsos.swa.verwaltung.entity.Ware;
import de.hsos.swa.verwaltung.entity.benutzer.Administrator;
import de.hsos.swa.verwaltung.entity.benutzer.Adresse;
import de.hsos.swa.verwaltung.entity.benutzer.Kunde;
import de.hsos.swa.verwaltung.entity.benutzer.Mitarbeiter;
import de.hsos.swa.verwaltung.gateway.DTOs.AdministratorDTO;
import de.hsos.swa.verwaltung.gateway.DTOs.Converter;
import de.hsos.swa.verwaltung.gateway.DTOs.KundeDTO;
import de.hsos.swa.verwaltung.gateway.DTOs.MitarbeiterDTO;
import de.hsos.swa.verwaltung.gateway.DTOs.WareDTO;
import de.hsos.swa.verwaltung.entity.security.UserLogin;

@ApplicationScoped
public class VerwaltungRepository implements VerwaltungService {

    @Inject
    EntityManager em;

    @Inject
    WarenkorbVerwaltungZuBestellung dienst;

    @Inject
    WarenkorbRepository warenkorb;

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public boolean mitarbeiterRegistrieren(Mitarbeiter mitarbeiter) {
        if (mitarbeiter != null) {
            if (UserLogin.userNotPresent(mitarbeiter.getEmail())) {
                em.persist(Converter.mitarbeiterToDTO(mitarbeiter));
                UserLogin.add(mitarbeiter.getEmail(), mitarbeiter.getPasswort(), "Mitarbeiter");
                return true;
            }
        }
        return false;
    }

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public boolean mitarbeiterLoeschen(long mId) {
        MitarbeiterDTO dto = em.find(MitarbeiterDTO.class, mId);
        if (dto != null) {

            if (!UserLogin.userNotPresent(dto.email)) {
                em.remove(dto);
                UserLogin.deleteUser(dto.email);
                return true;
            }
        }
        return false;
    }

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public boolean kundeRegistrieren(Kunde kunde) {
        if (kunde != null) {
            if (UserLogin.userNotPresent(kunde.getEmail())) {
                em.persist(Converter.kundeToDTO(kunde));
                UserLogin.add(kunde.getEmail(), kunde.getPasswort(), "Kunde");
                return true;
            }
        }
        return false;
    }

    /*
     * Implementierung
     *
     * @author Julian Schramm
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public boolean wareAuffuellen(long warenId, int menge) {
        WareDTO dto = em.find(WareDTO.class, warenId);
        if (dto != null) {
            dto.auffuellen(menge);
            em.merge(dto);
            warenkorb.warenkorbkontrolleNachAuffuellen();
            return true;
        }
        return false;
    }

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public boolean adminExistiert(String email) {
        if (this.em.createQuery("SELECT a FROM Administrator a WHERE email='" + email + "'", AdministratorDTO.class)
                .getResultList().size() != 1) {
            return false;
        }
        return true;
    }

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public boolean mitarbeiterExistiert(String email) {
        if (this.em.createQuery("SELECT a FROM Mitarbeiter a WHERE email='" + email + "'", MitarbeiterDTO.class)
                .getResultList().size() != 1) {
            return false;
        }
        return true;
    }

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public boolean kundeExistiert(String email) {
        if (this.em.createQuery("SELECT k FROM Kunde k WHERE email='" + email + "'", KundeDTO.class)
                .getResultList().size() != 1) {
            return false;
        }
        return true;
    }

    /*
     * Implementierung
     *
     * @author Julian Schramm
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public boolean adresseHinzufuegen(long kundenId, Adresse adresse) {
        KundeDTO kundeDTO = em.find(KundeDTO.class, kundenId);
        if (kundeDTO != null) {
            if (kundeDTO.adresse == null) {
                kundeDTO.setAdresse(Converter.adresseToDTO(adresse));
                this.em.merge(kundeDTO);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /*
     * Implementierung
     *
     * @author Julian Schramm
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public boolean adresseAendern(long kundenId, Adresse adresse) {
        KundeDTO kundeDTO = em.find(KundeDTO.class, kundenId);
        if (kundeDTO != null) {
            if (kundeDTO.adresse != null) {
                kundeDTO.setAdresse(Converter.adresseToDTO(adresse));
                this.em.merge(kundeDTO);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /*
     * Implementierung
     *
     * @author Julian Schramm
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public boolean kundeLoeschen(long kundenId) {
        KundeDTO kundeDTO = em.find(KundeDTO.class, kundenId);
        if (kundeDTO != null) {
            if (dienst.alleBestellungenLoeschen(kundenId)) {
                this.em.remove(kundeDTO);
                UserLogin.deleteUser(kundeDTO.email);
                return true;
            }
        }
        return false;
    }

    /*
     * Implementierung
     *
     * @author Julian Schramm, Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public Kunde kundeAbfragen(String email) {
        List<KundeDTO> dto = this.em.createQuery("SELECT k FROM Kunde k WHERE email='" + email + "'", KundeDTO.class)
                .getResultList();
        if (!dto.isEmpty()) {
            return Converter.kundeDTOToClass(dto.get(0));
        }
        return null;
    }

    /*
     * Implementierung
     *
     * @author Julian Schramm, Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public Mitarbeiter mitarbeiterAbfragen(String email) {
        List<MitarbeiterDTO> dto = this.em
                .createQuery("SELECT m FROM Mitarbeiter m WHERE email='" + email + "'", MitarbeiterDTO.class)
                .getResultList();
        if (!dto.isEmpty()) {
            return Converter.mitarbeiterDTOToClass(dto.get(0));
        }
        return null;
    }

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public Administrator adminAbfragen(String email) {
        List<AdministratorDTO> dto = this.em
                .createQuery("SELECT a FROM Administrator a WHERE email='" + email + "'", AdministratorDTO.class)
                .getResultList();
        if (!dto.isEmpty()) {
            return Converter.administratorDTOToClass(dto.get(0));
        }
        return null;
    }

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Ware> alleWarenAnzeigen() {
        Collection<WareDTO> dtos = this.em.createQuery("SELECT w FROM Ware w ORDER BY w_id").getResultList();
        if (dtos != null) {
            Collection<Ware> result = dtos.stream().map(Converter::wareDTOToClass)
                    .collect(Collectors.toList());
            return result;
        }
        return null;
    }

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public Adresse kundenAdresseAbfragen(long kundenId) {
        KundeDTO kunde = this.em.find(KundeDTO.class, kundenId);
        if (kunde != null) {
            if (kunde.adresse != null) {
                return Converter.adresseDTOToClass(kunde.adresse);
            }
        }
        return null;
    }

    /*
     * Implementierung
     *
     * @author Mick Hurrelbrink
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Mitarbeiter> alleMitarbeiter() {
        Collection<MitarbeiterDTO> dtos = this.em.createQuery("SELECT m FROM Mitarbeiter m").getResultList();
        if (dtos != null) {
            return dtos.stream().map(Converter::mitarbeiterDTOToClass).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Ware bildVonWare(long warenId){
        WareDTO wareDTO = em.find(WareDTO.class, warenId);
        if (wareDTO != null) {
            return Converter.wareDTOToClass(wareDTO);
        }else{
            return null;
        }
    }

    @Override
    public boolean bildZuWare(long warenId, String bild) {
        WareDTO wareDTO = em.find(WareDTO.class, warenId);
        if (wareDTO != null) {
            if (wareDTO.bild == null) {
                wareDTO.bild = bild;
                this.em.merge(wareDTO);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean bildLoeschen(long warenId){
        WareDTO wareDTO = em.find(WareDTO.class, warenId);
        if (wareDTO != null) {
            if (wareDTO.bild == null) {
                return false;

            } else {
                wareDTO.bild = null;
                this.em.merge(wareDTO);
                return true;
            }
        }
        return false;
    }
}