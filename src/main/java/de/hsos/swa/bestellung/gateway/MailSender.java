package de.hsos.swa.bestellung.gateway;

import java.time.LocalDate;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

/* 
 * Implementierung
 *
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm
 */
@ApplicationScoped
public class MailSender {

    @Inject
    Mailer syncMailer;

    public MailSender() {
    }

    public void sendEMailVersendet(String name, long bestellungId, LocalDate datum) {
        Mail mail = Mail.withText(name, "Ihre Bestellung bei BMS",
                "Ihre Bestellung mit der ID " + bestellungId
                        + " bei BMS wurde versendet. Voraussichtlicher Liefertermin ist der " + datum + ".");
        syncMailer.send(mail);
    }

    public void sendEMailVorraetig(String name) {
        Mail mail = Mail.withText(name, "Ihre Bestellung bei BMS",
                "Alle Waren fuer Ihre Bestellung sind wieder vorraetig. Der Warenkorb wurde bestellt.");
        syncMailer.send(mail);
    }
}
