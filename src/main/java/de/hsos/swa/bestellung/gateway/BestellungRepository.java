package de.hsos.swa.bestellung.gateway;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.hsos.swa.bestellung.acl.KundeBestellungDTO;
import de.hsos.swa.bestellung.acl.MitarbeiterBestellungDTO;
import de.hsos.swa.bestellung.acl.NutzerFuerBestellung;
import de.hsos.swa.bestellung.acl.WareAnpassen;
import de.hsos.swa.bestellung.acl.WareBestellungDTO;
import de.hsos.swa.bestellung.control.BestellungService;
import de.hsos.swa.bestellung.entity.BestellStatus;
import de.hsos.swa.bestellung.entity.Bestellposten;
import de.hsos.swa.bestellung.entity.Bestellung;
import de.hsos.swa.bestellung.gateway.DTOs.BestellpostenDTO;
import de.hsos.swa.bestellung.gateway.DTOs.BestellungDTO;
import de.hsos.swa.bestellung.gateway.DTOs.Converter;
import de.hsos.swa.verwaltung.acl.WarenkorbDTO;
import de.hsos.swa.verwaltung.acl.WarenkorbVerwaltungZuBestellung;

@ApplicationScoped
public class BestellungRepository implements BestellungService, WarenkorbVerwaltungZuBestellung {

    @Inject
    EntityManager em;

    @Inject
    NutzerFuerBestellung nutzer;

    @Inject
    WareAnpassen aendern;

    @Inject
    MailSender mailSender;

    /*
     * Implementierung
     * 
     * @author Julian Schramm
     * 
     * Test/Kontrolle
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public Bestellung bestellungAnzeigen(long bestellungId) {
        BestellungDTO bestellungDTO = this.em.find(BestellungDTO.class, bestellungId);
        if (bestellungDTO != null) {
            return Converter.bestellungDTOToClass(bestellungDTO);
        }
        return null;
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
    public Collection<Bestellung> bestellungenAnzeigen(long kundenId) {
        List<BestellungDTO> alleBestellungenDTO = em
                .createQuery("SELECT b FROM Bestellung b WHERE kundenid=" + kundenId + " ORDER BY bestellId", BestellungDTO.class)
                .getResultList();
        if (alleBestellungenDTO != null) {
            List<Bestellung> alleBestellungen = new ArrayList<>();
            for (int i = 0; i < alleBestellungenDTO.size(); i++) {
                alleBestellungen.add(Converter.bestellungDTOToClass(alleBestellungenDTO.get(i)));
            }
            return alleBestellungen;
        }
        return null;
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
    public boolean lieferterminAendern(long kundenId, long bestellungId, LocalDate liefertermin) {
        BestellungDTO bestellungDTO = this.em.find(BestellungDTO.class, bestellungId);
        if (bestellungDTO != null) {
            if (bestellungDTO.kundenId == kundenId) {
                if (bestellungDTO.status != BestellStatus.VERSENDET) {
                    bestellungDTO.liefertermin = liefertermin;
                    bestellungDTO.status = BestellStatus.TERMIN_EINGETRAGEN;
                    this.em.merge(bestellungDTO);
                    return true;
                }
                return false;
            }
            return false;
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
    public boolean bestellungVersendet(long kundenId, long bestellungId) {
        BestellungDTO bestellungDTO = this.em.find(BestellungDTO.class, bestellungId);
        if (bestellungDTO != null) {
            if (bestellungDTO.kundenId == kundenId) {
                if (bestellungDTO.liefertermin != null) {
                    bestellungDTO.status = BestellStatus.VERSENDET;
                    KundeBestellungDTO kundeBestellungDTO = nutzer.sucheKundeNachId(kundenId);
                    this.em.merge(bestellungDTO);
                    mailSender.sendEMailVersendet(kundeBestellungDTO.email, bestellungId, bestellungDTO.liefertermin);
                    return true;
                }
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
    public boolean warenkorbBestellen(WarenkorbDTO dto) {
        if (dto != null) {
            Bestellung bestellung = new Bestellung(dto.kundenId);
            for (int i = 0; i < dto.warenkorbelemente.size(); i++) {
                bestellung.addBestellposten(new Bestellposten(dto.warenkorbelemente.get(i).ware.warenId,
                        dto.warenkorbelemente.get(i).menge));
            }
            this.em.persist(Converter.bestellungToDTO(bestellung));
            if (!dto.complete) {
                KundeBestellungDTO temp = nutzer.sucheKundeNachId(dto.kundenId);
                mailSender.sendEMailVorraetig(temp.email);
            }
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
    public Collection<Bestellung> bestellungenEinesKundenAnzeigen(long kundenId) {
        Collection<BestellungDTO> dtos = this.em
                .createQuery("SELECT b FROM Bestellung b WHERE kundenid=" + kundenId + " ORDER BY bestellId", BestellungDTO.class)
                .getResultList();
        if (dtos != null) {
            return dtos.stream().map(Converter::bestellungDTOToClass).collect(Collectors.toList());
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
    public Bestellung eineBestellungAnzeigen(long kundenId, long bestellId) {
        BestellungDTO dto = this.em.find(BestellungDTO.class, bestellId);
        if (dto != null) {
            if (dto.kundenId == kundenId) {
                return Converter.bestellungDTOToClass(dto);
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
    public boolean bestellungLoeschen(long kundenId, long bestellId) {
        BestellungDTO dto = this.em.find(BestellungDTO.class, bestellId);
        if (dto != null) {
            if (dto.status == BestellStatus.NEU || dto.status == BestellStatus.TERMIN_EINGETRAGEN) {
                if (dto.kundenId == kundenId) {
                    for(int i=0; i<dto.posten.size(); i++) {
                        aendern.warenbestandAnpassen(new WareBestellungDTO(dto.posten.get(i).warenId, dto.posten.get(i).menge));
                    }
                    this.em.remove(dto);
                    return true;
                }
                return false;
            }
            return false;
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
    public boolean bestellpostenBearbeiten(long kundenId, long bestellId, Bestellposten posten) {
        BestellungDTO dto = this.em.find(BestellungDTO.class, bestellId);
        if (dto != null) {
            if (dto.status == BestellStatus.VERSENDET) {
                return false;
            }
            if (dto.kundenId == kundenId) {
                Bestellung bestellung = Converter.bestellungDTOToClass(dto);
                boolean flag = false;

                for (int i = 0; i < bestellung.getBestellposten().size(); i++) {
                    if (bestellung.getBestellposten().get(i).getId() == posten.getId()) {
                        bestellung.editBestellposten(posten, i);
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    BestellpostenDTO toedit = this.em.find(BestellpostenDTO.class, posten.getId());
                    if (toedit != null) {
                        boolean warenaenderung = false;
                        
                        if(toedit.warenId != posten.getWarenId()) {
                            // WarenId geändert
                            warenaenderung = aendern.warenbestandAnpassen(new WareBestellungDTO(posten.getWarenId(), -(posten.getMenge())));
                            if(warenaenderung) {
                                aendern.warenbestandAnpassen(new WareBestellungDTO(toedit.warenId, toedit.menge));
                            }
                        } else {
                            // Menge geändert
                            long editmenge = toedit.menge - posten.getMenge();
                            warenaenderung = aendern.warenbestandAnpassen(new WareBestellungDTO(posten.getWarenId(), editmenge));
                        }

                        if(warenaenderung) {
                            toedit.warenId = posten.getWarenId();
                            toedit.menge = posten.getMenge();
                            this.em.merge(toedit);
                            return true;
                        }
                    }
                }
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
    public boolean bestellpostenLoeschen(long kundenId, long bestellId, long postenId) {
        BestellungDTO dto = this.em.find(BestellungDTO.class, bestellId);
        if (dto != null) {
            if (dto.status == BestellStatus.VERSENDET) {
                return false;
            }
            if (dto.kundenId == kundenId) {
                Bestellung bestellung = Converter.bestellungDTOToClass(dto);

                for (int i = 0; i < bestellung.getBestellposten().size(); i++) {
                    if (bestellung.getBestellposten().get(i).getId() == postenId) {
                        aendern.warenbestandAnpassen(new WareBestellungDTO(bestellung.getBestellposten().get(i).getWarenId(), bestellung.getBestellposten().get(i).getMenge()));
                        bestellung.deleteBestellposten(bestellung.getBestellposten().get(i));
                        this.em.merge(Converter.bestellungToExistingDTO(bestellung));
                        if(bestellung.getBestellposten().isEmpty()) {
                            this.bestellungLoeschen(kundenId, bestellId);
                        }
                        return true;
                    }
                }
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
    public boolean alleBestellungenLoeschen(long kundenId) {
        Collection<BestellungDTO> dtos = this.em
                .createQuery("SELECT b FROM Bestellung b WHERE kundenid=" + kundenId, BestellungDTO.class)
                .getResultList();

        Iterator<BestellungDTO> itr = dtos.iterator();
        while (itr.hasNext()) {
            this.em.remove(itr.next());
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
    public boolean mitarbeiterExistiertUndBerechtigt(long mitarbeiterId, String email) {
        MitarbeiterBestellungDTO dto = nutzer.sucheMitarbeiterNachId(mitarbeiterId);
        if (dto != null) {
            if (dto.email.equals(email)) {
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
    public boolean kundeExistiertUndBerechtigt(long kundenId, String email) {
        KundeBestellungDTO dto = nutzer.sucheKundeNachId(kundenId);
        if (dto != null) {
            if (dto.email.equals(email)) {
                return true;
            }
        }
        return false;
    }

}