package com.potionrush.brewing;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class CauldronStateTest {
    @Test
    void kualiBaruKosong() {
        assertEquals(CauldronState.KOSONG, new Cauldron().getStatus());
    }
    @Test
    void setelahMendidihRamuanSiap() {
        Cauldron k = new Cauldron();
        k.tambahBahan(Ingredient.DAUN);
        k.tambahBahan(Ingredient.KRISTAL);
        k.mulaiMendidih();
        assertEquals(CauldronState.MENDIDIH, k.getStatus());
        k.update(4.0);
        assertEquals(CauldronState.SIAP, k.getStatus());
        assertEquals(PotionType.PENYEMBUH, k.ambilRamuan());
        assertEquals(CauldronState.KOSONG, k.getStatus());
    }
    @Test
    void tidakBisaTambahBahanSaatMendidih() {
        Cauldron k = new Cauldron();
        k.tambahBahan(Ingredient.JAMUR);
        k.mulaiMendidih();
        assertFalse(k.tambahBahan(Ingredient.TULANG));
    }
    @Test
    void undoLewatCommandHistory() {
        Cauldron k = new Cauldron();
        CommandHistory riwayat = new CommandHistory();
        riwayat.jalankan(new AddIngredientCommand(k, Ingredient.JAMUR));
        riwayat.jalankan(new AddIngredientCommand(k, Ingredient.DAUN));
        riwayat.undo();
        assertEquals(1, k.getIsi().size());
        riwayat.undo();
        assertEquals(CauldronState.KOSONG, k.getStatus());
    }
}
