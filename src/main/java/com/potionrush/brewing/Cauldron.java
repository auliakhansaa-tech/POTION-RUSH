package com.potionrush.brewing;

import java.util.ArrayList;
import java.util.List;

// Satu kuali tempat bahan dimasukkan
public class Cauldron {
    private final List<Ingredient> isi = new ArrayList<>();

    public void tambahBahan(Ingredient bahan) {
        isi.add(bahan);
    }

    // Menghapus bahan terakhir, kalau kuali tidak kosong
    public void undo() {
        if (!isi.isEmpty()) {
            isi.remove(isi.size() - 1);
        }
    }

    public List<Ingredient> getIsi() {
        return isi;
    }

    // Mencari resep yang cocok dengan isi kuali.
    // Mengembalikan null kalau tidak ada yang cocok (ramuan gagal).
    // Setelah diracik, kuali dikosongkan.
    public PotionType racik() {
        PotionType hasil = null;
        for (Recipe resep : Recipe.semuaResep()) {
            if (resep.cocok(isi)) {
                hasil = resep.getHasil();
                break;
            }
        }
        isi.clear();
        return hasil;
    }
}