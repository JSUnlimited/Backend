package de.hsos.swa.suche.acl;

import java.util.Collection;

/* 
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public interface WarenFuerSuche {

    Collection<WareSucheDTO> warenkatalogAnfordern();

    WareSucheDTO wareSuchenNachId(long warenId);

    Collection<WareSucheDTO> wareSuchenNachName(String suchbegriff);
}
