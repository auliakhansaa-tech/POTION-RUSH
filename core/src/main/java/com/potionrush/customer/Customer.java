
package com.potionrush.customer;

import com.potionrush.brewing.PotionType;

public class Customer {

    private final String jenis;
    private final PotionType pesanan;
    private final PatienceStrategy strategiKesabaran;
    private final double kesabaranAwal;
    private double sisaKesabaran;

    private CustomerState status = CustomerState.MENUNGGU;

    public Customer(String jenis, PotionType pesanan, PatienceStrategy strategiKesabaran) {
        this.jenis = jenis;
        this.pesanan = pesanan;
        this.strategiKesabaran = strategiKesabaran;
        this.kesabaranAwal = strategiKesabaran.getKesabaranAwal();
        this.sisaKesabaran = kesabaranAwal;
    }

    public void kurangiKesabaran(double detik) {
        if (status != CustomerState.MENUNGGU) {
            return;
        }

        sisaKesabaran = strategiKesabaran.kurangi(sisaKesabaran, detik);

        if (sisaKesabaran <= 0) {
            sisaKesabaran = 0;
            status = CustomerState.PERGI;
        }
    }

    public boolean isHabisSabar() {
        return sisaKesabaran == 0;
    }

    public double getPersenKesabaran() {
        return sisaKesabaran / kesabaranAwal;
    }

    public String getTingkatKesabaran() {
        double persen = getPersenKesabaran();

        if (persen > 0.66) {
            return "HIJAU";
        } else if (persen > 0.33) {
            return "KUNING";
        } else {
            return "MERAH";
        }
    }

    public int hitungBayaran() {
        int harga = pesanan.getHarga();
        String tingkat = getTingkatKesabaran();

        if (tingkat.equals("HIJAU")) {
            return (int) Math.round(harga * 1.3);
        } else if (tingkat.equals("KUNING")) {
            return harga;
        } else {
            return (int) Math.round(harga * 0.8);
        }
    }

    public boolean layani(PotionType ramuan) {
        if (status != CustomerState.MENUNGGU) {
            return false;
        }

        if (ramuan == pesanan) {
            status = CustomerState.DILAYANI;
            return true;
        }

        return false;
    }

    public CustomerState getStatus() {
        return status;
    }

    public String getJenis() {
        return jenis;
    }

    public PotionType getPesanan() {
        return pesanan;
    }

    public boolean isSudahDilayani() {
        return status == CustomerState.DILAYANI;
    }
}
