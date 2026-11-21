package com.potionrush.customer;

public class SabarNormal implements PatienceStrategy {

    @Override
    public double getKesabaranAwal() {
        return 25;
    }

    @Override
    public double kurangi(double sisaKesabaran, double detik) {
        return sisaKesabaran - detik;
    }
}