package com.potionrush.core;

// Menyimpan data permainan yang sedang berjalan
public class GameSession {
    private int koin = 0;
    private int reputasi = 5;
    private int hari = 1;

    public int getKoin() {
        return koin;
    }

    public int getReputasi() {
        return reputasi;
    }

    public int getHari() {
        return hari;
    }

    public void tambahKoin(int jumlah) {
        koin += jumlah;
    }

    // Reputasi berkurang 1, tapi tidak boleh kurang dari 0
    public void kurangiReputasi() {
        if (reputasi > 0) {
            reputasi--;
        }
    }

    // Pemain kalah kalau reputasi sudah habis
    public boolean isKalah() {
        return reputasi == 0;
    }

    public void lanjutHari() {
        hari++;
    }
}