package de.hsos.swa;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

/* 
 * Implementierung
 *
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm
 */
@QuarkusTest
public class VerwaltungTest {

    @Test
    public void AlleVerwaltungTests() {
        testMitarbeiterRegistrieren();
        testKundeRegistrieren();
        testAdresseHinzufuegen();
        testAdresseAendern();
        testKundeLoeschen();
        testWareAuffuellen();
        testMitarbeiterLoeschen();
    }

    public void testMitarbeiterRegistrieren() {
        given().auth().basic("System@BMS.de", "Syst1234").contentType(ContentType.JSON).body(
                "{\"email\":\"mitarbeiter1@BMS.de\",\"passwort\":\"mitarbeiter1234\", \"vorname\":\"Der\", \"nachname\":\"Boss\"}")
                .when()
                .post("/baumarkt-shop/admin").then().statusCode(200);
    }

    public void testKundeRegistrieren() {
        given().contentType(ContentType.JSON).body(
                "{\"email\":\"julian.schramm@gmx.de\",\"passwort\":\"julian1234\", \"vorname\":\"Julian\", \"nachname\":\"Schramm\"}")
                .when()
                .post("/baumarkt-shop/user").then().statusCode(200);
    }

    public void testAdresseHinzufuegen() {
        given().auth().basic("julian.schramm@gmx.de", "julian1234").contentType(ContentType.JSON)
                .body("{\"plz\":\"00000\", \"ort\":\"Teststadt\", \"strasse\":\"Teststraße\", \"hausnr\":\"1\"}")
                .post("baumarkt-shop/kunde/2/adresse").then().statusCode(200);
    }

    public void testAdresseAendern() {
        given().auth().basic("julian.schramm@gmx.de", "julian1234").contentType(ContentType.JSON)
                .body("{\"plz\":\"49134\", \"ort\":\"Wallenhorst\", \"strasse\":\"Am Sportplatz\", \"hausnr\":\"12b\"}")
                .put("baumarkt-shop/kunde/2/adresse").then().statusCode(200);
    }

    public void testKundeLoeschen() {
        given().auth().basic("julian.schramm@gmx.de", "julian1234").contentType(ContentType.JSON)
                .delete("/baumarkt-shop/kunde/2").then().statusCode(200);
    }

    public void testWareAuffuellen() {
        given().auth().basic("mitarbeiter1@BMS.de", "mitarbeiter1234").contentType(ContentType.JSON)
                .put("baumarkt-shop/mitarbeiter/2/waren/1?menge=5")
                .then().statusCode(200);
    }

    public void testMitarbeiterLoeschen() {
        given().auth().basic("System@BMS.de", "Syst1234").contentType(ContentType.JSON).delete("/baumarkt-shop/admin/1")
                .then().statusCode(200);
    }

}