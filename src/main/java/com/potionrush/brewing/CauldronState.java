package com.potionrush.brewing;
// Status kuali (pola State)
public enum CauldronState {
    KOSONG, // belum ada bahan
    DIISI, // sudah ada bahan, boleh ditambah atau di-undo
    MENDIDIH, // sedang mendidih, tidak boleh diubah
    SIAP // ramuan sudah jadi dan bisa diambil
}
