package com.potionrush.brewing;
// Satu aksi pemain yang bisa dijalankan dan dibatalkan (pola Command)
public interface Command {
    void execute();
    void undo();
}
