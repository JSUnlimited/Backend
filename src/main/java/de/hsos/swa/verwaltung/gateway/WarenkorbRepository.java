package de.hsos.swa.verwaltung.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.hsos.swa.verwaltung.gateway.DTOs.WareDTO;
import de.hsos.swa.verwaltung.acl.WarenkorbDTO;
import de.hsos.swa.verwaltung.acl.WarenkorbVerwaltungZuBestellung;
import de.hsos.swa.verwaltung.control.WarenkorbService;
import de.hsos.swa.verwaltung.entity.Ware;
import de.hsos.swa.verwaltung.entity.warenkorb.Warenkorb;
import de.hsos.swa.verwaltung.gateway.DTOs.Converter;
import de.hsos.swa.verwaltung.gateway.DTOs.KundeDTO;

/*
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
@ApplicationScoped
public class WarenkorbRepository implements WarenkorbService {
    private final ConcurrentMap<Long, Warenkorb> warenkoerbe = new ConcurrentHashMap<>();

    @Inject
    EntityManager em;

    @Inject
    WarenkorbVerwaltungZuBestellung warenkorbZuBestellung;

    @Override
    public boolean wareHinzufuegen(long kundenId, long warenId, long menge) {
        if (this.em.find(KundeDTO.class, kundenId) != null) {
            WareDTO dto = this.em.find(WareDTO.class, warenId);
            if (dto != null) {
                if (!warenkoerbe.containsKey(kundenId)) {
                    warenkoerbe.put(kundenId, new Warenkorb(kundenId));
                }
                for (int i = 0; i < warenkoerbe.get(kundenId).getWarenkorbinhalt().size(); i++) {
                    if (warenkoerbe.get(kundenId).getWarenkorbinhalt().get(i).getWare().getId() == warenId) {
                        warenkoerbe.get(kundenId).getWarenkorbinhalt().get(i).addMenge(menge);
                        return true;
                    }
                }
                warenkoerbe.get(kundenId).addElement(Converter.wareDTOToClass(dto), menge);
                return true;
            }
        }
        return false;
    }

    @Override
    public Warenkorb warenkorbAnzeigen(long kundenId) {
        if (warenkoerbe.containsKey(kundenId)) {
            return this.warenkoerbe.get(kundenId);
        }
        return null;
    }

    @Override
    public List<Ware> warenkorbBestellen(long kundenId) {
        KundeDTO kundeDTO = this.em.find(KundeDTO.class, kundenId);
        if (kundeDTO != null) {
            if (kundeDTO.adresse == null) {
                return null;
            }
            List<Ware> fehlen = new ArrayList<Ware>();
            if (warenkoerbe.containsKey(kundenId)) {
                WarenkorbDTO dto = WarenkorbConverter.warenkorbToDTO(warenkoerbe.get(kundenId));
                if (dto != null) {
                    for (int i = 0; i < dto.warenkorbelemente.size(); i++) {
                        WareDTO temp = this.em.find(WareDTO.class, dto.warenkorbelemente.get(i).ware.warenId);
                        if (temp != null) {
                            if (temp.menge - dto.warenkorbelemente.get(i).menge < 0) {
                                fehlen.add(
                                        new Ware(temp.warenId, temp.name, temp.menge, temp.preis, temp.beschreibung, temp.bild));
                                if (warenkoerbe.get(kundenId).getComplete()) {
                                    warenkoerbe.get(kundenId).notComplete();
                                }
                            }
                        } else {
                            fehlen.add(new Ware(-1, dto.warenkorbelemente.get(i).ware.name, 0, 0.0, "", ""));
                            if (warenkoerbe.get(kundenId).getComplete()) {
                                warenkoerbe.get(kundenId).notComplete();
                            }
                        }
                    }

                    if (fehlen.isEmpty()) {
                        for (int j = 0; j < dto.warenkorbelemente.size(); j++) {
                            WareDTO temp = this.em.find(WareDTO.class, dto.warenkorbelemente.get(j).ware.warenId);
                            temp.menge = temp.menge - dto.warenkorbelemente.get(j).menge;
                            this.em.merge(temp);
                        }
                        warenkoerbe.remove(kundenId);
                        warenkorbZuBestellung.warenkorbBestellen(dto);
                    }
                    return fehlen;
                }
            }
        }
        return null;
    }

    @Override
    public boolean kundeExistiertUndBerechtigt(long kundenId, String email) {
        KundeDTO dto = this.em.find(KundeDTO.class, kundenId);
        if (dto != null) {
            if (dto.email.equals(email)) {
                return true;
            }
        }
        return false;
    }

    public void warenkorbkontrolleNachAuffuellen() {
        for (var entry : warenkoerbe.entrySet()) {
            this.warenkorbBestellen(entry.getKey());
        }
    }

}
