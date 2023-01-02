package de.hsos.swa.suche.gateway;

import de.hsos.swa.suche.acl.WareSucheDTO;
import de.hsos.swa.suche.entity.Ware;

/* 
 * Implementierung
 *
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public class Converter {
    public static Ware wareSucheDTOToClass(WareSucheDTO waresucheDTO) {
        return new Ware(waresucheDTO.warenId, waresucheDTO.name, waresucheDTO.preis, waresucheDTO.beschreibung,
                waresucheDTO.menge, waresucheDTO.bild);
    }
}