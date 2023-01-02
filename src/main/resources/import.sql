-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(nextval('hibernate_sequence'), 'field-1');
-- insert into myentity (id, field) values(nextval('hibernate_sequence'), 'field-2');
-- insert into myentity (id, field) values(nextval('hibernate_sequence'), 'field-3');

INSERT INTO Ware(W_ID, NAME, PREIS, BESCHREIBUNG, MENGE, BILD) VALUES(1,'Mauer und Putzmörtel 25kg', 5.99, 'Kalk-Zementmörtel zum Mauern und Putzen', 10, null);
INSERT INTO Ware(W_ID, NAME, PREIS, BESCHREIBUNG, MENGE, BILD) VALUES(2,'Gartenbaubeton 30kg', 6.99, 'Sack Gartenbeton für Betonierarbeiten im Garten', 15, null);
INSERT INTO Ware(W_ID, NAME, PREIS, BESCHREIBUNG, MENGE, BILD) VALUES(3,'Mörtelkübel 40l', 4.99, 'Zum Mörtel mischen und Schutt sammeln', 4, null);
INSERT INTO Ware(W_ID, NAME, PREIS, BESCHREIBUNG, MENGE, BILD) VALUES(4,'Maurerkelle 16cm', 10.99, 'Ideal für Maurer- und Putzarbeiten', 2, null);
INSERT INTO Ware(W_ID, NAME, PREIS, BESCHREIBUNG, MENGE, BILD) VALUES(5,'Kalkputz 30kg', 10.49, 'Grund- und Deckputz', 55, null);
INSERT INTO Ware(W_ID, NAME, PREIS, BESCHREIBUNG, MENGE, BILD) VALUES(6,'Montage- und Baukleber', 9.99, 'Ideal für dauerhafte Verbindungen', 100, null);
INSERT INTO Ware(W_ID, NAME, PREIS, BESCHREIBUNG, MENGE, BILD) VALUES(7,'Feuerfestmörtel 2kg', 9.29, 'Zum Ausbessern von Schäden an feuerfesten Auskleidungen', 1, null);
INSERT INTO Ware(W_ID, NAME, PREIS, BESCHREIBUNG, MENGE, BILD) VALUES(8,'Estrichbeton 10kg', 5.99, 'Für jegliche Beton- und Estricharbeiten', 40, null);
INSERT INTO Ware(W_ID, NAME, PREIS, BESCHREIBUNG, MENGE, BILD) VALUES(9,'Wandfarbe weiß 10l', 32.99, 'Hohe Deckkraft in der Regel genügt ein Anstrich', 12, null);
INSERT INTO Ware(W_ID, NAME, PREIS, BESCHREIBUNG, MENGE, BILD) VALUES(10,'Malerkrepp 5m', 8.99, 'Klebeband zum Abdecken bei Malerarbeiten', 5, null);
INSERT INTO Ware(W_ID, NAME, PREIS, BESCHREIBUNG, MENGE, BILD) VALUES(11,'Multivlies grau 10m', 9.29, 'für alle Handwerks-, Renovierungs-, Bau- und Malerarbeiten geeignet', 6, null);
INSERT INTO Ware(W_ID, NAME, PREIS, BESCHREIBUNG, MENGE, BILD) VALUES(12,'Farbroller-Set', 14.99, 'Starterset für Farbarbeiten', 3, null);