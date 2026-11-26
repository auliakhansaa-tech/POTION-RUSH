
package com.potionrush.customer;

public class DailyPatience implements PatienceStrategy {

    private PatienceStrategy strategiDasar;
    private double kecepatan;

    public DailyPatience(PatienceStrategy strategiDasar, int hari) {
        this.strategiDasar = strategiDasar;

        kecepatan = 1 + 0.15 * (hari - 1);

        if (kecepatan > 2.0) {
            kecepatan = 2.0;
        }
    }

    @Override
    public double getKesabaranAwal() {
        return strategiDasar.getKesabaranAwal();
    }

    @Override
    public double kurangi(double sisaKesabaran, double detik) {
        double detikTerasa = detik * kecepatan;
        return strategiDasar.kurangi(sisaKesabaran, detikTerasa);
    }

    public double getKecepatan() {
        return kecepatan;
    }
}
