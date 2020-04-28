# Käyttöohje

Kaikki annetut komennot ajetaan repositorion juuresta löytyvässä hakemistossa
`patkis/`.

## Ohjelman ajaminen

Ohjelman voi kääntää ja paketoida antamalla komennon `mvn package`. Tämän
jälkeen sen voi ajaa komentamalla `java -jar target/patkis-1.0-SNAPSHOT.jar`.

## Kirjautuminen

Ohjelman avauduttua se aukeaa automaattisesti kirjautumisnäkymään. Tässä
näkymässä kirjautuminen onnistuu syöttämällä käyttäjätunnus tekstikenttään,
jonka ohessa lukee teksti "Username" ja painamalla sen jälkeen "Log in"
-nappia.

## Rekisteröityminen

Rekisteröitymään pääsee kirjautumisnäkymästä painamalla nappia, jossa lukee
"Register an account". Tämän jälkeen haluamansa käyttäjätunnuksen voi syöttää
käyttäjätunnukselle varattuun tekstikenttään ja "Register" -nappia painamalla
järjestelmä rekisteröi käyttäjätunnuksen, mikäli se on mahdollista. Näkymä
palaa kirjautumisnäkymään onnistuneen rekisteröinnin yhteydessä tai jos
käyttäjä painaa erikseen paluunappia.

## Koodinpätkien hallinta

Kun käyttäjä on kirjautunut sisään onnistuneesti, hänet ohjataan listanäkymään
hänelle itselleen näkyvistä koodinpätkistä.

Tästä näkymästä käyttäjä voi kirjautua ulos painamalla ikkunan yläreunassa
sijaitsevaa "Log out" -nappia. Näin tehtäessä sovellus palaa
kirjautumisnäkymään.
