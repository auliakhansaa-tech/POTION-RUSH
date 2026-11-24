package com.potionrush.customer;

public class ShortPatience implements PatienceStrategy {

    @Override
    public double getKesabaranAwal() {
        return 15;
    }

    @Override
    public double kurangi(double sisaKesabaran, double detik) {
        return sisaKesabaran - detik;
    }
}