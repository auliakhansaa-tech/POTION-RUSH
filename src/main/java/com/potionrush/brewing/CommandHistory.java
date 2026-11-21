package com.potionrush.brewing;
import java.util.ArrayDeque;
import java.util.Deque;
// Menyimpan riwayat aksi supaya bisa di-undo (aksi terakhir dibatalkan duluan)
public class CommandHistory {
    private final Deque<Command> riwayat = new ArrayDeque<>();
    public void jalankan(Command aksi) {
        aksi.execute();
        riwayat.push(aksi);
    }
    public void undo() {
        if (!riwayat.isEmpty()) {
            riwayat.pop().undo();
        }
    }
    public boolean isKosong() {
        return riwayat.isEmpty();
    }
}
