package de.hsos.swa.suche.boundary.rs.DTOs;

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

    public static WareDTO wareToDTO(Ware ware) {
        return new WareDTO(ware.getId(), ware.getName(), ware.getPreis(), ware.getBeschreibung(), ware.getMenge(), ware.getBild());
    }
}
