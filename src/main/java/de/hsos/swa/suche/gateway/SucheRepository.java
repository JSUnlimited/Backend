package de.hsos.swa.suche.gateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.hsos.swa.suche.acl.WareSucheDTO;
import de.hsos.swa.suche.acl.WarenFuerSuche;
import de.hsos.swa.suche.control.SucheService;
import de.hsos.swa.suche.entity.Ware;

@ApplicationScoped
public class SucheRepository implements SucheService {

    @Inject
    WarenFuerSuche katalog;

    /*
     * Implementierung
     *
     * @author Julian Schramm
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public Collection<Ware> warenkatalogAnfordern() {
        Collection<WareSucheDTO> ausSuche = katalog.warenkatalogAnfordern();
        if (ausSuche != null) {
            return ausSuche.stream().map(Converter::wareSucheDTOToClass).collect(Collectors.toList());
        }
        return null;
    }

    /*
     * Implementierung
     *
     * @author Julian Schramm
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public Ware wareSuchenNachId(long warenId) {
        WareSucheDTO ausSuche = katalog.wareSuchenNachId(warenId);
        if (ausSuche != null) {
            return Converter.wareSucheDTOToClass(ausSuche);
        }
        return null;
    }

    /*
     * Implementierung
     *
     * @author Julian Schramm
     * 
     * Kontrolle/Korrektur
     * 
     * @author Julian Schramm, Mick Hurrelbrink
     */
    @Override
    public Ware[] wareSuchenNachName(String suchbegriff) {
        if (suchbegriff.length() == 0) {
            return null;
        }
        ArrayList<Ware> waren = new ArrayList<>();
        String[] schluesselwoerter = suchbegriff.split(" ");

        for (int i = 0; i < schluesselwoerter.length; i++) {
            Collection<WareSucheDTO> dtos = katalog.wareSuchenNachName(suchbegriff);
            Iterator<WareSucheDTO> it = dtos.iterator();
            if (waren.isEmpty()) {
                while (it.hasNext()) {
                    waren.add(Converter.wareSucheDTOToClass(it.next()));
                }
            } else {
                while (it.hasNext()) {
                    WareSucheDTO temp = it.next();
                    for (int j = 0; j < waren.size(); j++) {
                        if (waren.get(j).getId() == temp.warenId)
                            break;
                        if (waren.size() - 1 == j)
                            waren.add(Converter.wareSucheDTOToClass(temp));
                    }
                }
            }
        }
        return waren.toArray(new Ware[waren.size()]);
    }
}