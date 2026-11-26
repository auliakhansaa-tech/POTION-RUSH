package com.potionrush.brewing;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeTest {

    @Test
    void urutanBenarCocok() {
        Recipe resep = new Recipe(PotionType.PENYEMBUH,
                List.of(Ingredient.DAUN, Ingredient.KRISTAL));
        assertTrue(resep.cocok(List.of(Ingredient.DAUN, Ingredient.KRISTAL)));
    }

    @Test
    void urutanTerbalikTidakCocok() {
        Recipe resep = new Recipe(PotionType.PENYEMBUH,
                List.of(Ingredient.DAUN, Ingredient.KRISTAL));
        assertFalse(resep.cocok(List.of(Ingredient.KRISTAL, Ingredient.DAUN)));
    }
}