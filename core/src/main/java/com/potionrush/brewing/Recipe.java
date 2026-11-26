package com.potionrush.brewing;
import java.util.ArrayList;
import java.util.List;
public class Recipe {
    private PotionType hasil;
    private List<Ingredient> urutanBahan;
    public Recipe(PotionType hasil, List<Ingredient> urutanBahan) {
        this.hasil = hasil;
        this.urutanBahan = urutanBahan;
    }

    public boolean cocok(List<Ingredient> bahan) {
        return urutanBahan.equals(bahan);
    }
    public PotionType getHasil() {
        return hasil;
    }
    public List<Ingredient> getUrutanBahan() {
        return urutanBahan;
    }
    public static ArrayList<Recipe> semuaResep() {
        ArrayList<Recipe> daftarResep = new ArrayList<>();
        daftarResep.add(new Recipe(PotionType.PENYEMBUH,
                List.of(Ingredient.DAUN, Ingredient.KRISTAL)));
        daftarResep.add(new Recipe(PotionType.KEKUATAN,
                List.of(Ingredient.JAMUR, Ingredient.TULANG)));
        daftarResep.add(new Recipe(PotionType.RACUN,
                List.of(Ingredient.JAMUR, Ingredient.DAUN,
                        Ingredient.TULANG)));
        daftarResep.add(new Recipe(PotionType.TAK_TERLIHAT,
                List.of(Ingredient.BUNGA, Ingredient.KRISTAL,
                        Ingredient.DAUN)));
        return daftarResep;
    }
}
