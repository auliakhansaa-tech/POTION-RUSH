package com.potionrush.customer;

import com.potionrush.brewing.PotionType;

public class CustomerFactory {

    public static Customer buatKsatria(PotionType pesanan) {
        return new Customer("Ksatria", pesanan, 25);
    }

    public static Customer buatPencuri(PotionType pesanan) {
        return new Customer("Pencuri", pesanan, 15);
    }

    public static Customer buatPenyihir(PotionType pesanan) {
        return new Customer("Penyihir", pesanan, 35);
    }
}
