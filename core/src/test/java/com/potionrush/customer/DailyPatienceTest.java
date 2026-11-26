
package com.potionrush.customer;

import com.potionrush.brewing.PotionType;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DailyPatienceTest {

    @Test
    void hariPertamaKecepatanNormal() {
        DailyPatience strategi = new DailyPatience(new NormalPatience(), 1);
        assertEquals(1.0, strategi.getKecepatan(), 0.001);
    }

    @Test
    void hariKetigaLebihCepat() {
        DailyPatience strategi = new DailyPatience(new NormalPatience(), 3);
        assertEquals(1.3, strategi.getKecepatan(), 0.001);
    }

    @Test
    void kecepatanMaksimalDuaKaliLipat() {
        DailyPatience strategi = new DailyPatience(new NormalPatience(), 20);
        assertEquals(2.0, strategi.getKecepatan(), 0.001);
    }

    @Test
    void pencuriDiHariKetigaLebihCepatPergi() {
        Customer pencuri = new Customer("Thief", PotionType.KEKUATAN,
                new DailyPatience(new ShortPatience(), 3));

        pencuri.kurangiKesabaran(10);
        assertEquals(2.0 / 15.0, pencuri.getPersenKesabaran(), 0.001);

        pencuri.kurangiKesabaran(2);
        assertEquals(CustomerState.PERGI, pencuri.getStatus());
    }

    @Test
    void petualangBaruSelaluMenunggu() {
        Random acak = new Random(42);

        for (int i = 0; i < 20; i++) {
            Customer petualang = CustomerFactory.buatAcak(acak, 5);
            assertEquals(CustomerState.MENUNGGU, petualang.getStatus());
            assertEquals(1.0, petualang.getPersenKesabaran(), 0.001);
        }
    }
}
