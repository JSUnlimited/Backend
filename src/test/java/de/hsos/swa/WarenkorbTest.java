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
public class WarenkorbTest {

        @Test
        public void AlleWarenkorbTests() {
                testWarenkorbAnzeigen();
                testWareHinzufuegen();
                testWarenkorbBestellen();
        }

        public void testWarenkorbAnzeigen() {
                // Kunde registrieren
                given().contentType(ContentType.JSON).body(
                                "{\"email\":\"paul.panzer@gmx.de\",\"passwort\":\"paul1234\", \"vorname\":\"Paul\", \"nachname\":\"Panzer\"}")
                                .when()
                                .post("/baumarkt-shop/user").then().statusCode(200);

                // Adresse anlegen
                given().auth().basic("paul.panzer@gmx.de", "paul1234").contentType(ContentType.JSON)
                                .body("{\"plz\":\"00000\", \"ort\":\"Teststadt\", \"strasse\":\"Teststraße\", \"hausnr\":\"1\"}")
                                .post("baumarkt-shop/kunde/3/adresse").then().statusCode(200);

                // Ware zum Warenkorb hinzufuegen
                given().auth().basic("paul.panzer@gmx.de", "paul1234").contentType(ContentType.JSON)
                                .body("{\"warenId\":\"2\",\"menge\":\"1\"}").when()
                                .put("/baumarkt-shop/kunde/3/warenkorb").then().statusCode(200);

                // Warenkorb anzeigen
                given().auth().basic("paul.panzer@gmx.de", "paul1234").contentType(ContentType.JSON).when()
                                .get("/baumarkt-shop/kunde/3/warenkorb").then().statusCode(200);
        }

        public void testWareHinzufuegen() {
                given().auth().basic("paul.panzer@gmx.de", "paul1234").contentType(ContentType.JSON)
                                .body("{\"warenId\":\"7\",\"menge\":\"1\"}").when()
                                .put("/baumarkt-shop/kunde/3/warenkorb").then().statusCode(200);
        }

        public void testWarenkorbBestellen() {
                given().auth().basic("paul.panzer@gmx.de", "paul1234").contentType(ContentType.JSON)
                                .post("/baumarkt-shop/kunde/3/warenkorb").then().statusCode(200);
        }
}