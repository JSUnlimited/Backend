package de.hsos.swa.bestellung.acl;

/* 
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public interface NutzerFuerBestellung {

    KundeBestellungDTO sucheKundeNachId(long kundenId);

    MitarbeiterBestellungDTO sucheMitarbeiterNachId(long mitarbeiterId);
}
