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

![Kirjautumisruutu](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/manual/patkis-login.png)

## Rekisteröityminen

Rekisteröitymään pääsee kirjautumisnäkymästä painamalla nappia, jossa lukee
"Register an account". Tämän jälkeen haluamansa käyttäjätunnuksen voi syöttää
käyttäjätunnukselle varattuun tekstikenttään ja "Register" -nappia painamalla
järjestelmä rekisteröi käyttäjätunnuksen, mikäli se on mahdollista. Näkymä
palaa kirjautumisnäkymään onnistuneen rekisteröinnin yhteydessä tai jos
käyttäjä painaa erikseen paluunappia.

![Rekisteröintiruutu](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/manual/patkis-register.png)

## Päänäkymä

Kun käyttäjä on kirjautunut sisään onnistuneesti, hänet ohjataan listanäkymään
hänelle itselleen näkyvistä koodinpätkistä. Näihin lukeutuvat käyttäjän omat
koodinpätkät sekä muiden käyttäjien julkiseksi luokittelemat koodinpätkät.

![Päänäkymä](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/manual/patkis-mainview.png)

Tästä näkymästä käyttäjä voi kirjautua ulos painamalla ikkunan yläreunassa
sijaitsevaa "Log out" -nappia. Näin tehtäessä sovellus palaa
kirjautumisnäkymään.

## Koodinpätkien hallinta

Ruudun alareunassa on automaattisesti koodinpätkän lisäyslomake. Tähän on
helppo täyttää koodinpätkän tiedot. Tunnistekenttään kirjoitetaan halutut
tunnisteet välilyönnillä erotettuna. Tunnisteita ei tarvitse erikseen
tallentaa, vaan ne tallentuvat tietokantaan koodinpätkien lisäämisen ja
luomisen yhteydessä. Uloskirjautumisen yhteydessä käyttämättömät tunnisteet
poistetaan tietokannasta.

![Koodinpätkän lisääminen](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/manual/patkis-add-snip.png)

Koodinpätkän lisääminen onnistuu yksinkertaisesti lisäämisnappia painamalla.
Tämän jälkeen listanäkymä päivittyy automaattisesti vastaamaan tuoretta
tilannetta.

![Kontekstivalikko](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/manual/patkis-ctx-menu.png)

Koodinpätkien kanssa vuorovaikuttamiseen on useampi mukava ja intuitiivinen
keino. Tavallisimmat toiminnot löytyvät yläpalkista ja hiiren oikean korvan
takaa kontekstivalikosta. Tämän lisäksi tietyillä toiminnoilla on omat
näppäinyhdistelmänsä, joiden avulla ohjelman tehokäyttö helpottuu. Näistä on
kerrottu lisää alempana kohdassa **Näppäinyhdistelmät**. Koodinpätkän voi avata
tarkastelua varten myös kaksoisnapsauttamalla sitä hiirellä.

Tässä on kolmen kuvan kuvasarja koodinpätkän muokkaamisesta:

Aloitamme avaamalla halutun koodinpätkän.

![Koodinpätkän muokkaamisen aloitus](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/manual/patkis-edit-pre.png)

Tämän jälkeen voimme tehdä muutoksia.

![Koodinpätkän muokkaaminen](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/manual/patkis-edit-intra.png)

Muutoksien tallentamisen (tai perumisen) jälkeen ruudun alaosaan palaa taas
lisäysnäkymä ja muutokset ilmaantuvat koodinpätkälistaukseen.

![Koodinpätkän muokkaamisen jälkeen](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/manual/patkis-edit-post.png)

Koska järjestelmä kykenee useamman paikallisen käyttäjätilin toimintoihin, voit
tarkastella myös muiden käyttäjien koodinpätkistä niitä, jotka nämä ovat
asettaneet julkisiksi. Tällaisia koodinpätkiä et voi itse muokata, kuten alla
olevasta esimerkistä voidaan huomata:

![Kirjoitussuojattu koodinpätkä](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/manual/patkis-readonly.png)

Ohjelma mahdollistaa myös koodinpätkien suodattamisen tunnisteiden perusteella.
Suodatus toimii sillä periaatteella, että kaikki mihin tahansa valittuun
suodattimeen liittyvät koodinpätkät näytetään.

Suodattimia voi lisätä napsauttamalla hiirellä koodinpätkälistauksessa
esiintyvää tunnistetta. Nämä tunnistaa tunnistetta edeltävästä plus-merkistä.

Suodattimien poistaminen tapahtuu napsauttamalla hiirellä ruudun yläreunassa
sijaitsevassa suodatinlistauksessa esiintyvää tunnistetta. Nämä tunnistaa
tunnisteen perässä olevasta ruksista.

![Suodattimet toiminnassa](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/manual/patkis-filters.png)

## Näppäinyhdistelmät

`<Enter>`: Avaa valittu koodinpätkä

`<Delete>`: Poista valittu koodinpätkä

`<Ctrl>` + `C`: Kopioi valitun koodinpätkän koodi leikepöydälle

`<Ctrl>` + `S`: Tallenna käsittelyssä oleva koodinpätkä

`<Ctrl>` + `W`: Sulje käsittelyssä oleva koodinpätkä

`<Ctrl>` + `Q`: Kirjaudu ulos
