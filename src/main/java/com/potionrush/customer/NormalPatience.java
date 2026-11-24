package com.potionrush.customer;

public class NormalPatience implements PatienceStrategy {

    @Override
    public double getKesabaranAwal() {
        return 25;
    }

    @Override
    public double kurangi(double sisaKesabaran, double detik) {
        return sisaKesabaran - detik;
    }
}