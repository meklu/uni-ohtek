# Arkkitehtuuri

## Rakenne

Sovelluksessa käytetään kolmikerroksista pakkausrakennetta.

![Pakkausrakenne](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/architecture/structure.png)

Pakkauksen `org.meklu.patkis` alipakkaus `ui` sisältää käyttöliittymälogiikan,
`domain` varsinaisen sovelluslogiikan ja `dao` huolehtii tiedon
tallentamisesta.

## Käyttöliittymä

Sovelluksen käyttöliittymä sisältää muutaman eri näkymän
- kirjautumisruutu
- rekisteröitymisruutu
- koodinpätkien listanäkymä

Nämä on toteutettu `org.meklu.patkis.ui.View` -rajapinnan toteutuksina. Näistä
jokainen toteuttaa siis sisäisen tilansa lisäksi oleellisesti `getStage()`
-metodin, joka palauttaa JavaFX:n `Stage`-olion. Näiden `Stage`-olioiden
välillä sitten vaihdellaan sovelluksen elinkaaren aikana näkymästä toiseen
siirtyessä.

Käyttöliittymä on eristetty sovelluslogiikasta ja kutsuu varta vasten
tarkoitukselle pyhitettyjen `domain`-alipakkauksen luokkien metodeja
sovelluksen tarpeiden edellyttämällä tavalla sovelluksen tilaa muuttaakseen.

## Sovelluslogiikka

Sovelluksen loogisen datamallin muodostavat tietomalliluokat
[User](https://github.com/meklu/uni-ohtek/blob/master/patkis/src/main/java/org/meklu/patkis/domain/User.java),
[Snippet](https://github.com/meklu/uni-ohtek/blob/master/patkis/src/main/java/org/meklu/patkis/domain/Snippet.java)
sekä
[Tag](https://github.com/meklu/uni-ohtek/blob/master/patkis/src/main/java/org/meklu/patkis/domain/Tag.java),
jotka kuvaavat käyttäjiä, koodinpätkiä sekä tunnisteita.

![Datamalli](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/architecture/data-model.png)

Sovellustoiminnallisuudet toteuttaa luokka
[Logic](https://github.com/meklu/uni-ohtek/blob/master/patkis/src/main/java/org/meklu/patkis/domain/Logic.java).
Tämä tarjoaa käyttöliittymälle sen tarvitsemat sovellusloogiset toiminnot,
kuten sisäänkirjautumisen, rekisteröitymisen ja koodinpätkätietueiden
listaamisen ja luomisen.

![Sovelluslogiikkakaavio](https://raw.githubusercontent.com/meklu/uni-ohtek/master/imgs/architecture/application-logic.png)

`Logic` käsittelee tietueiden käsittelyn välillisesti `dao`-alipakkauksen
Dao-rajapintojen avulla varsinaisia implementaatioita näiden kautta
käyttämällä.

## Tietojen pysyväistallennus

Tietojen pysyväistallennuksesta huolehtivat alipakkauksen `dao` luokat
`DB{User,Snippet,Tag}Dao`. Nämä käyttävät alipakkauksen `domain` luokkaa
[Database](https://github.com/meklu/uni-ohtek/blob/master/patkis/src/main/java/org/meklu/patkis/domain/Database.java)
kommunikoidakseen niitä takaavan SQLite -tietokannan kanssa.

Valtaosa talletustoteutuksesta on abstrahoitu hyvin geneeriseksi, millä koodin
turha toistaminen on saatu melko alhaiselle tasolle. Rakenne jättää myös
vaihtoehtoisten talletusmenetelmien kuten muiden tietokantojen
käyttöönottamisen kohtalaisen kivuttomaksi.
