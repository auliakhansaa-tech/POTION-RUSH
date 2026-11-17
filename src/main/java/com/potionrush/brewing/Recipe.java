package com.potionrush.brewing;

import java.util.List;

// Satu resep: ramuan yang dihasilkan dan urutan bahannya
public class Recipe {
    private final PotionType hasil;
    private final List<Ingredient> urutanBahan;

    public Recipe(PotionType hasil, List<Ingredient> urutanBahan) {
        this.hasil = hasil;
        this.urutanBahan = urutanBahan;
    }

    // true kalau bahan dan urutannya sama persis dengan resep
    public boolean cocok(List<Ingredient> bahan) {
        return urutanBahan.equals(bahan);
    }

    public PotionType getHasil() {
        return hasil;
    }

    // Daftar semua resep yang ada di game
    public static List<Recipe> semuaResep() {
        return List.of(
                new Recipe(PotionType.PENYEMBUH,
                        List.of(Ingredient.DAUN, Ingredient.KRISTAL)),
                new Recipe(PotionType.KEKUATAN,
                        List.of(Ingredient.JAMUR, Ingredient.TULANG)),
                new Recipe(PotionType.RACUN,
                        List.of(Ingredient.JAMUR, Ingredient.DAUN, Ingredient.TULANG)),
                new Recipe(PotionType.TAK_TERLIHAT,
                        List.of(Ingredient.BUNGA, Ingredient.KRISTAL, Ingredient.DAUN))
        );
    }
}