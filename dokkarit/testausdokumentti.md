# Testausdokumentti

Ohjelmalla on kohtuullisen kattavat yksikkö- ja integraatiotestit. Ohjelmaa on
tämän lisäksi testattu myös manuaalisesti järjestelmätasolla.

## Yksikkö- ja integraatiotestaus

Sovellustason logiikkaa
([domain-alapaketti](https://github.com/meklu/uni-ohtek/tree/master/patkis/src/main/java/org/meklu/patkis/domain))
testaa ensisijaisesti
[LogicTest-testiluokka.](https://github.com/meklu/uni-ohtek/blob/master/patkis/src/test/java/LogicTest.java)

Tietueiden ja tiedontalletuksen yksikkötestaus tehdään enimmäkseen erinäisten
[DB\*DaoTest-testiluokkien](https://github.com/meklu/uni-ohtek/blob/master/patkis/src/test/java/)
kautta.

Tämän lisäksi kaksi- ja kolmealkioisten apuluokkien
[Pair](https://github.com/meklu/uni-ohtek/tree/master/patkis/src/main/java/org/meklu/patkis/domain/Pair.java)
ja
[Triple](https://github.com/meklu/uni-ohtek/tree/master/patkis/src/main/java/org/meklu/patkis/domain/Triple.java)
testit
[PairTest](https://github.com/meklu/uni-ohtek/blob/master/patkis/src/test/java/PairTest.java)
ja
[TripleTest](https://github.com/meklu/uni-ohtek/blob/master/patkis/src/test/java/TripleTest.java)
ovat hyvin kattavat.

## Testikattavuus

Testikattavuus on kirjoitushetkellä rivikattavuuden osalta 84% ja
haarautumakattavuuden osalta 67%.

## Järjestelmätestaus

Sovelluksen järjestelmätestaus on suoritettu manuaalisesti.

### Asennus ja konfigurointi

Sovellusta on ajettu
[käyttöohjeen](https://github.com/meklu/uni-ohtek/blob/master/dokkarit/käyttöohje.md)
mukaisesti niin ilman valmista paikallista tietokantaa ja valmiin paikallisen
tietokannan kanssa GNU/Linux -ympäristöissä.

### Toiminnallisuus

Kaikki
[määrittelyasiakirjan](https://github.com/meklu/uni-ohtek/blob/master/dokkarit/vaatimusmäärittely.md)
ja käyttöohjeen listaamat ominaisuudet on käyty läpi ja todettu toimiviksi niin
kelvoilla kuin epäkelvoilla syötteillä.

## Sovellukseen jääneet laatuongelmat

Sovellus ei tällä hetkellä anna virheilmoituksia seuraavissa tilanteissa:

- kirjautumisen jälkeiset tietokantavirheet
