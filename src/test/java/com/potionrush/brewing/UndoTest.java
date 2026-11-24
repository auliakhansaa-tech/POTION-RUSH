package com.potionrush.brewing;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UndoTest {

    @Test
    void undoBerhasilSaatKualiDiisi() {
        Cauldron kuali = new Cauldron();
        CommandHistory riwayat = new CommandHistory();
        riwayat.jalankan(new AddIngredientCommand(kuali, Ingredient.DAUN));
        assertTrue(riwayat.undo());
        assertEquals(CauldronState.KOSONG, kuali.getStatus());
    }
    @Test
    void undoGagalSaatKualiMendidih() {
        Cauldron kuali = new Cauldron();
        CommandHistory riwayat = new CommandHistory();
        riwayat.jalankan(new AddIngredientCommand(kuali, Ingredient.DAUN));
        kuali.mulaiMendidih();
        assertFalse(riwayat.undo());
        assertEquals(1, kuali.getIsi().size());
    }
    @Test
    void undoSaatRiwayatKosong() {
        CommandHistory riwayat = new CommandHistory();
        assertFalse(riwayat.undo());
    }
    @Test
    void resetMengosongkanKuali() {
        Cauldron kuali = new Cauldron();
        kuali.tambahBahan(Ingredient.JAMUR);
        kuali.mulaiMendidih();
        kuali.reset();
        assertEquals(CauldronState.KOSONG, kuali.getStatus());
        assertTrue(kuali.getIsi().isEmpty());
    }
}