package de.hsos.swa.verwaltung.entity.security;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import de.hsos.swa.verwaltung.gateway.DTOs.AdministratorDTO;
import io.quarkus.runtime.StartupEvent;

/* 
 * Implementierung
 *
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
@Singleton
public class CreateInitialSecEntities {

    @Inject
    protected EntityManager em;

    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        UserLogin.deleteAll();
        UserLogin.add("System@BMS.de", "Syst1234", "Admin");
        em.persist(new AdministratorDTO("System@BMS.de", "Waldemar", "Wald"));
    }
}