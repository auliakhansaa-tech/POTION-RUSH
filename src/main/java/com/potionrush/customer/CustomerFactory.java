
package com.potionrush.customer;

import com.potionrush.brewing.PotionType;
import java.util.Random;

public class CustomerFactory {

    public static Customer buatKsatria(PotionType pesanan) {
        return new Customer("Ksatria", pesanan, new SabarNormal());
    }

    public static Customer buatPencuri(PotionType pesanan) {
        return new Customer("Pencuri", pesanan, new SabarPendek());
    }

    public static Customer buatPenyihir(PotionType pesanan) {
        return new Customer("Penyihir", pesanan, new SabarPanjang());
    }

    public static Customer buatAcak(Random acak) {
        int jenis = acak.nextInt(3);

        if (jenis == 2) {
            PotionType pesanan = acak.nextBoolean()
                    ? PotionType.RACUN
                    : PotionType.TAK_TERLIHAT;

            return buatPenyihir(pesanan);
        }

        PotionType[] semua = PotionType.values();
        PotionType pesanan = semua[acak.nextInt(semua.length)];

        if (jenis == 0) {
            return buatKsatria(pesanan);
        }

        return buatPencuri(pesanan);
    }
}
