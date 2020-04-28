# Vaatimusmäärittely

## Sovelluksen tarkoitus

Sovelluksen avulla käyttäjä voi tallettaa hyödyllisiä *koodinpätkiä* ja
etsiä jo tallettamiaan sellaisia.

Sovelluksella voi olla useampi *käyttäjä*, joilla jokaisella on omat
koodinpätkänsä. Koodinpätkän voi määritellä myös *julkiseksi*, jolloin
muutkin käyttäjät voivat halutessaan tarkastella sitä.

## Käyttäjät

Käyttäjärooleja on alustavasti vain yhtä, *normaalia*. Jossain vaiheessa
kehityskaarta sovellukseen voitaisiin lisätä *pääkäyttäjä*, joka voisi tehdä
suurempioikeuksista sisällönhallintaa.

## Toiminnallisuus

### Ennen kirjautumista

- [x] Käyttäjä voi luoda käyttäjätunnuksen
	- [x] tunnuksen täytyy olla uniikki
	- [ ] ja vähintään `n` -merkkinen
- [x] Käyttäjä voi kirjautua sisään
	- [x] kirjautuminen tapahtuu kirjoittamalla tunnukset
	  kirjautumislomakkeeseen

### Kirjautumisen jälkeen

- [x] Käyttäjä näkee yleiskatsauksen viimeisimmistä koodinpätkistään
	- [x] näkymärunko
	- [ ] datan populointi näkymään
- [ ] Käyttäjä voi lisätä uuden koodinpätkän
	- [ ] mikäli koodinpätkää ei määritellä julkiseksi, näkyy se vain
	  käyttäjälle itselleen
- [ ] Käyttäjä voi asettaa koodinpätkälle sanamuotoisia *tunnisteita*, jotka
  auttavat oikean koodinpätkän löytämisessä
	- tunniste voi olla esim. jokin seuraavista:
		- ohjelmointikieli
		- aihealue
		- jokin muu erittäin kuvaava deskriptori
- [ ] Käyttäjä voi selata koodinpätkiä tunnisteiden mukaan
- [ ] Käyttäjä voi poistaa luomansa koodinpätkän
- [x] Käyttäjä voi kirjautua ulos

## Jatkokehitysideoita

- Käyttäjä voi etsiä koodinpätkiä vapaalla tekstihaulla
	- myös otsikon mukaan
- Mahdollisuus tallentaa koodinpätkiä suosikkilistaan
- Lisää granulaarisuutta näkyvyyteen, esim. käyttäjäryhmäkohtaiset
  näkyvyysasetukset voisivat olla hyödyllisiä
- Tunnisteille voisi asettaa tunnistekohtaisen värin tai esimerkiksi
  automaattisesti tunnistetekstin pohjalta generoidun sellaisen
- Jos aikaa on maailmassa aivan rutosti liikaa, voisi koodinpätkille tehdä
  kommentointimahdollisuuden
- Jos aikaa on vähemmän, mutta paljon, voisi tehdä someista tutun
  reaktio-hässäkän koodinpätkille
