package com.potionrush.ui;

import com.potionrush.brewing.CauldronState;
import com.potionrush.brewing.Ingredient;
import com.potionrush.brewing.PotionType;

import java.util.List;

public class DisplayName {

    public static String bahan(Ingredient bahan) {
        if (bahan == Ingredient.JAMUR) {
            return "Mushroom";
        } else if (bahan == Ingredient.DAUN) {
            return "Leaf";
        } else if (bahan == Ingredient.KRISTAL) {
            return "Crystal";
        } else if (bahan == Ingredient.BUNGA) {
            return "Flower";
        } else {
            return "Bone";
        }
    }

    public static String ramuan(PotionType ramuan) {
        if (ramuan == PotionType.PENYEMBUH) {
            return "Healing Potion";
        } else if (ramuan == PotionType.KEKUATAN) {
            return "Strength Potion";
        } else if (ramuan == PotionType.RACUN) {
            return "Poison";
        } else {
            return "Invisibility Potion";
        }
    }

    public static String statusKuali(CauldronState status) {
        if (status == CauldronState.KOSONG) {
            return "Empty";
        } else if (status == CauldronState.DIISI) {
            return "Filled";
        } else if (status == CauldronState.MENDIDIH) {
            return "Boiling";
        } else {
            return "Ready";
        }
    }

    public static String daftarBahan(List<Ingredient> daftar) {
        String teks = "";
        for (int i = 0; i < daftar.size(); i++) {
            if (i > 0) {
                teks = teks + " > ";
            }
            teks = teks + bahan(daftar.get(i));
        }
        return teks;
    }
}