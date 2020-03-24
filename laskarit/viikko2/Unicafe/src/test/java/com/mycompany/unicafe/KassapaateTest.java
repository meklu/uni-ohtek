package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class KassapaateTest {

    Kassapaate kassa;

    @Before
    public void setUp() {
        kassa = new Kassapaate();
    }

    @Test
    public void luotuKassaOlemassa() {
        assertTrue(kassa!=null);
    }

    @Test
    public void alkuSaldoOikein() {
        assertEquals(100000, kassa.kassassaRahaa());
    }

    @Test
    public void kateismaksuEdullisestaRiittaa() {
        assertEquals(60, kassa.syoEdullisesti(300));
        assertEquals(100240, kassa.kassassaRahaa());
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void kateismaksuEdullisestaEiRiita() {
        assertEquals(200, kassa.syoEdullisesti(200));
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void kateismaksuMaukkaastaRiittaa() {
        assertEquals(60, kassa.syoMaukkaasti(460));
        assertEquals(100400, kassa.kassassaRahaa());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void kateismaksuMaukkaastaEiRiita() {
        assertEquals(200, kassa.syoMaukkaasti(200));
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void korttimaksuEdullisestaRiittaa() {
        Maksukortti k = new Maksukortti(300);
        assertTrue(kassa.syoEdullisesti(k));
        assertEquals(60, k.saldo());
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void korttimaksuEdullisestaEiRiita() {
        Maksukortti k = new Maksukortti(200);
        assertFalse(kassa.syoEdullisesti(k));
        assertEquals(200, k.saldo());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void korttimaksuMaukkaastaRiittaa() {
        Maksukortti k = new Maksukortti(460);
        assertTrue(kassa.syoMaukkaasti(k));
        assertEquals(60, k.saldo());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void korttimaksuMaukkaastaEiRiita() {
        Maksukortti k = new Maksukortti(200);
        assertFalse(kassa.syoMaukkaasti(k));
        assertEquals(200, k.saldo());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void kortinLatausToimii() {
        Maksukortti k = new Maksukortti(200);
        kassa.lataaRahaaKortille(k, 12);
        assertEquals(212, k.saldo());
        assertEquals(100012, kassa.kassassaRahaa());
    }

    @Test
    public void kortinLatausEiNegatoi() {
        Maksukortti k = new Maksukortti(200);
        kassa.lataaRahaaKortille(k, -12);
        assertEquals(200, k.saldo());
        assertEquals(100000, kassa.kassassaRahaa());
    }
}
