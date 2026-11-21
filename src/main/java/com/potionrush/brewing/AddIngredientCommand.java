package com.potionrush.brewing;
// Aksi memasukkan satu bahan ke satu kuali
public class AddIngredientCommand implements Command {
    private final Cauldron kuali;
    private final Ingredient bahan;
    private boolean berhasil = false;
    public AddIngredientCommand(Cauldron kuali, Ingredient bahan) {
        this.kuali = kuali;
        this.bahan = bahan;
    }
    @Override
    public void execute() {
        berhasil = kuali.tambahBahan(bahan);
    }
    // Hanya membatalkan kalau bahan tadi benar-benar masuk
    @Override
    public void undo() {
        if (berhasil) {
            kuali.undo();
            berhasil = false;
        }
    }
}

