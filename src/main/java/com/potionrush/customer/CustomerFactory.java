
package com.potionrush.customer;

import com.potionrush.brewing.PotionType;
import java.util.Random;

public class CustomerFactory {

    public static Customer buatKsatria(PotionType pesanan) {
        return new Customer("Knight", pesanan, new NormalPatience());
    }

    public static Customer buatPencuri(PotionType pesanan) {
        return new Customer("Thief", pesanan, new ShortPatience());
    }

    public static Customer buatPenyihir(PotionType pesanan) {
        return new Customer("Wizard", pesanan, new LongPatience());
    }

    public static Customer buatAcak(Random acak) {
        return buatAcak(acak, 1);
    }

    public static Customer buatAcak(Random acak, int hari) {
        int jenis = acak.nextInt(3);

        if (jenis == 0) {
            PotionType pesanan = ambilRamuanAcak(acak);
            return new Customer("Knight", pesanan,
                    new DailyPatience(new NormalPatience(), hari));

        } else if (jenis == 1) {
            PotionType pesanan = ambilRamuanAcak(acak);
            return new Customer("Thief", pesanan,
                    new DailyPatience(new ShortPatience(), hari));

        } else {
            PotionType pesanan;

            if (acak.nextBoolean()) {
                pesanan = PotionType.RACUN;
            } else {
                pesanan = PotionType.TAK_TERLIHAT;
            }

            return new Customer("Wizard", pesanan,
                    new DailyPatience(new LongPatience(), hari));
        }
    }

    private static PotionType ambilRamuanAcak(Random acak) {
        PotionType[] semuaRamuan = PotionType.values();
        int nomor = acak.nextInt(semuaRamuan.length);
        return semuaRamuan[nomor];
    }
}
