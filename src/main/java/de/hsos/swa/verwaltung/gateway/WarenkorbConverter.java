package de.hsos.swa.verwaltung.gateway;

import de.hsos.swa.verwaltung.acl.WareDTO;
import de.hsos.swa.verwaltung.acl.WarenkorbDTO;
import de.hsos.swa.verwaltung.acl.WarenkorbelementDTO;
import de.hsos.swa.verwaltung.entity.Ware;
import de.hsos.swa.verwaltung.entity.warenkorb.Warenkorb;

/*
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class WarenkorbConverter {
    public static WarenkorbDTO warenkorbToDTO(Warenkorb warenkorb) {
        WarenkorbDTO dto = new WarenkorbDTO(warenkorb.getKundenId(), warenkorb.getComplete());

        for (int i = 0; i < warenkorb.getWarenkorbinhalt().size(); i++) {
            WareDTO ware = wareToDTO(warenkorb.getWarenkorbinhalt().get(i).getWare());
            dto.warenkorbelemente.add(new WarenkorbelementDTO(ware, warenkorb.getWarenkorbinhalt().get(i).getMenge()));
        }

        return dto;
    }

    private static WareDTO wareToDTO(Ware ware) {
        return new WareDTO(ware.getId(), ware.getName(), ware.getPreis(), ware.getBeschreibung());
    }
}
