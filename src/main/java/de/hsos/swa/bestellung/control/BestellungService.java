package de.hsos.swa.bestellung.control;

import java.time.LocalDate;
import java.util.Collection;

import de.hsos.swa.bestellung.entity.Bestellposten;
import de.hsos.swa.bestellung.entity.Bestellung;

/* 
 * Implementierung
 *
 * @author Julian Schramm, Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public interface BestellungService {

    Bestellung bestellungAnzeigen(long bestellungId);

    Collection<Bestellung> bestellungenAnzeigen(long kundenId);

    boolean lieferterminAendern(long kundenId, long bestellungId, LocalDate liefertermin);

    boolean bestellungVersendet(long kundenId, long bestellungId);

    Collection<Bestellung> bestellungenEinesKundenAnzeigen(long kundenId);

    Bestellung eineBestellungAnzeigen(long kundenId, long bestellungId);

    boolean bestellungLoeschen(long kundenId, long bestellungId);

    boolean bestellpostenBearbeiten(long kundenId, long bestellungId, Bestellposten posten);

    boolean bestellpostenLoeschen(long kundenId, long bestellungId, long postenId);

    boolean mitarbeiterExistiertUndBerechtigt(long mitarbeiterId, String email);

    boolean kundeExistiertUndBerechtigt(long kundenId, String email);
}
