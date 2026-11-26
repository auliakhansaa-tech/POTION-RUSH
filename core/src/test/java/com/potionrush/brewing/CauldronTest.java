package com.potionrush.brewing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CauldronTest {

    @Test
    void undoMenghapusBahanTerakhir() {
        Cauldron kuali = new Cauldron();
        kuali.tambahBahan(Ingredient.JAMUR);
        kuali.tambahBahan(Ingredient.DAUN);
        kuali.undo();
        assertEquals(1, kuali.getIsi().size());
        assertEquals(Ingredient.JAMUR, kuali.getIsi().get(0));
    }

    @Test
    void racikResepBenar() {
        Cauldron kuali = new Cauldron();
        kuali.tambahBahan(Ingredient.DAUN);
        kuali.tambahBahan(Ingredient.KRISTAL);
        assertEquals(PotionType.PENYEMBUH, kuali.racik());
        assertTrue(kuali.getIsi().isEmpty());
    }

    @Test
    void racikResepSalah() {
        Cauldron kuali = new Cauldron();
        kuali.tambahBahan(Ingredient.KRISTAL);
        kuali.tambahBahan(Ingredient.DAUN);
        assertNull(kuali.racik());
    }
}