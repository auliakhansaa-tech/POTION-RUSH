package com.potionrush.brewing;
import java.util.ArrayList;

public class CommandHistory {
    private ArrayList<Command> riwayat = new ArrayList<>();
    public void jalankan(Command aksi) {
        aksi.execute();
        riwayat.add(aksi);
    }
    public boolean undo() {
        if (riwayat.isEmpty()) {
            return false;
        }
        int indexTerakhir = riwayat.size() - 1;
        Command aksiTerakhir = riwayat.remove(indexTerakhir);

        return aksiTerakhir.undo();
    }
    public void hapusSemua() {
        riwayat.clear();
    }
    public boolean isKosong() {
        return riwayat.isEmpty();
    }
}
