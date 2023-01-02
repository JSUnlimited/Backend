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
public class BestellungTest {

        @Test
        public void AlleBestellungTests() {

                testBestellungAnzeigen();
                testBestellungenAnzeigen();
                testLieferterminAendern();
                testBestellungVersendet();
                testAlleBestellungenEinesKunden();
                testEineBestellungEinesKunden();
                testBestellpostenBearbeiten();
                testBestellpostenEntfernen();
                testBestellungLoeschen();

        }

        public void testBestellungAnzeigen() {
                // Mitarbeiter registrieren
                given().auth().basic("System@BMS.de", "Syst1234").contentType(ContentType.JSON).body(
                                "{\"email\":\"mitarbeiter2@BMS.de\",\"passwort\":\"mitarbeiter1234\", \"vorname\":\"Der\", \"nachname\":\"Boss\"}")
                                .when()
                                .post("/baumarkt-shop/admin").then().statusCode(200);

                // Kunde registrieren
                given().contentType(ContentType.JSON).body(
                                "{\"email\":\"mick.hurrelbrink@gmx.de\",\"passwort\":\"mick1234\", \"vorname\":\"Mick\", \"nachname\":\"Hurrelbrink\"}")
                                .when()
                                .post("/baumarkt-shop/user").then().statusCode(200);

                // Kundenadresse hinzufuegen
                given().auth().basic("mick.hurrelbrink@gmx.de", "mick1234").contentType(ContentType.JSON)
                                .body("{\"plz\":\"00000\", \"ort\":\"Teststadt\", \"strasse\":\"Teststraße\", \"hausnr\":\"1\"}")
                                .post("baumarkt-shop/kunde/1/adresse").then().statusCode(200);

                // Ware dem Warenkorb hinzufuegen
                given().auth().basic("mick.hurrelbrink@gmx.de", "mick1234").contentType(ContentType.JSON)
                                .body("{\"warenId\":\"5\",\"menge\":\"4\"}").when()
                                .put("/baumarkt-shop/kunde/1/warenkorb").then().statusCode(200);

                // Warenkorb bestellen
                given().auth().basic("mick.hurrelbrink@gmx.de", "mick1234").contentType(ContentType.JSON)
                                .post("/baumarkt-shop/kunde/1/warenkorb").then().statusCode(200);

                // Eine Bestellung anzeigen
                given().auth().basic("mitarbeiter2@BMS.de", "mitarbeiter1234").contentType(ContentType.JSON).when()
                                .get("/baumarkt-shop/mitarbeiter/1/kunde/1/bestellung/1").then().statusCode(200);
        }

        public void testBestellungenAnzeigen() {

                given().auth().basic("mitarbeiter2@BMS.de", "mitarbeiter1234").contentType(ContentType.JSON).when()
                                .get("/baumarkt-shop/mitarbeiter/1/kunde/1/bestellung").then().statusCode(200);
        }

        public void testLieferterminAendern() {

                given().auth().basic("mitarbeiter2@BMS.de", "mitarbeiter1234").contentType(ContentType.JSON).when()
                                .put("/baumarkt-shop/mitarbeiter/1/kunde/1/bestellung/1/liefertermin?liefertermin=31-08-2022")
                                .then()
                                .statusCode(200);
        }

        public void testBestellungVersendet() {

                given().auth().basic("mitarbeiter2@BMS.de", "mitarbeiter1234").contentType(ContentType.JSON).when()
                                .put("/baumarkt-shop/mitarbeiter/1/kunde/1/bestellung/1/versendet")
                                .then()
                                .statusCode(200);
        }

        public void testAlleBestellungenEinesKunden() {

                given().auth().basic("mick.hurrelbrink@gmx.de",
                                "mick1234").contentType(ContentType.JSON).when()
                                .get("/baumarkt-shop/kunde/1/bestellung").then().statusCode(200);
        }

        public void testEineBestellungEinesKunden() {

                given().auth().basic("mick.hurrelbrink@gmx.de",
                                "mick1234").contentType(ContentType.JSON).when()
                                .get("/baumarkt-shop/kunde/1/bestellung/1").then().statusCode(200);
        }

        public void testBestellpostenBearbeiten() {

                // Ware zum Warenkorb hinzufuegen
                given().auth().basic("mick.hurrelbrink@gmx.de", "mick1234").contentType(ContentType.JSON)
                                .body("{\"warenId\":\"1\",\"menge\":\"1\"}").when()
                                .put("/baumarkt-shop/kunde/1/warenkorb").then().statusCode(200);

                // Warenkorb bestellen
                given().auth().basic("mick.hurrelbrink@gmx.de", "mick1234").contentType(ContentType.JSON)
                                .post("/baumarkt-shop/kunde/1/warenkorb").then().statusCode(200);

                // Bestellposten aendern
                given().auth().basic("mick.hurrelbrink@gmx.de", "mick1234").contentType(ContentType.JSON)
                                .body("{\"postenId\":\"2\",\"warenId\":\"2\",\"menge\":\"2\"}").when()
                                .put("/baumarkt-shop/kunde/1/bestellung/2/posten").then().statusCode(200);
        }

        public void testBestellpostenEntfernen() {

                given().auth().basic("mick.hurrelbrink@gmx.de", "mick1234").contentType(ContentType.JSON)
                                .delete("/baumarkt-shop/kunde/1/bestellung/2/posten/2").then().statusCode(200);
        }

        public void testBestellungLoeschen() {

                given().auth().basic("mick.hurrelbrink@gmx.de", "mick1234").contentType(ContentType.JSON)
                                .delete("/baumarkt-shop/kunde/1/bestellung/2").then().statusCode(400); 
                                // letzter Bestellposten entfernt die Bestellung

        }

}