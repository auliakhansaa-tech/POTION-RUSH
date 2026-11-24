package com.potionrush.brewing;

public class AddIngredientCommand implements Command {
    private Cauldron kuali;
    private Ingredient bahan;
    private boolean bahanSudahMasuk = false;
    public AddIngredientCommand(Cauldron kuali, Ingredient bahan) {
        this.kuali = kuali;
        this.bahan = bahan;
    }
    @Override
    public void execute() {
        bahanSudahMasuk = kuali.tambahBahan(bahan);
    }
    @Override
    public boolean undo() {
        if (bahanSudahMasuk == false) {
            return false;
        }
        bahanSudahMasuk = false;
        return kuali.undo();
    }
}
