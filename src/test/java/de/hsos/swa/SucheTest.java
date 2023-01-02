package de.hsos.swa;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

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
public class SucheTest {

        @Test
        public void AlleSucheTests() {
                testSucheAlleWaren();
                testSucheWareNachId();
                testSucheWareNachName();
        }

        public void testSucheAlleWaren() {
                given().contentType(ContentType.JSON).when().get("/baumarkt-shop/katalog").then().statusCode(200);
        }

        public void testSucheWareNachId() {
                given().contentType(ContentType.JSON).when().get("/baumarkt-shop/id/1").then().statusCode(200).body(
                                "name",
                                is("Mauer und Putzmörtel 25kg"), "beschreibung",
                                is("Kalk-Zementmörtel zum Mauern und Putzen"));

                given().contentType(ContentType.JSON).when().get("/baumarkt-shop/id/5").then().statusCode(200).body(
                                "name",
                                is("Kalkputz 30kg"), "beschreibung", is("Grund- und Deckputz"));

                given().contentType(ContentType.JSON).when().get("/baumarkt-shop/id/8").then().statusCode(200).body(
                                "name",
                                is("Estrichbeton 10kg"), "beschreibung", is("Für jegliche Beton- und Estricharbeiten"));
        }

        public void testSucheWareNachName() {
                given().contentType(ContentType.JSON).when().get("/baumarkt-shop/name/5m").then().statusCode(200).body(
                                "name",
                                hasItems("Malerkrepp 5m"), "beschreibung",
                                hasItems("Klebeband zum Abdecken bei Malerarbeiten"));

                given().contentType(ContentType.JSON).when().get("/baumarkt-shop/name/10l").then().statusCode(200).body(
                                "name",
                                hasItems("Wandfarbe weiß 10l"), "beschreibung",
                                hasItems("Hohe Deckkraft in der Regel genügt ein Anstrich"));

                given().contentType(ContentType.JSON).when().get("/baumarkt-shop/name/30kg").then().statusCode(200)
                                .body("name",
                                                hasItems("Gartenbaubeton 30kg"), "beschreibung",
                                                hasItems("Sack Gartenbeton für Betonierarbeiten im Garten"));

                given().contentType(ContentType.JSON).when().get("/baumarkt-shop/name/30kg").then().statusCode(200)
                                .body("name",
                                                hasItems("Kalkputz 30kg"), "beschreibung",
                                                hasItems("Grund- und Deckputz"));
        }
}
