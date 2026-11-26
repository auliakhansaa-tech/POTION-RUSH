
package com.potionrush.customer;

import com.potionrush.brewing.PotionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    void pesananBenarMembuatStatusDilayani() {
        Customer c = CustomerFactory.buatKsatria(PotionType.PENYEMBUH);

        assertTrue(c.layani(PotionType.PENYEMBUH));
        assertEquals(CustomerState.DILAYANI, c.getStatus());
    }

    @Test
    void pesananSalahDitolak() {
        Customer c = CustomerFactory.buatKsatria(PotionType.PENYEMBUH);

        assertFalse(c.layani(PotionType.RACUN));
        assertEquals(CustomerState.MENUNGGU, c.getStatus());
    }

    @Test
    void kesabaranHabisMembuatPetualangPergi() {
        Customer c = CustomerFactory.buatPencuri(PotionType.KEKUATAN);

        c.kurangiKesabaran(20);

        assertTrue(c.isHabisSabar());
        assertEquals(CustomerState.PERGI, c.getStatus());
    }

    @Test
    void bayaranBarHijauDapatBonus() {
        Customer c = CustomerFactory.buatKsatria(PotionType.PENYEMBUH);

        assertEquals(13, c.hitungBayaran());
    }

    @Test
    void bayaranBarMerahBerkurang() {
        Customer c = CustomerFactory.buatKsatria(PotionType.PENYEMBUH);

        c.kurangiKesabaran(20);

        assertEquals("MERAH", c.getTingkatKesabaran());
        assertEquals(8, c.hitungBayaran());
    }
}
