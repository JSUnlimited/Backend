package de.hsos.swa.verwaltung.control;

import java.util.List;

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
public interface WarenkorbService {

    Warenkorb warenkorbAnzeigen(long kundenId);

    boolean wareHinzufuegen(long kundenId, long warenId, long menge);

    List<Ware> warenkorbBestellen(long kundenId);

    boolean kundeExistiertUndBerechtigt(long kundenId, String email);
}
