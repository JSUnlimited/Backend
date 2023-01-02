package de.hsos.swa.verwaltung.acl;

/* 
 * Implementierung
 *
 * @author Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public interface WarenkorbVerwaltungZuBestellung {

    boolean warenkorbBestellen(WarenkorbDTO dto);

    boolean alleBestellungenLoeschen(long kundenId);
}
