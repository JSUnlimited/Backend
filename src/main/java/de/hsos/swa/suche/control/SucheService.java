package de.hsos.swa.suche.control;

import java.util.Collection;

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
public interface SucheService {

    Collection<Ware> warenkatalogAnfordern();

    Ware wareSuchenNachId(long warenId);

    Ware[] wareSuchenNachName(String name);
}
