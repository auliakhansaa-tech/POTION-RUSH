package com.potionrush.customer;

import com.potionrush.brewing.PotionType;

public class Customer {
    private final String jenis;
    private final PotionType pesanan;
    private final double kesabaranAwal;
    private double sisaKesabaran;
    private boolean sudahDilayani = false;

    public Customer(String jenis, PotionType pesanan, double kesabaran) {
        this.jenis = jenis;
        this.pesanan = pesanan;
        this.kesabaranAwal = kesabaran;
        this.sisaKesabaran = kesabaran;
    }

    public void kurangiKesabaran(double detik) {
        sisaKesabaran -= detik;
        if (sisaKesabaran < 0) {
            sisaKesabaran = 0;
        }
    }

    public boolean isHabisSabar() {
        return sisaKesabaran == 0;
    }

    public double getPersenKesabaran() {
        return sisaKesabaran / kesabaranAwal;
    }

    public boolean layani(PotionType ramuan) {
        if (ramuan == pesanan) {
            sudahDilayani = true;
            return true;
        }
        return false;
    }

    public String getJenis() {
        return jenis;
    }

    public PotionType getPesanan() {
        return pesanan;
    }

    public boolean isSudahDilayani() {
        return sudahDilayani;
    }
}
