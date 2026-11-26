package com.potionrush.brewing;

// Jenis-jenis ramuan beserta harganya (dalam koin)
public enum PotionType {
    PENYEMBUH(10),
    KEKUATAN(12),
    RACUN(18),
    TAK_TERLIHAT(20);

    private final int harga;

    PotionType(int harga) {
        this.harga = harga;
    }

    public int getHarga() {
        return harga;
    }
}