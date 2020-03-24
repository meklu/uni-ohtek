package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }

    @Test
    public void alkuSaldoOikein() {
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void latausToimii() {
        kortti.lataaRahaa(101);
        assertEquals(111, kortti.saldo());
    }

    @Test
    public void veloitusToimii() {
        kortti.otaRahaa(10);
        assertEquals(0, kortti.saldo());
    }

    @Test
    public void yliveloitusMahdoton() {
        kortti.otaRahaa(11);
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void veloituksenTotuusarvoTasmaa() {
        assertEquals(true, kortti.otaRahaa(7));
        assertEquals(false, kortti.otaRahaa(7));
    }
}
