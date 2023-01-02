package de.hsos.swa.verwaltung.control;

import java.util.Collection;

import de.hsos.swa.verwaltung.entity.Ware;
import de.hsos.swa.verwaltung.entity.benutzer.Administrator;
import de.hsos.swa.verwaltung.entity.benutzer.Adresse;
import de.hsos.swa.verwaltung.entity.benutzer.Kunde;
import de.hsos.swa.verwaltung.entity.benutzer.Mitarbeiter;

/* 
 * Implementierung
 *
 * @author Julian Schramm, Mick Hurrelbrink
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
public interface VerwaltungService {

    boolean mitarbeiterRegistrieren(Mitarbeiter mitarbeiter);

    boolean mitarbeiterLoeschen(long mId);

    boolean kundeRegistrieren(Kunde kunde);

    boolean adresseHinzufuegen(long kundenId, Adresse adresse);

    boolean adresseAendern(long kundenId, Adresse adresse);

    boolean kundeLoeschen(long kundenId);

    boolean wareAuffuellen(long warenId, int menge);

    boolean adminExistiert(String email);

    boolean mitarbeiterExistiert(String email);

    boolean kundeExistiert(String email);

    Kunde kundeAbfragen(String email);

    Mitarbeiter mitarbeiterAbfragen(String email);

    Administrator adminAbfragen(String email);

    /* Fuer Qute */
    Collection<Ware> alleWarenAnzeigen();

    Adresse kundenAdresseAbfragen(long kundenId);

    Collection<Mitarbeiter> alleMitarbeiter();

    boolean bildZuWare(long warenId, String bild);

    boolean bildLoeschen(long warenId);

    Ware bildVonWare(long warenId);
}
