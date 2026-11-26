package com.potionrush.brewing;
import java.util.ArrayList;
public class Cauldron {
    public static final double WAKTU_MENDIDIH = 4.0;
    private ArrayList<Ingredient> isi = new ArrayList<>();
    private CauldronState status = CauldronState.KOSONG;
    private double sisaWaktuMendidih = 0;
    private PotionType hasil = null;

    public boolean tambahBahan(Ingredient bahan) {
        if (status == CauldronState.MENDIDIH || status == CauldronState.SIAP)
        {
            return false;
        }
        isi.add(bahan);
        status = CauldronState.DIISI;
        return true;
    }

    public boolean undo() {
        if (status != CauldronState.DIISI) {
            return false;
        }
        int indexTerakhir = isi.size() - 1;
        isi.remove(indexTerakhir);
        if (isi.isEmpty()) {
            status = CauldronState.KOSONG;
        }
        return true;
    }
    public void mulaiMendidih() {
        if (status == CauldronState.DIISI) {
            status = CauldronState.MENDIDIH;
            sisaWaktuMendidih = WAKTU_MENDIDIH;
        }
    }

    public void update(double detik) {
        if (status != CauldronState.MENDIDIH) {
            return;
        }
        sisaWaktuMendidih = sisaWaktuMendidih - detik;
        if (sisaWaktuMendidih <= 0) {
            sisaWaktuMendidih = 0;
            hasil = racik();
            status = CauldronState.SIAP;
        }
    }

    public PotionType ambilRamuan() {
        if (status != CauldronState.SIAP) {
            return null;
        }
        PotionType ramuan = hasil;
        hasil = null;
        status = CauldronState.KOSONG;
        return ramuan;
    }

    public void reset() {
        isi.clear();
        status = CauldronState.KOSONG;
        sisaWaktuMendidih = 0;
        hasil = null;
    }

    public PotionType racik() {
        PotionType ramuan = null;
        for (Recipe resep : Recipe.semuaResep()) {
            if (resep.cocok(isi)) {
                ramuan = resep.getHasil();
                break;
            }
        }
        isi.clear();
        return ramuan;
    }

    public ArrayList<Ingredient> getIsi() {
        return isi;
    }
    public CauldronState getStatus() {
        return status;
    }
    public double getSisaWaktuMendidih() {
        return sisaWaktuMendidih;
    }
    public PotionType getHasil() {
        return hasil;
    }
}