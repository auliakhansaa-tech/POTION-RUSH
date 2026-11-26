package com.potionrush.customer;

public interface PatienceStrategy {

    double getKesabaranAwal();

    double kurangi(double sisaKesabaran, double detik);
}