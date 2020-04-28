# Ohjelmistotekniikan harjoitustyö

Tämän harjoitustyön aiheena on koodinpätkämanageri.

[Vaatimusmäärittely](https://github.com/meklu/uni-ohtek/blob/master/dokkarit/vaatimusmäärittely.md)

[Tuntikirjanpito](https://github.com/meklu/uni-ohtek/blob/master/dokkarit/tuntikirjanpito.md)

[Arkkitehtuuri](https://github.com/meklu/uni-ohtek/blob/master/dokkarit/arkkitehtuuri.md)

[Käyttöohje](https://github.com/meklu/uni-ohtek/blob/master/dokkarit/käyttöohje.md)

# Releaset

[Viikko 5](https://github.com/meklu/uni-ohtek/releases/tag/viikko5.1)

# Ajo-ohjeita

## Ohjelmakoodin kääntäminen

`mvn compile`

## Paketointi

`mvn package`

Tämä generoi jar-paketin polkuun `target/patkis-1.0-SNAPSHOT.jar`

## Testien ajo

`mvn test`

## Testikattavuus

`mvn jacoco:report`

## Checkstyle

`mvn jxr:jxr checkstyle:checkstyle`

## Javadoc

`mvn javadoc:javadoc`
