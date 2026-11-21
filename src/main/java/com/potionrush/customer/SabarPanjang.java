package com.potionrush.customer;

public class SabarPanjang implements PatienceStrategy {

    @Override
    public double getKesabaranAwal() {
        return 35;
    }

    @Override
    public double kurangi(double sisaKesabaran, double detik) {
        return sisaKesabaran - detik;
    }
}